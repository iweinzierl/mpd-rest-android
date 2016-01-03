package com.github.iweinzierl.mpd.domain;

import com.orm.dsl.Table;

@Table
public class PlayerInfo {

    private Long id;

    private PlayerStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
}
