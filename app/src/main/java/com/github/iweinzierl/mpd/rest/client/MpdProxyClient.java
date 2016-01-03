package com.github.iweinzierl.mpd.rest.client;

import com.github.iweinzierl.mpd.domain.Artist;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface MpdProxyClient {

    @Headers({"Accept: application/json"})
    @GET("/api/artist")
    Call<List<Artist>> listArtists();
}
