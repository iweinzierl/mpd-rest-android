package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.Playlist;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

public class SavePlaylistTask extends AsyncTask<Playlist, Void, Playlist> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(SavePlaylistTask.class.getName());

    private final Context context;

    public SavePlaylistTask(Context context) {
        this.context = context;
    }

    @Override
    protected Playlist doInBackground(Playlist... playlists) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call<Playlist> call = proxyClient.savePlaylist(playlists[0]);

        try {
            Response<Playlist> response = call.execute();
            return response.body();
        } catch (IOException e) {
            LOG.error("Unable to save playlist: {}", playlists[0].getName(), e);
        }

        return null;
    }
}
