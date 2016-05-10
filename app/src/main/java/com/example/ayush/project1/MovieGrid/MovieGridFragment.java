package com.example.ayush.project1.MovieGrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ayush.project1.MovieDetails.AllDetails.MovieDetailsFragment;
import com.example.ayush.project1.Utilities.APIClient;
import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.MovieDetails.AllDetails.MovieDetailsActivity;
import com.example.ayush.project1.Utilities.MoviesContract;
import com.example.ayush.project1.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends Fragment {

    private Context mContext;
    private GridView mGridView;
    private RestAdapter mRestAdapter;
    private APIClient mMovieAPI;
    private ProgressBar mProgressBar;
    private View view;
    private String sortOrder;

    private boolean isTablet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getContext();
        mRestAdapter = new RestAdapter.Builder().setEndpoint(BuildConfig.BASE_URL).build();
        mMovieAPI = mRestAdapter.create(APIClient.class);
        isTablet = getResources().getBoolean(R.bool.is_tablet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_grid, container, false);

        mGridView = (GridView) view.findViewById(R.id.grid);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mProgressBar.setVisibility(View.VISIBLE);

        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, false);

        sortOrder = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.key), "");

        if (sortOrder.equals("Popularity")) {
            sortByPopularity();
        } else if (sortOrder.equals("Rating")) {
            sortByRating();
        } else {
            sortByFavorites();
        }
    }

    private void sortByPopularity() {

        mMovieAPI.getPopularMovies(new Callback<Movies>() {
            @Override

            public void success(Movies movies, Response response) {

                mProgressBar.setVisibility(View.GONE);
                List<MoviesResults> moviesList = movies.getResults();

                setItemClickAction(moviesList);
            }

            public void failure(RetrofitError error) {
                displayInternetConnectionFailure();
            }
        });
    }

    private void sortByRating() {

        mMovieAPI.getByRating(new Callback<Movies>() {
            @Override

            public void success(Movies movies, Response response) {

                mProgressBar.setVisibility(View.GONE);

                List<MoviesResults> moviesList = movies.getResults();

                setItemClickAction(moviesList);
            }

            public void failure(RetrofitError error) {
                displayInternetConnectionFailure();
            }
        });
    }

    private void sortByFavorites() {
        mProgressBar.setVisibility(View.GONE);

        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.BASE_CONTENT_URI, null, null, null, null);

        List<MoviesResults> moviesList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                MoviesResults moviesResults = new MoviesResults();
                moviesResults.setId(cursor.getString(1));
                moviesList.add(moviesResults);
            }

            cursor.close();

            setItemClickAction(moviesList);
        } else {
            Snackbar.make(view, "No favourite movies added!", Snackbar.LENGTH_LONG).show();
        }
    }

    private void setItemClickAction(List<MoviesResults> moviesList) {

        final boolean isPreferenceFavourites = sortOrder.equals("Favorite");

        final MovieAdapter adapt = new MovieAdapter(mContext, R.layout.grid_item, moviesList, isPreferenceFavourites);

        mGridView.setAdapter(adapt);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isTablet) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.details_container, MovieDetailsFragment
                                    .newInstance(adapt.getItem(position).getId(), isPreferenceFavourites))
                            .commit();

                } else {
                    startActivity(new Intent(getActivity(), MovieDetailsActivity.class)
                            .putExtra("Selected", adapt.getItem(position).getId())
                            .putExtra("Preference", isPreferenceFavourites));
                }
            }
        });
    }

    private void displayInternetConnectionFailure() {
        mProgressBar.setVisibility(View.GONE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Please check your internet connectivity");

        alertDialogBuilder.create().show();
    }
}