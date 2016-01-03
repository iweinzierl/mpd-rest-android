package com.github.iweinzierl.mpd.domain;

import java.util.Collection;

public class Playlist {

    public static final String CURRENT = "CURRENT";

    private String name;

    private Collection<Song> songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }
}
