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

        updatePlayerInfo();
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

    @Override
    public void onChangeVolume(int volume) {
        getPlayer().setVolume(volume);
    }

    private void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        playerControlsFragment.setPlayerInfo(playerInfo);
    }

    @Override
    public void onPlayerPaused() {
        if (playerInfo == null) {
            updatePlayerInfo();
        } else {
            playerInfo.setStatus(PlayerStatus.PAUSED);
            playerControlsFragment.setPlayerInfo(playerInfo);
        }
    }

    @Override
    public void onPlayerStarted() {
        if (playerInfo == null) {
            updatePlayerInfo();
        } else {
            playerInfo.setStatus(PlayerStatus.PLAYING);
            playerControlsFragment.setPlayerInfo(playerInfo);
        }
    }

    @Override
    public void onPlayerStopped() {
        if (playerInfo == null) {
            updatePlayerInfo();
        } else {
            playerInfo.setStatus(PlayerStatus.STOPPED);
            playerControlsFragment.setPlayerInfo(playerInfo);
        }
    }

    @Override
    public void onJumpedToPreviousSong() {
        if (playerInfo == null) {
            updatePlayerInfo();
        }
    }

    @Override
    public void onJumpedToNextSong() {
        if (playerInfo == null) {
            updatePlayerInfo();
        }
    }

    @Override
    public void onVolumeChanged(int volume) {
    }

    private void updatePlayerInfo() {
        startProgress(getString(R.string.player_progress_update_info));

        new GetPlayerInfoTask(this) {
            @Override
            protected void onPostExecute(PlayerInfo playerInfo) {
                super.onPostExecute(playerInfo);
                stopProgress();
                setPlayerInfo(playerInfo);
            }
        }.execute();
    }
}
