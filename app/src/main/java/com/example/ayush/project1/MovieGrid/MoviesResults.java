package com.example.ayush.project1.MovieGrid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class MoviesResults {

    @SerializedName("poster_path")
    @Expose
    private String photo;

    @SerializedName("id")
    @Expose
    private String id;

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}