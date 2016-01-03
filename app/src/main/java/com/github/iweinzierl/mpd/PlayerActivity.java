package com.github.iweinzierl.mpd;

import android.os.Bundle;

import com.github.iweinzierl.mpd.async.GetPlayerInfoTask;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.domain.PlayerStatus;
import com.github.iweinzierl.mpd.fragment.PlayerControlsFragment;
import com.github.iweinzierl.mpd.player.Player;

public class PlayerActivity extends BaseActivity implements PlayerControlsFragment.Callback, Player.PlayerListener {

    private PlayerControlsFragment playerControlsFragment;

    private PlayerInfo playerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerControlsFragment = new PlayerControlsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.player_controls_fragment, playerControlsFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getPlayer().addPlayerListener(this);

        new GetPlayerInfoTask(this) {
            @Override
            protected void onPostExecute(PlayerInfo playerInfo) {
                super.onPostExecute(playerInfo);
                setPlayerInfo(playerInfo);
            }
        }.execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void onPreviousSong() {
        getPlayer().previous();
    }

    @Override
    public void onPausePlayer() {
        getPlayer().pause();
    }

    @Override
    public void onStartPlayer() {
        getPlayer().play();
    }

    @Override
    public void onNextSong() {
        getPlayer().next();
    }

    private void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        playerControlsFragment.setPlayerInfo(playerInfo);
    }

    @Override
    public void onPlayerPaused() {
        playerInfo.setStatus(PlayerStatus.PAUSED);
        playerControlsFragment.setPlayerInfo(playerInfo);
    }

    @Override
    public void onPlayerStarted() {
        playerInfo.setStatus(PlayerStatus.PLAYING);
        playerControlsFragment.setPlayerInfo(playerInfo);
    }

    @Override
    public void onPlayerStopped() {
        playerInfo.setStatus(PlayerStatus.STOPPED);
        playerControlsFragment.setPlayerInfo(playerInfo);
    }

    @Override
    public void onJumpedToPreviousSong() {
    }

    @Override
    public void onJumpedToNextSong() {
    }
}
