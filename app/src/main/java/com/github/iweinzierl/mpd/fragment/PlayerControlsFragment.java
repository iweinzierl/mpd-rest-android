package com.github.iweinzierl.mpd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.domain.PlayerStatus;

import org.slf4j.Logger;

public class PlayerControlsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    public interface Callback {
        void onPreviousSong();

        void onPausePlayer();

        void onStartPlayer();

        void onNextSong();

        void onChangeVolume(int volume);
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(PlayerControlsFragment.class.getName());

    private Callback callback;

    private View pause;
    private View play;
    private AppCompatSeekBar volumeBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);

        pause = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play);
        volumeBar = (AppCompatSeekBar) view.findViewById(R.id.volume_bar);
        View previous = view.findViewById(R.id.previous);
        View next = view.findViewById(R.id.next);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onPreviousSong();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onPausePlayer();
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onStartPlayer();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onNextSong();
                }
            }
        });

        volumeBar.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
        if (fromUser) {
            LOG.debug("User changed volume to: {}", value);
            callback.onChangeVolume(value);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        applyPlayPauseButton(playerInfo);
        applyVolume(playerInfo);
    }

    private void applyPlayPauseButton(PlayerInfo playerInfo) {
        if (playerInfo == null || playerInfo.getStatus() != PlayerStatus.PLAYING) {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        } else {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
    }

    private void applyVolume(PlayerInfo playerInfo) {
        if (playerInfo != null) {
            volumeBar.setProgress(playerInfo.getVolume());
        }
    }
}
