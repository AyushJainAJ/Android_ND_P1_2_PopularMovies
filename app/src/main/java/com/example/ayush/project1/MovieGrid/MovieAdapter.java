package com.example.ayush.project1.MovieGrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class MovieAdapter extends ArrayAdapter<MoviesResults> {

    private final Context mContext;

    private final List<MoviesResults> mMoviesList;

    private LayoutInflater mInflater;

    private boolean mIsFavorite;

    public MovieAdapter(Context context, int resource, List<MoviesResults> movieResults, boolean isFavorite) {
        super(context, resource, movieResults);

        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMoviesList = movieResults;
        this.mIsFavorite = isFavorite;
    }

    /*
    position - Row position
    convertView - ScrapView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        MoviesResults moviesResult = getItem(position);

        /*
            convertView will have a non-null value when ListView is asking you to recycle the row
            layout. So,when convertView is not null, you should simply update its contents
            instead of inflating a new row layout.
        */

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mIsFavorite) {
            Picasso.with(mContext)
                    .load(holder.readImageFromFile(mContext, moviesResult.getId(), "thumbnail"))
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(holder.getPosterImg());
        } else {
            Picasso.with(mContext)
                    .load(BuildConfig.IMAGE_URL + mMoviesList.get(position).getPhoto())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(holder.getPosterImg());
        }
        return convertView;
    }

    public static class ViewHolder {

        @Bind(R.id.posterImg)
        public ImageView mPosterImg;

        public ImageView getPosterImg() {
            return mPosterImg;
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public File readImageFromFile(Context context, String id, String type) {
            File file = null;
            try {
                file = new File(context.getFilesDir(), id + type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }
    }
}