package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.helper.AlbumComparator;

import java.util.Collections;


public class AlbumsAdapter extends BaseListAdapter<Album> {

    @SuppressWarnings("unchecked")
    public AlbumsAdapter(Context context) {
        super(context, Collections.EMPTY_LIST, new AlbumComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Album album = getTypedItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_album, viewGroup, false);

        // TODO set cover
        TextView artistView = (TextView) view.findViewById(R.id.artist);
        TextView albumView = (TextView) view.findViewById(R.id.album);

        artistView.setText(album.getArtist().getName());
        albumView.setText(album.getName());

        return view;
    }
}
