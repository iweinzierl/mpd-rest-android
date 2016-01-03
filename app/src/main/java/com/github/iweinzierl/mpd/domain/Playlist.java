package com.github.iweinzierl.mpd.domain;

import com.orm.dsl.Table;

import java.util.Collection;

@Table
public class Playlist {

    public static final String CURRENT = "CURRENT";

    private Long id;

    private String name;

    private Collection<Song> songs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
