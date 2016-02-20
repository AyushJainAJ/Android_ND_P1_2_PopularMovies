package com.example.ayush.project1.MovieDetails;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ayush.project1.R;

class DetailsViewHolder {
    public final ImageView poster;
    public final ImageView thumbnail;

    public final TextView overview;
    public final TextView releaseDate;
    public final TextView userRating;
    public final RatingBar ratingBar;

    public DetailsViewHolder(View v)
    {
        thumbnail = (ImageView)v.findViewById(R.id.imgThumbnail);
        poster = (ImageView)v.findViewById(R.id.imgPoster);
        ratingBar = (RatingBar)v.findViewById(R.id.rating_bar);
        overview = (TextView) v.findViewById(R.id.overview);
        releaseDate = (TextView) v.findViewById(R.id.release_date);
        userRating=(TextView) v.findViewById(R.id.user_rating);
    }
}
