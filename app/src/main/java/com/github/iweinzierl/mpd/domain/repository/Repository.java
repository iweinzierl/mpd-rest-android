package com.github.iweinzierl.mpd.domain.repository;

import android.content.Context;

import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.domain.Song;
import com.orm.SugarRecord;

import java.util.List;

public class Repository {

    private final Context context;

    public Repository(Context context) {
        this.context = context;
    }

    public boolean isCacheEnabled() {
        // TODO evaluate settings
        return true;
    }

    public List<Artist> listArtists() {
        return SugarRecord.listAll(Artist.class);
    }

    public List<Album> listAlbums() {
        return SugarRecord.listAll(Album.class);
    }

    public List<Album> findAlbums(final String artistName) {
        List<Artist> artist = SugarRecord.find(
                Artist.class,
                "name = ?",
                artistName
        );

        return SugarRecord.find(
                Album.class,
                "artist = ?",
                String.valueOf(artist.get(0).getId())
        );
    }

    public List<Song> findSongs(final String albumName) {
        List<Album> album = SugarRecord.find(
                Album.class,
                "name = ?",
                albumName);

        return SugarRecord.find(
                Song.class,
                "album = ?",
                String.valueOf(album.get(0).getId())
        );
    }
}
