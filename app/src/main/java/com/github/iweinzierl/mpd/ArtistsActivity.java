package com.github.iweinzierl.mpd;

import android.os.Bundle;

import com.github.iweinzierl.mpd.async.ListArtistsTask;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.fragment.ArtistsFragment;

import java.util.List;

public class ArtistsActivity extends BaseActivity {

    private ArtistsFragment artistsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artistsFragment = new ArtistsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, artistsFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startProgress(getString(R.string.artists_load_artists));

        new ListArtistsTask(this) {
            @Override
            protected void onPostExecute(List<Artist> artists) {
                setArtists(artists);
                stopProgress();
            }
        }.execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    private void setArtists(List<Artist> artists) {
        artistsFragment.setArtists(artists);
    }
}
