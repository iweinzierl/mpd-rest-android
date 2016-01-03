package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.helper.ArtistComparator;

import java.util.Collections;


public class ArtistsAdapter extends BaseListAdapter<Artist> {

    @SuppressWarnings("unchecked")
    public ArtistsAdapter(Context context) {
        super(context, Collections.EMPTY_LIST, new ArtistComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Artist artist = getTypedItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_artist, viewGroup, false);

        TextView valueField = (TextView) view.findViewById(R.id.value);
        valueField.setText(artist.getName());

        return view;
    }
}
