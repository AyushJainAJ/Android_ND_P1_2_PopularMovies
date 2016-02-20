package com.example.ayush.project1.AllMovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ayush.project1.APIClient;
import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.MovieDetails.Main2Activity;
import com.example.ayush.project1.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends Fragment {

    private Context mContext;
    private GridView mGridView;
    private RestAdapter restadapter;
    private APIClient mMovieAPI;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getContext();
        restadapter = new RestAdapter.Builder().setEndpoint(BuildConfig.BASE_URL).build();
        mMovieAPI = restadapter.create(APIClient.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mProgressBar.setVisibility(View.VISIBLE);

        String sort_order = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.key), "");

        if (sort_order.equals("Popularity"))
            sortByPopularity();
        else
            sortByRating();

    }

    private void sortByPopularity() {

        mMovieAPI.getPopularMovies(new Callback<Movies>() {
            @Override

            public void success(Movies movies, Response response) {

                mProgressBar.setVisibility(View.GONE);
                List<MoviesResults> moviesList = movies.getResults();

                final MovieAdapter adapt = new MovieAdapter(mContext, R.layout.grid_item, moviesList);

                mGridView.setAdapter(adapt);

                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), Main2Activity.class)
                                .putExtra("Selected", adapt.getItem(position).getId()));
                    }
                });
            }

            public void failure(RetrofitError error) {

                mProgressBar.setVisibility(View.GONE);

                final Activity activity = getActivity();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("Please check your internet connectivity");

                alertDialogBuilder.create().show();
            }

        });
    }

    private void sortByRating() {

//        APIClient mMovieAPI = restadapter.create(APIClient.class);


        mMovieAPI.getByRating(new Callback<Movies>() {
            @Override

            public void success(Movies movies, Response response) {

                mProgressBar.setVisibility(View.GONE);
                List<MoviesResults> moviesList = movies.getResults();

                final MovieAdapter adapt = new MovieAdapter(mContext, R.layout.grid_item, moviesList);

                mGridView.setAdapter(adapt);

                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), Main2Activity.class)
                                .putExtra("Selected", adapt.getItem(position).getId()));
                    }
                });
            }

            public void failure(RetrofitError error) {

                mProgressBar.setVisibility(View.GONE);
                final Activity activity = getActivity();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("Please check your internet connectivity");

                alertDialogBuilder.create().show();
            }

        });
    }
}