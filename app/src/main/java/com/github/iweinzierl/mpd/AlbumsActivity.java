package com.github.iweinzierl.mpd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.iweinzierl.mpd.async.ListAlbumsByArtistTask;
import com.github.iweinzierl.mpd.async.ListAlbumsTask;
import com.github.iweinzierl.mpd.async.ListSongsByAlbumTask;
import com.github.iweinzierl.mpd.async.SavePlaylistTask;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.Playlist;
import com.github.iweinzierl.mpd.domain.Song;
import com.github.iweinzierl.mpd.fragment.AlbumsFragment;
import com.google.common.base.Strings;

import java.util.List;

public class AlbumsActivity extends BaseActivity implements AlbumsFragment.Callback {

    public static final String EXTRA_ARTIST_NAME = "com.github.iweinzierl.mpd.AlbumsActivity.EXTRA_ARTIST_NAME";

    private AlbumsFragment albumsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        albumsFragment = new AlbumsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, albumsFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startProgress(getString(R.string.albums_progress_load_albums));

        final String artistName = getArtistNameFromIntent();
        if (Strings.isNullOrEmpty(artistName)) {
            new ListAlbumsTask(this) {
                @Override
                protected void onPostExecute(List<Album> albums) {
                    super.onPostExecute(albums);
                    setAlbums(albums);
                    stopProgress();
                }
            }.execute();
        } else {
            new ListAlbumsByArtistTask(this) {
                @Override
                protected void onPostExecute(List<Album> albums) {
                    super.onPostExecute(albums);
                    setAlbums(albums);
                    stopProgress();
                }
            }.execute(artistName);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    public void onAlbumClicked(Album album) {
        // nothing to do
    }

    @Override
    public void onAddReplacePlay(Album album) {
        startAddReplacePlayProgress(album);
    }

    private void startAddReplacePlayProgress(final Album album) {
        startProgress(getString(R.string.albums_progress_add_replace_play));

        new ListSongsByAlbumTask(this) {
            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                addReplacePlay(album, songs);
            }
        }.execute(album.getName());
    }

    private void addReplacePlay(final Album album, final List<Song> songs) {
        if (!isProgress()) {
            startProgress(getString(R.string.albums_progress_add_replace_play));
        }

        Playlist playlist = new Playlist();
        playlist.setName(Playlist.CURRENT);
        playlist.setSongs(songs);

        new SavePlaylistTask(this) {
            @Override
            protected void onPostExecute(Playlist playlist) {
                super.onPostExecute(playlist);
                stopProgress();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                AlbumsActivity.this,
                                getString(R.string.albums_notification_add_replace_play_successful, album.getName()),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                getPlayer().play();
            }
        }.execute(playlist);
    }

    private String getArtistNameFromIntent() {
        Intent data = getIntent();
        return data.getStringExtra(EXTRA_ARTIST_NAME);
    }

    private void setAlbums(List<Album> albums) {
        albumsFragment.setAlbums(albums);
    }
}
