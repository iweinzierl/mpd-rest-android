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

public class PlayerControlTask extends AsyncTask<PlayerControlTask.Command, Void, PlayerControlTask.Status> {

    public enum Command {
        PLAY,
        NEXT,
        PREVIOUS,
        STOP
    }

    public enum Status {
        OK,
        NOK
    }

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(PlayerControlTask.class.getName());

    private final Context context;

    public PlayerControlTask(Context context) {
        this.context = context;
    }

    @Override
    protected Status doInBackground(Command... command) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call call;

        switch (command[0]) {
            case PLAY:
                call = proxyClient.play();
                break;
            case NEXT:
                call = proxyClient.playNext();
                break;
            case PREVIOUS:
                call = proxyClient.playPrevious();
                break;
            case STOP:
                call = proxyClient.stop();
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + command[0].name());
        }

        try {
            Response response = call.execute();
            if (response.code() == 200) {
                return Status.OK;
            } else {
                return Status.NOK;
            }
        } catch (IOException e) {
            LOG.error("Unable to change player status to: {}", command[0], e);
        }

        return Status.NOK;
    }
}
