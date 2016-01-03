package com.github.iweinzierl.mpd;

import android.content.Intent;
import android.os.Bundle;

import com.github.iweinzierl.mpd.async.ListArtistsTask;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.fragment.ArtistsFragment;

import java.util.List;

public class ArtistsActivity extends BaseActivity implements ArtistsFragment.Callback {

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

        startProgress(getString(R.string.artists_progress_load_artists));

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

    @Override
    public void onArtistClicked(Artist artist) {
        if (artist != null) {
            Intent intent = new Intent(this, AlbumsActivity.class);
            intent.putExtra(AlbumsActivity.EXTRA_ARTIST_NAME, artist.getName());

            startActivity(intent);
        }
    }

    private void setArtists(List<Artist> artists) {
        artistsFragment.setArtists(artists);
    }
}
