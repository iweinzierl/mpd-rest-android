package com.github.iweinzierl.mpd.rest.client;

import com.github.iweinzierl.mpd.domain.Album;
import com.github.iweinzierl.mpd.domain.Artist;
import com.github.iweinzierl.mpd.domain.PlayerInfo;
import com.github.iweinzierl.mpd.domain.Playlist;
import com.github.iweinzierl.mpd.domain.Song;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MpdProxyClient {

    @GET("/api/player")
    Call<PlayerInfo> getPlayerInfo();

    @GET("/api/player/play")
    Call<Void> play();

    @POST("/api/player/play")
    Call<Void> play(@Body Song song);

    @GET("/api/player/pause")
    Call<Void> pause();

    @GET("/api/player/next")
    Call<Void> playNext();

    @GET("/api/player/previous")
    Call<Void> playPrevious();

    @GET("/api/player/stop")
    Call<Void> stop();

    @GET("/api/player/volume")
    Call<Void> setVolume(@Query("volume") int volume);

    @Headers({"Accept: application/json"})
    @GET("/api/artist")
    Call<List<Artist>> listArtists();

    @Headers({"Accept: application/json"})
    @GET("/api/album")
    Call<List<Album>> listAlbums();

    @Headers({"Accept: application/json"})
    @GET("/api/album")
    Call<List<Album>> listAlbums(@Query("artist") String artist);

    @Headers({"Accept: application/json"})
    @GET("/api/album/{album}")
    Call<List<Song>> listSongs(@Path("album") String album);

    @Headers({"Accept: application/json"})
    @GET("/api/song")
    Call<List<Song>> listSongs();

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("/api/playlist")
    Call<Playlist> savePlaylist(@Body Playlist playlist);
}
