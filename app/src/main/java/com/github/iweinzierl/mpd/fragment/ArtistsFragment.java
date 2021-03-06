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
import com.github.iweinzierl.mpd.adapter.ArtistsAdapter;
import com.github.iweinzierl.mpd.domain.Artist;

import java.util.Collections;
import java.util.List;

public class ArtistsFragment extends Fragment {

    public interface Callback {
        void onArtistClicked(Artist artist);
    }

    private Callback callback;

    private ArtistsAdapter artistsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        artistsAdapter = new ArtistsAdapter(getActivity());

        final ListView artistsList = (ListView) view.findViewById(R.id.list);
        artistsList.setAdapter(artistsAdapter);
        artistsList.setOnItemClickListener(new ArticleClickListener());

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
    public void setArtists(List<Artist> artists) {
        if (artists == null) {
            artistsAdapter.setItems(Collections.EMPTY_LIST);
        } else {
            artistsAdapter.setItems(artists);
        }
    }

    private class ArticleClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (callback != null) {
                Artist artist = artistsAdapter.getTypedItem(i);
                callback.onArtistClicked(artist);
            }
        }
    }
}
