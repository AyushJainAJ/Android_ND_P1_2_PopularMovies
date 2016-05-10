package com.example.ayush.project1.MovieGrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayush.project1.BuildConfig;
import com.example.ayush.project1.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

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

//            Toast.makeText(mContext, moviesResult.getOriginalTitle(), Toast.LENGTH_SHORT).show();
            Picasso.with(mContext).load(holder.readImageFromFile(mContext, moviesResult.getId(), "thumbnail"))
                    .placeholder(R.drawable.loading)
                    .into(holder.getPosterImg());
        } else {
            Picasso.with(mContext)
                    .load(BuildConfig.IMAGE_URL + mMoviesList.get(position).getPhoto())
                    .into(holder.getPosterImg());
        }
        return convertView;
    }

    private static class ViewHolder {

        private ImageView mPosterImg;

        public ViewHolder(View view) {
            this.mPosterImg = ((ImageView) view.findViewById(R.id.posterImg));
        }

        public ImageView getPosterImg() {
            return mPosterImg;
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