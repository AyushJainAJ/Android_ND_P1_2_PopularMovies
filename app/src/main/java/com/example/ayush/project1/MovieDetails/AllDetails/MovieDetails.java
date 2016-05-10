package com.example.ayush.project1.MovieDetails.AllDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetails {

    @SerializedName("original_title")
    @Expose
    private String original_title;

    @SerializedName("poster_path")
    @Expose
    private String poster_path; //Image thumbnail

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("vote_average")
    @Expose
    private String vote_average;

    @SerializedName("release_date")
    @Expose
    private String release_date;

    @SerializedName("backdrop_path")
    @Expose
    private String backdrop_path;

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String title) {
        this.original_title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(String vote) {
        this.vote_average = vote;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }
}