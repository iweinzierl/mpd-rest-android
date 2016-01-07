package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.Song;

import java.util.Collections;


public class SongsAdapter extends BaseListAdapter<Song> {

    @SuppressWarnings("unchecked")
    public SongsAdapter(Context context) {
        super(context, Collections.EMPTY_LIST);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Song song = getTypedItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_song, viewGroup, false);

        TextView timeView = (TextView) view.findViewById(R.id.duration);
        TextView songView = (TextView) view.findViewById(R.id.song);
        TextView artistView = (TextView) view.findViewById(R.id.artist);
        TextView albumView = (TextView) view.findViewById(R.id.album);

        timeView.setText(formatTime(song.getLength()));
        songView.setText(song.getTitle());
        artistView.setText(song.getArtist().getName());
        albumView.setText(song.getAlbum().getName());

        return view;
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format("%02d : %02d : %02d", hours, minutes, seconds);
        } else {
            return String.format("%02d : %02d", minutes, seconds);
        }
    }
}
