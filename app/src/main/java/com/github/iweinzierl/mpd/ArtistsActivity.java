package com.github.iweinzierl.mpd;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.iweinzierl.mpd.async.ListArtistsTask;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.domain.Song;
import com.github.iweinzierl.mpd.fragment.ArtistsFragment;
import com.github.iweinzierl.mpd.helper.LibrarySyncProcess;
import com.orm.SugarRecord;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_artists, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                syncLibrary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadArtists();
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

    private void loadArtists() {
        startProgress(getString(R.string.artists_progress_load_artists));

        new ListArtistsTask(this) {
            @Override
            protected void onPostExecute(List<Artist> artists) {
                setArtists(artists);
                stopProgress();
            }
        }.execute();
    }

    private void setArtists(List<Artist> artists) {
        artistsFragment.setArtists(artists);
    }

    private void syncLibrary() {
        startProgress("Synchronize Library ...");

        SugarRecord.deleteAll(Song.class);
        SugarRecord.deleteAll(Album.class);
        SugarRecord.deleteAll(Artist.class);

        new LibrarySyncProcess(this, new LibrarySyncProcess.SyncCallback() {
            @Override
            public void onSyncSucceeded() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgress();
                    }
                });

                loadArtists();
            }

            @Override
            public void onSyncFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgress();
                    }
                });
            }
        }).sync();
    }
}
