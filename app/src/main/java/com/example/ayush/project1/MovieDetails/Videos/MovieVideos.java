package com.example.ayush.project1.MovieDetails.Videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieVideos {

    @SerializedName("results")
    @Expose
    private List<MovieVideosResults> results = new ArrayList<>();

    public List<MovieVideosResults> getResults() {
        return results;
    }
}
