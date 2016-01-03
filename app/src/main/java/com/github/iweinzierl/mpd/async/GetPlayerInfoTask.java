package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

public class GetPlayerInfoTask extends AsyncTask<Void, Void, PlayerInfo> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(GetPlayerInfoTask.class.getName());

    private final Context context;

    public GetPlayerInfoTask(Context context) {
        this.context = context;
    }

    @Override
    protected PlayerInfo doInBackground(Void... params) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call<PlayerInfo> call = proxyClient.getPlayerInfo();

        try {
            Response<PlayerInfo> response = call.execute();
            return response.body();
        } catch (IOException e) {
            LOG.error("Unable to load player info", e);
        }

        return null;
    }
}
