package com.github.iweinzierl.mpd.helper;

import com.github.iweinzierl.mpd.domain.Artist;

import java.util.Comparator;

public class ArtistComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist a, Artist b) {
        return a.getName().compareTo(b.getName());
    }
}
