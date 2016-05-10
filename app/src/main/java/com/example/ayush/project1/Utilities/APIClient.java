package com.example.ayush.project1.Utilities;

import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.MovieGrid.Movies;
import com.example.ayush.project1.MovieDetails.AllDetails.MovieDetails;
import com.example.ayush.project1.MovieDetails.Reviews.MovieReviews;
import com.example.ayush.project1.MovieDetails.Videos.MovieVideos;

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

    //Example: https://api.themoviedb.org/3/movie/550/videos?api_key=39c3e3ffb0e8f8fe315acef1e9f9dee1
    @GET("/movie/{id}/videos?api_key="+BuildConfig.API_KEY)
    void getMovieTrailer(@Path("id") String id, Callback<MovieVideos> movieVideosCallback);

    @GET("/movie/{id}/reviews?api_key=" + BuildConfig.API_KEY)
    void getMovieReviewsFromId(@Path("id") String id, Callback<MovieReviews> movieReviewsCallback);
}
