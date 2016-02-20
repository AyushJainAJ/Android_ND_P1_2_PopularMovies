package com.example.ayush.project1.MovieDetails;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ayush.project1.APIClient;
import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.R;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Main2Fragment extends Fragment {

    private String id;
    private Context mContext;
    public ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString("id");
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.detailsProgressBar);

        final Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)rootView
                .findViewById(R.id.toolbar_layout);

        final DetailsViewHolder viewHolder = new DetailsViewHolder(rootView);

        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(BuildConfig.BASE_URL)
                .build();

        APIClient movieapi = restadapter.create(APIClient.class);

        mProgressBar.setVisibility(View.VISIBLE);

        movieapi.getMovieFromId(id, new Callback<MovieDetails>() {
            public void success(MovieDetails movie, Response response) {

                mProgressBar.setVisibility(View.GONE);

                collapsingToolbar.setTitle(movie.getOriginal_title());

                Picasso.with(mContext)
                        .load(BuildConfig.IMAGE_URL + movie.getPoster_path())
                        .into(viewHolder.thumbnail);

                Picasso.with(mContext)
                        .load(BuildConfig.IMAGE_URL + movie.getBackdrop_path()).into
                        (viewHolder.poster);



                (viewHolder.overview).setText(getString(R.string.overview, movie
                        .getOverview()));

                (viewHolder.releaseDate).setText(getString(R.string.release_date, movie
                        .getRelease_date()));



                String rating = movie.getVote_average();
                viewHolder.ratingBar.setVisibility(View.VISIBLE);

                (viewHolder.ratingBar).setRating((Float.parseFloat(rating)) / 2.0f);

                (viewHolder.userRating).setText(getString(R.string.user_rating, rating));
            }

            public void failure(RetrofitError error) {

                mProgressBar.setVisibility(View.GONE);

                final Activity activity = getActivity();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("Please check your internet connectivity");
            }
        });

        return rootView;
    }
}