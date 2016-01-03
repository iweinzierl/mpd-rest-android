package com.github.iweinzierl.mpd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.adapter.AlbumsAdapter;
import com.github.iweinzierl.mpd.domain.Album;

import java.util.Collections;
import java.util.List;

public class AlbumsFragment extends Fragment {

    public interface Callback {
        void onAlbumClicked(Album artist);
    }

    private Callback callback;

    private AlbumsAdapter albumsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        albumsAdapter = new AlbumsAdapter(getActivity());

        final ListView albumsList = (ListView) view.findViewById(R.id.list);
        albumsList.setAdapter(albumsAdapter);
        albumsList.setOnItemClickListener(new AlbumClickListener());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    @SuppressWarnings("unchecked")
    public void setAlbums(List<Album> albums) {
        if (albums == null) {
            albumsAdapter.setItems(Collections.EMPTY_LIST);
        } else {
            albumsAdapter.setItems(albums);
        }
    }

    private class AlbumClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (callback != null) {
                Album album = albumsAdapter.getTypedItem(i);
                callback.onAlbumClicked(album);
            }
        }
    }
}
