package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

public class ListArtistsTask extends AsyncTask<Void, Void, List<Artist>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(ListArtistsTask.class.getName());

    private final Context context;

    public ListArtistsTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Artist> doInBackground(Void... voids) {
        MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
        Call<List<Artist>> call = proxyClient.listArtists();

        try {
            Response<List<Artist>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            LOG.error("Unable to fetch artists", e);
        }

        return null;
    }
}
