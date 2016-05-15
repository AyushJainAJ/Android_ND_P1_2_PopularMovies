package com.example.ayush.project1.MovieDetails.Reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieReviewsResults {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
