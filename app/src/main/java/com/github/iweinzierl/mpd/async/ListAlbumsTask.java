package com.github.iweinzierl.mpd.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.repository.Repository;
import com.github.iweinzierl.mpd.rest.ClientFactory;
import com.github.iweinzierl.mpd.rest.client.MpdProxyClient;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

public class ListAlbumsTask extends AsyncTask<Void, Void, List<Album>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance("[rest]").getLogger(ListAlbumsTask.class.getName());

    private final Context context;

    public ListAlbumsTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Album> doInBackground(Void... params) {
        Repository repository = new Repository(context);

        if (repository.isCacheEnabled()) {
            return repository.listAlbums();
        } else {
            MpdProxyClient proxyClient = ClientFactory.createMpdProxyClient(context);
            Call<List<Album>> call = proxyClient.listAlbums();

            try {
                Response<List<Album>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                LOG.error("Unable to fetch albums", e);
            }

            return null;
        }
    }
}
