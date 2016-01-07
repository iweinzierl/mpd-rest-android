package com.github.iweinzierl.mpd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.adapter.SongsAdapter;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.domain.PlayerStatus;

import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerControlsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    public interface Callback {
        void onPreviousSong();

        void onPausePlayer();

        void onStartPlayer();

        void onNextSong();

        void onChangeVolume(int volume);
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(PlayerControlsFragment.class.getName());

    private Timer updateTimesTimer;

    private Callback callback;

    private SongsAdapter songAdapter;

    private long elapsedTime = -1;

    private View pause;
    private View play;

    private TextView elapsedTimeView;
    private TextView totalTimeView;
    private TextView timeSeparatorView;
    private TextView song;
    private TextView artist;
    private TextView album;

    private AppCompatSeekBar volumeBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);

        songAdapter = new SongsAdapter(getActivity());

        pause = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play);
        elapsedTimeView = (TextView) view.findViewById(R.id.elapsed_time);
        totalTimeView = (TextView) view.findViewById(R.id.total_time);
        timeSeparatorView = (TextView) view.findViewById(R.id.time_separator);
        song = (TextView) view.findViewById(R.id.song);
        artist = (TextView) view.findViewById(R.id.artist);
        album = (TextView) view.findViewById(R.id.album);
        volumeBar = (AppCompatSeekBar) view.findViewById(R.id.volume_bar);
        View previous = view.findViewById(R.id.previous);
        View next = view.findViewById(R.id.next);
        ListView playlist = (ListView) view.findViewById(R.id.playlist);
        playlist.setAdapter(songAdapter);

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
                pauseClicked();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playClicked();
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
        applySongInformation(playerInfo);
        applyTimes(playerInfo);
        applyPlaylist(playerInfo);
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

    private void applySongInformation(PlayerInfo playerInfo) {
        if (playerInfo != null && playerInfo.getCurrentSong() != null) {
            song.setText(playerInfo.getCurrentSong().getTitle());
            artist.setText(playerInfo.getCurrentSong().getArtist().getName());
            album.setText(playerInfo.getCurrentSong().getAlbum().getName());
        }
    }

    private void applyTimes(PlayerInfo playerInfo) {
        if (playerInfo != null && playerInfo.getCurrentSong() != null) {
            elapsedTimeView.setText(formatTime(playerInfo.getElapsedTime()));
            totalTimeView.setText(formatTime(playerInfo.getTotalTime()));
            timeSeparatorView.setVisibility(View.VISIBLE);

            elapsedTime = playerInfo.getElapsedTime();

            if (playerInfo.getStatus() == PlayerStatus.PLAYING) {
                startRuntimeClock();
            }
        }
    }

    private void applyPlaylist(PlayerInfo playerInfo) {
        if (playerInfo != null) {
            songAdapter.setItems(playerInfo.getPlaylist());
        }
    }

    private static String formatTime(long seconds) {
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format("%02d : %02d : %02d", hours, minutes, seconds);
        } else {
            return String.format("%02d : %02d", minutes, seconds);
        }
    }

    private void pauseClicked() {
        if (callback != null) {
            callback.onPausePlayer();
        }

        stopRuntimeClock();
    }

    private void playClicked() {
        if (callback != null) {
            callback.onStartPlayer();
        }

        startRuntimeClock();
    }

    private void startRuntimeClock() {
        updateTimesTimer = new Timer();
        updateTimesTimer.schedule(
                new UpdateElapsedTimeTask(
                        new UpdateElapsedTimeHandler(elapsedTimeView),
                        elapsedTime
                ),
                1000,
                1000
        );
    }

    private void stopRuntimeClock() {
        updateTimesTimer.cancel();
    }

    private class UpdateElapsedTimeHandler extends Handler {
        private final TextView textView;

        private UpdateElapsedTimeHandler(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void handleMessage(Message msg) {
            elapsedTime = msg.getData().getLong("elapsed.time");
            textView.setText(formatTime(elapsedTime));
        }
    }

    private class UpdateElapsedTimeTask extends TimerTask {
        private final Handler handler;
        private long elapsedTime;

        private UpdateElapsedTimeTask(Handler handler, long elapsedTime) {
            this.handler = handler;
            this.elapsedTime = elapsedTime;
        }

        @Override
        public void run() {
            elapsedTime += 1;

            Message message = new Message();
            message.getData().putLong("elapsed.time", elapsedTime);

            handler.sendMessage(message);
        }
    }
}
