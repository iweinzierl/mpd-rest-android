package com.github.iweinzierl.mpd.helper;

import com.github.iweinzierl.mpd.domain.Album;

import java.util.Comparator;

public class AlbumComparator implements Comparator<Album> {

    @Override
    public int compare(Album a, Album b) {
        return a.getName().compareTo(b.getName());
    }
}
