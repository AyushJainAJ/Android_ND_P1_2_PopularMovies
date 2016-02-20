package com.example.ayush.project1.MovieDetails;

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

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }
}