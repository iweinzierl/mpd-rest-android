package com.github.iweinzierl.mpd;

import com.github.iweinzierl.mpd.helper.LibrarySyncProcess;
import com.github.iweinzierl.mpd.player.Player;
import com.orm.SugarApp;

public class MpdProxyApplication extends SugarApp {

    public static final String PREFS_APP = "com.github.iweinzierl.mpd.MpdProxyApplication.PREFERENCES";

    private Player player;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new Player(this);

        LibrarySyncProcess syncProcess = new LibrarySyncProcess(this);
        if (syncProcess.isSyncRequired()) {
            syncProcess.sync();
        }
    }

    public Player getPlayer() {
        return player;
    }
}
