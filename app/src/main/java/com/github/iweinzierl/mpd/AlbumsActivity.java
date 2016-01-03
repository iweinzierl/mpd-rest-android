package com.github.iweinzierl.mpd;

import android.content.Intent;
import android.os.Bundle;

import com.github.iweinzierl.mpd.async.ListAlbumsByArtistTask;
import com.github.iweinzierl.mpd.async.ListAlbumsTask;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.fragment.AlbumsFragment;
import com.google.common.base.Strings;

import java.util.List;

public class AlbumsActivity extends BaseActivity {

    public static final String EXTRA_ARTIST_NAME = "com.github.iweinzierl.mpd.AlbumsActivity.EXTRA_ARTIST_NAME";

    private AlbumsFragment albumsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        albumsFragment = new AlbumsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, albumsFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startProgress(getString(R.string.albums_progress_load_albums));

        final String artistName = getArtistNameFromIntent();
        if (Strings.isNullOrEmpty(artistName)) {
            new ListAlbumsTask(this) {
                @Override
                protected void onPostExecute(List<Album> albums) {
                    super.onPostExecute(albums);
                    setAlbums(albums);
                    stopProgress();
                }
            }.execute();
        } else {
            new ListAlbumsByArtistTask(this) {
                @Override
                protected void onPostExecute(List<Album> albums) {
                    super.onPostExecute(albums);
                    setAlbums(albums);
                    stopProgress();
                }
            }.execute(artistName);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    private String getArtistNameFromIntent() {
        Intent data = getIntent();
        return data.getStringExtra(EXTRA_ARTIST_NAME);
    }

    private void setAlbums(List<Album> albums) {
        albumsFragment.setAlbums(albums);
    }
}
