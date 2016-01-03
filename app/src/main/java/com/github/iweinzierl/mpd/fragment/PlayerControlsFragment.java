package com.github.iweinzierl.mpd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.domain.PlayerStatus;

public class PlayerControlsFragment extends Fragment {

    public interface Callback {
        void onPreviousSong();

        void onPausePlayer();

        void onStartPlayer();

        void onNextSong();
    }

    private Callback callback;

    private View pause;
    private View play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);

        pause = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play);
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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        if (playerInfo.getStatus() == PlayerStatus.PLAYING) {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
        else {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        }
    }
}
