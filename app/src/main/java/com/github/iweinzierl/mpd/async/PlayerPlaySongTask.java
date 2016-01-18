package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.Song;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

public class PlayerPlaySongTask extends AsyncTask<Song, Void, PlayerPlaySongTask.Status> {

    public enum Status {
        OK,
        NOK
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(PlayerPlaySongTask.class.getName());

    private final Context context;

    public PlayerPlaySongTask(Context context) {
        this.context = context;
    }

    @Override
    protected Status doInBackground(Song... song) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call call = proxyClient.play(song[0]);

        try {
            Response response = call.execute();
            if (response.code() == 200) {
                return Status.OK;
            } else {
                return Status.NOK;
            }
        } catch (IOException e) {
            LOG.error("Unable to play song: {}", song[0], e);
        }

        return Status.NOK;
    }
}
