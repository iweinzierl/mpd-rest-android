package com.github.iweinzierl.mpd;

import android.app.Application;

import com.github.iweinzierl.mpd.player.Player;

public class MpdProxyApplication extends Application {

    private Player player;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new Player(this);
    }

    public Player getPlayer() {
        return player;
    }
}
