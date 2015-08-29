package br.com.recrutamentoandroid.api;

import java.util.ArrayList;

import br.com.recrutamentoandroid.model.Episode;
import br.com.recrutamentoandroid.model.Rating;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

public interface TraktApi {

    @GET("/shows/{showId}/seasons/{season}")
    void getSeason(@Path("showId") String showId, @Path("season") String season,
           @Header("trakt-api-version") String apiVersion, @Header("trakt-api-key") String apiKey,
                   @Header("Content-Type") String contentType, Callback<ArrayList<Episode>> response);

    @GET("/shows/{showId}/seasons/{season}/ratings")
    void getSeasonRating(@Path("showId") String showId, @Path("season") String season,
                   @Header("trakt-api-version") String apiVersion, @Header("trakt-api-key") String apiKey,
                   @Header("Content-Type") String contentType, Callback<Rating> response);

}
