package com.example.ayush.project1.MovieDetails.AllDetails;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayush.project1.Utilities.APIClient;
import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.MovieDetails.Reviews.MovieReviewAdapter;
import com.example.ayush.project1.MovieDetails.Reviews.MovieReviews;
import com.example.ayush.project1.MovieDetails.Reviews.MovieReviewsResults;
import com.example.ayush.project1.MovieDetails.Videos.MovieVideos;
import com.example.ayush.project1.MovieDetails.Videos.MovieVideosAdapter;
import com.example.ayush.project1.Utilities.MoviesContract;
import com.example.ayush.project1.R;
import com.example.ayush.project1.Utilities.SettingsActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.EXTRA_TEXT;

public class MovieDetailsFragment extends Fragment {

    //Movie Details
    private String mId;
    private String mMovieTitle;
    private String mOverview;
    private String mRating;
    private String mRelease;
    private String mVideoURL;
    private boolean inDB;

    //Activity Details
    private Context mContext;
    private AppCompatActivity mActivity;
    private ContentResolver mContentResolver;
    private View rootView;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    //UI Globals
    @Bind(R.id.detailsProgressBar)
    public ProgressBar mProgressBar;

    private RestAdapter mRestAdapter;
    private APIClient mMovieAPI;
    private boolean isPreferenceFavourites;
    private DetailsViewHolder mViewHolder;

