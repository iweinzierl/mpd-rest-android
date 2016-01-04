package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

public class PlayerVolumeControlTask extends AsyncTask<Integer, Void, PlayerVolumeControlTask.Status> {

    public enum Status {
        OK,
        NOK
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(PlayerVolumeControlTask.class.getName());

    private final Context context;

    public PlayerVolumeControlTask(Context context) {
        this.context = context;
    }

    @Override
    protected Status doInBackground(Integer... volume) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call call = proxyClient.setVolume(volume[0]);

        try {
            Response response = call.execute();
            if (response.code() == 200) {
                return Status.OK;
            } else {
                return Status.NOK;
            }
        } catch (IOException e) {
            LOG.error("Unable to change player volume to: {}", volume[0], e);
        }

        return Status.NOK;
    }
}
