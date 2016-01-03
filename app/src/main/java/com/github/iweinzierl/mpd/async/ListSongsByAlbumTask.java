package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.Song;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

public class ListSongsByAlbumTask extends AsyncTask<String, Void, List<Song>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(ListSongsByAlbumTask.class.getName());

    private final Context context;

    public ListSongsByAlbumTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Song> doInBackground(String... albums) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call<List<Song>> call = proxyClient.listSongs(albums[0]);

        try {
            Response<List<Song>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            LOG.error("Unable to fetch songs by album: {}", albums[0], e);
        }

        return null;
    }
}
