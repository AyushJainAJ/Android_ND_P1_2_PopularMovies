package com.example.ayush.project1.MovieDetails.Videos;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieVideosAdapter extends ArrayAdapter<MovieVideosResults> {
    private List<MovieVideosResults> mVideoResults;
    private Context mContext;
    private LayoutInflater mInflater;

    public MovieVideosAdapter(Context context, int resource, List<MovieVideosResults> videoResults) {
        super(context, resource, videoResults);

        mInflater = LayoutInflater.from(context);
        this.mVideoResults = videoResults;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mVideoResults.size();
    }

    @Override
    public MovieVideosResults getItem(int position) {
        return mVideoResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        /*
            convertView will have a non-null value when ListView is asking you to recycle the row
            layout. So,when convertView is not null, you should simply update its contents
            instead of inflating a new row layout.
        */

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(BuildConfig.YOUTUBE_IMAGE_HEADER + getItem(position).getKey()
                + BuildConfig.YOUTUBE_IMAGE_FOOTER)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(holder.getVideoImage());

        return convertView;
    }

    public static class ViewHolder {

        @Bind(R.id.video_image_image_view)
        public ImageView mVideoImage;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
        public ImageView getVideoImage() {
            return mVideoImage;
        }
    }
}