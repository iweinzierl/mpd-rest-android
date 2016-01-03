package com.github.iweinzierl.mpd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.adapter.StringListAdapter;
import com.github.iweinzierl.mpd.domain.Artist;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistsFragment extends Fragment {

    private StringListAdapter artistsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        artistsAdapter = new StringListAdapter(getActivity(), Collections.EMPTY_LIST);

        ListView artistsList = (ListView) view.findViewById(R.id.list);
        artistsList.setAdapter(artistsAdapter);

        return view;
    }

    public void setArtists(List<Artist> artists) {
        if (artists == null) {
            artistsAdapter.setItems(Collections.EMPTY_LIST);
        } else {
            artistsAdapter.setItems(new ArrayList<>(Collections2.transform(artists, new Function<Artist, String>() {
                @Override
                public String apply(Artist artist) {
                    return artist.getName();
                }
            })));
        }
    }
}
