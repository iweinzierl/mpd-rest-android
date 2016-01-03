package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.Album;

import java.util.Collections;


public class AlbumsAdapter extends BaseListAdapter<Album> {

    @SuppressWarnings("unchecked")
    public AlbumsAdapter(Context context) {
        super(context, Collections.EMPTY_LIST);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Album album = getTypedItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_text, viewGroup, false);

        TextView valueField = (TextView) view.findViewById(R.id.value);
        valueField.setText(album.getName());

        return view;
    }
}
