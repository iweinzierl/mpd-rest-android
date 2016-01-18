package com.github.iweinzierl.mpd.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.MpdProxyApplication;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.domain.Song;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;
import com.google.common.base.Strings;
import com.orm.SugarRecord;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;

public class LibrarySyncProcess {

    public interface SyncCallback {
        void onSyncSucceeded();

        void onSyncFailed();
    }

    private static class SyncException extends Exception {
        public SyncException(String detailMessage) {
            super(detailMessage);
        }
    }

    private static class SyncStatistic {
        public long syncStart;

        public int syncedArtists;
        public int syncedAlbums;
        public int syncedSongs;

        public SyncStatistic() {
            syncStart = System.currentTimeMillis();
        }
    }

    private class EntityExtractor {
        private Map<String, Artist> artists = new HashMap<>();
        private Map<String, Album> albums = new HashMap<>();

        public void extract(Collection<Song> songs) {
            artists.clear();
            albums.clear();

            for (Song song : songs) {
                extractArtist(song);
                extractAlbums(song);
            }
        }

        public Map<String, Artist> getArtists() {
            return artists;
        }

        public Map<String, Album> getAlbums() {
            return albums;
        }

        private void extractArtist(Song song) {
            Artist artist = song.getAlbum().getArtist();
            artists.put(artist.getName(), artist);
        }

        private void extractAlbums(Song song) {
            Album album = song.getAlbum();
            albums.put(album.getName(), album);
        }
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[sync]").getLogger(LibrarySyncProcess.class.getName());
    private static final String PREF_SYNC_DATE = "com.github.iweinzierl.mpd.helper.LibrarySyncProcess.PREFERENCES.sync.date";


    private final Context context;
    private final SyncCallback callback;

    public LibrarySyncProcess(Context context) {
        this(context, null);
    }

    public LibrarySyncProcess(Context context, SyncCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public boolean isSyncRequired() {
        SharedPreferences prefs = context.getSharedPreferences(MpdProxyApplication.PREFS_APP, Context.MODE_PRIVATE);
        String syncDate = prefs.getString(PREF_SYNC_DATE, null);

        return Strings.isNullOrEmpty(syncDate);
    }

    public void sync() {
        final SyncStatistic syncStatistic = new SyncStatistic();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                LOG.info("Sync started");

                try {
                    doSync(syncStatistic);
                } catch (IOException | SyncException e) {
                    LOG.error("Sync failed", e);

                    if (callback != null) {
                        callback.onSyncFailed();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                LOG.info("Sync finished (took {} ms)", System.currentTimeMillis() - syncStatistic.syncStart);
                LOG.debug("  -> synced artists: {}", syncStatistic.syncedArtists);
                LOG.debug("  -> synced albums:  {}", syncStatistic.syncedAlbums);
                LOG.debug("  -> synced songs:   {}", syncStatistic.syncedSongs);

                setSyncDate();

                if (callback != null) {
                    callback.onSyncSucceeded();
                }
            }
        }.execute();
    }

    private void doSync(SyncStatistic syncStatistic) throws IOException, SyncException {
        MpdProxyClient proxyClient = ClientFactory.createSyncClient(context);

        Call<List<Song>> listCall = proxyClient.listSongs();
        Response<List<Song>> response = listCall.execute();

        if (response.code() != 200) {
            LOG.warn("Unable to sync library! Http status code = " + response.code());
            return;
        }

        final List<Song> songs = response.body();
        LOG.debug("Received {} songs", songs.size());

        EntityExtractor extractor = new EntityExtractor();
        extractor.extract(songs);

        SugarRecord.deleteAll(Song.class);
        SugarRecord.deleteAll(Album.class);
        SugarRecord.deleteAll(Artist.class);

        Collection<Artist> artists = extractor.getArtists().values();
        SugarRecord.saveInTx(artists);

        Collection<Album> albums = extractor.getAlbums().values();
        applyEntitiesToAlbums(albums, extractor.getArtists());
        SugarRecord.saveInTx(albums);

        applyEntitiesToSongs(songs, extractor.getArtists(), extractor.getAlbums());
        SugarRecord.saveInTx(songs);

        syncStatistic.syncedArtists = artists.size();
        syncStatistic.syncedAlbums = albums.size();
        syncStatistic.syncedSongs = songs.size();
    }

    private void setSyncDate() {
        SharedPreferences prefs = context.getSharedPreferences(MpdProxyApplication.PREFS_APP, Context.MODE_PRIVATE);
        String syncDate = DateTime.now().toString("yyyy-MM-dd HH:mm");
        prefs.edit().putString(PREF_SYNC_DATE, syncDate).apply();
    }

    private void applyEntitiesToAlbums(Collection<Album> albums, Map<String, Artist> artists) {
        for (Album album : albums) {
            Artist artist = artists.get(album.getArtist().getName());
            album.setArtist(artist);
        }
    }

    private void applyEntitiesToSongs(Collection<Song> songs, Map<String, Artist> artists, Map<String, Album> albums) {
        for (Song song : songs) {
            Artist artist = artists.get(song.getArtist().getName());
            Album album = albums.get(song.getAlbum().getName());

            song.setArtist(artist);
            song.setAlbum(album);
        }
    }
}
