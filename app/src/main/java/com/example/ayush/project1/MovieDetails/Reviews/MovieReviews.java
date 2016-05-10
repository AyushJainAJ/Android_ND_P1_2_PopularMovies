package com.example.ayush.project1.MovieDetails.Reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieReviews {

    @SerializedName("results")
    @Expose
    private List<MovieReviewsResults> results = new ArrayList<>();

    public List<MovieReviewsResults> getResults() {
        return results;
    }

}
