package com.example.ayush.project1;

import com.example.ayush.project1.AllMovies.Movies;
import com.example.ayush.project1.MovieDetails.MovieDetails;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface APIClient {

    @GET("/discover/movie?sort_by=popularity.desc&api_key=" + BuildConfig.API_KEY)
    void getPopularMovies(Callback<Movies> response);

    @GET("/discover/movie?sort_by=vote_average.desc&api_key=" +BuildConfig.API_KEY)
    void getByRating(Callback<Movies> response);

    @GET("/movie/{id}?api_key="+BuildConfig.API_KEY)
    void getMovieFromId(@Path("id") String id, Callback<MovieDetails> movieCallback);
}
