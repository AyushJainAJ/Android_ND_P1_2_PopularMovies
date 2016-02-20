package com.example.ayush.project1.AllMovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends ArrayAdapter<MoviesResults> {


        private final Context context;

        private final List<MoviesResults> moviesList;


        public MovieAdapter(Context context, int resource, List<MoviesResults> objects) {
            super(context, resource, objects);
            this.context = context;
            this.moviesList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.grid_item, parent, false);

            Picasso.with(context)
                    .load(BuildConfig.IMAGE_URL + moviesList.get(position).getPhoto())
                    .into((ImageView) view.findViewById(R.id.posterImg));

            return view;
        }
    }