    @Bind(R.id.toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbar;

    public static MovieDetailsFragment newInstance(String id, boolean local) {
        MovieDetailsFragment obj = new MovieDetailsFragment();
        obj.setID(id);
        obj.setLocal(local);
        return obj;
    }

    public void setID(String id) {
        this.mId = id;
    }

    public void setLocal(boolean local) {
        this.isPreferenceFavourites = local;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);    //To retain values of variables when screen orientation changes

        if (!getResources().getBoolean(R.bool.is_tablet)) {
            this.mId = getArguments().getString("id");
            this.isPreferenceFavourites = getArguments().getBoolean("preference");
        }

        this.inDB = false;
        mContentResolver = getActivity().getContentResolver();

        mContext = getActivity().getApplicationContext();
        mRestAdapter = new RestAdapter.Builder().setEndpoint(BuildConfig.BASE_URL).build();
        mMovieAPI = mRestAdapter.create(APIClient.class);
        mActivity = ((AppCompatActivity) getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, rootView);

        mViewHolder = new DetailsViewHolder(rootView);

        return rootView;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!getResources().getBoolean(R.bool.is_tablet)) {

            mActivity.setSupportActionBar(toolbar);

            if (mActivity.getSupportActionBar() != null)
                mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        if (!isPreferenceFavourites) {
            menuInflater.inflate(R.menu.menu_details, menu);
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                onShareOptionClicked();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(mContext, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkIifMovieIsInDatabase();

        if (isPreferenceFavourites) {
            loadFromCache();
        } else {
            loadFromAPI();
        }
    }

    private void loadFromCache() {
        mViewHolder.video.setVisibility(View.GONE);
        mViewHolder.review.setVisibility(View.GONE);

        Cursor cursor = mActivity.getContentResolver().query(MoviesContract.BASE_CONTENT_URI,
                new String[]{MoviesContract.MoviesEntry.COLUMN_ID, MoviesContract.MoviesEntry.COLUMN_TITLE,
                        MoviesContract.MoviesEntry.COLUMN_OVERVIEW, MoviesContract.MoviesEntry.COLUMN_RATING,
                        MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE}, MoviesContract.MoviesEntry.COLUMN_ID
                        + "=?", new String[]{mId}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mProgressBar.setVisibility(View.GONE);
            mMovieTitle = cursor.getString(1);
            mOverview = cursor.getString(2);
            mRating = cursor.getString(3);
            mRelease = cursor.getString(4);

            cursor.close();

            collapsingToolbar.setTitle(mMovieTitle);

            Picasso.with(getContext()).load(readImageFromFile(mId, "poster"))
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(mViewHolder.poster);

            Picasso.with(getContext()).load(readImageFromFile(mId, "thumbnail"))
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(mViewHolder.thumbnail);

            (mViewHolder.overview).setText(getString(R.string.overview, mOverview));

            mViewHolder.ratingBar.setVisibility(View.VISIBLE);

            (mViewHolder.ratingBar).setRating((Float.parseFloat(mRating)) / 2.0f);

            (mViewHolder.releaseDate).setText(getString(R.string.release_date, mRelease));

            (mViewHolder.userRating).setText(getString(R.string.user_rating, mRating));
        }
    }

    private void loadFromAPI() {
        mMovieAPI.getMovieFromId(mId, new Callback<MovieDetails>() {
            public void success(final MovieDetails movie, Response response) {

                mProgressBar.setVisibility(View.GONE);
                mMovieTitle = movie.getOriginalTitle();
                mOverview = movie.getOverview();
                mRating = movie.getVoteAverage();
                mRelease = movie.getRelease_date();

                collapsingToolbar.setTitle(mMovieTitle);

                Picasso.with(mContext)
                        .load(BuildConfig.IMAGE_URL + movie.getPosterPath())
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                        .into(mViewHolder.thumbnail);

                Picasso.with(mContext)
                        .load(BuildConfig.IMAGE_URL + movie.getBackdropPath())
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                        .into(mViewHolder.poster);

                (mViewHolder.overview).setText(getString(R.string.overview, mOverview));

                (mViewHolder.releaseDate).setText(getString(R.string.release_date, mRelease));

                mViewHolder.ratingBar.setVisibility(View.VISIBLE);

                (mViewHolder.ratingBar).setRating((Float.parseFloat(mRating)) / 2.0f);

                (mViewHolder.userRating).setText(getString(R.string.user_rating, mRating));

                mMovieAPI.getMovieTrailer(mId, new Callback<MovieVideos>() {
                    @Override
                    public void success(final MovieVideos movieVideos, Response response) {

                        MovieVideosAdapter moviesVideoAdapter = new MovieVideosAdapter(getContext(),
                                R.layout.video_item, movieVideos.getResults());

                        (mViewHolder.videosList).setAdapter(moviesVideoAdapter);

                        if (movieVideos.getResults().size() > 0)
                            mVideoURL = BuildConfig.YOUTUBE_URL + movieVideos.getResults().get(0).getKey();
                        else
                            mVideoURL = "none";

                        (mViewHolder.videosList).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String url = BuildConfig.YOUTUBE_URL + movieVideos.getResults().get(position).getKey();
                                startActivity((new Intent(Intent.ACTION_VIEW)).setData(Uri.parse(url)));
                            }
                        });

                        mMovieAPI.getMovieReviewsFromId(mId, new Callback<MovieReviews>() {
                            @Override
                            public void success(final MovieReviews movieReviews, Response response) {

                                MovieReviewAdapter movieReviewAdapter = new MovieReviewAdapter(getContext(),
                                        R.layout.review_item, movieReviews.getResults());

                                (mViewHolder.reviewsList).setAdapter(movieReviewAdapter);

                                (mViewHolder.reviewsList).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        MovieReviewsResults result = movieReviews.getResults().get(position);

                                        new AlertDialog.Builder(getContext())
                                                .setTitle(result.getAuthor())
                                                .setMessage(result.getContent())
                                                .setPositiveButton("Close", null)
                                                .create().show();
                                    }
                                });

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                displayInternetConnectionError();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        displayInternetConnectionError();
                    }
                });
            }

            public void failure(RetrofitError error) {
                displayInternetConnectionError();
            }
        });
    }

    private void checkIifMovieIsInDatabase() {

        String[] column = new String[]{MoviesContract.MoviesEntry.COLUMN_ID};

        String[] args = new String[]{mId};

        Cursor c = mContentResolver.query(MoviesContract.BASE_CONTENT_URI, column,
                MoviesContract.MoviesEntry.COLUMN_ID + "=?", args, null);

        if (c != null && c.getCount() > 0) {
            inDB = c.moveToFirst();
            (mViewHolder.favoriteButton).setImageResource(R.drawable.ic_favorite);
        } else {
            inDB = false;
            (mViewHolder.favoriteButton).setImageResource(R.drawable.ic_favorite_border_black);
        }
        c.close();
    }

    @OnClick(R.id.favourite_fab)
    public void onFavoriteOptionClicked() {
        if (!inDB) {
            addMovieToFavorite();
        } else {
            removeMovieFromFavorite();
        }
    }

    public void onShareOptionClicked() {

        if (!mVideoURL.equalsIgnoreCase("none")) {
            String text = "See the trailer of: " + mMovieTitle + "\n\n" + mVideoURL;
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(EXTRA_TEXT, text)
                    , "Share Using"));
        } else {
            Toast.makeText(getContext(), "Nothing to share!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToFile(ImageView imageView, String id, String type) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        try {
            FileOutputStream output = mActivity.openFileOutput(id + type, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File readImageFromFile(String id, String type) {
        File file = null;
        try {
            file = new File(mActivity.getFilesDir(), id + type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private boolean deleteImageFile(String id, String type) {
        File file;
        try {
            file = new File(mActivity.getFilesDir(), id + type);
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addMovieToFavorite() {
        ContentValues values = new ContentValues();

        if (mOverview != null && mRating != null && mRelease != null) {
            values.put(MoviesContract.MoviesEntry.COLUMN_ID, mId);
            values.put(MoviesContract.MoviesEntry.COLUMN_TITLE, mMovieTitle);
            values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, mOverview);
            values.put(MoviesContract.MoviesEntry.COLUMN_RATING, mRating);
            values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, mRelease);

            saveImageToFile(mViewHolder.poster, mId, "poster");
            saveImageToFile(mViewHolder.thumbnail, mId, "thumbnail");

            mContentResolver.insert(MoviesContract.BASE_CONTENT_URI, values);

            Snackbar.make(rootView, mMovieTitle + " added to favourites"
                    , Snackbar.LENGTH_SHORT).show();
            checkIifMovieIsInDatabase();
        } else {
            Snackbar.make(rootView, "Unable to add " + mMovieTitle + " to favourites",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void removeMovieFromFavorite() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("Remove " + mMovieTitle + " from favourites");

        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContentResolver.delete(MoviesContract.BASE_CONTENT_URI,
                        MoviesContract.MoviesEntry.COLUMN_ID + "=?",
                        new String[]{mId});

                Snackbar.make(rootView, mMovieTitle + " removed from favourites",
                        Snackbar.LENGTH_SHORT).show();

                deleteImageFile(mId, "poster");
                deleteImageFile(mId, "thumbnail");

                checkIifMovieIsInDatabase();
            }
        });

        dialog.setNegativeButton(android.R.string.no, null);

        dialog.create().show();
    }

    private void displayInternetConnectionError() {
        mProgressBar.setVisibility(View.GONE);

        final Activity activity = getActivity();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Please check your internet connectivity");
    }

    public static class DetailsViewHolder {

        @Bind(R.id.imgPoster)
        public ImageView poster;

        @Bind(R.id.imgThumbnail)
        public ImageView thumbnail;

        @Bind(R.id.overview)
        public TextView overview;

        @Bind(R.id.release_date)
        public TextView releaseDate;

        @Bind(R.id.user_rating)
        public TextView userRating;

        @Bind(R.id.rating_bar)
        public RatingBar ratingBar;

        @Bind(R.id.videos_list_view)
        public HListView videosList;

        @Bind(R.id.reviews_list_view)
        public HListView reviewsList;

        @Bind(R.id.video_card_view)
        public CardView video;

        @Bind(R.id.review_card_view)
        public CardView review;

        @Bind(R.id.favourite_fab)
        public FloatingActionButton favoriteButton;

        public DetailsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}