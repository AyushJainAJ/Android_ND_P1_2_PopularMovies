package com.example.ayush.project1.MovieDetails.Videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideosResults {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("key")
    @Expose
    private String key;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
