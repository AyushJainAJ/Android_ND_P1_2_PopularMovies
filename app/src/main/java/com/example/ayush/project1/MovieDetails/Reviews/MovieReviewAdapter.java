package com.example.ayush.project1.MovieDetails.Reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ayush.project1.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieReviewAdapter extends ArrayAdapter<MovieReviewsResults> {
    private List<MovieReviewsResults> mReviewsResults;

    private LayoutInflater mInflater;

    private Context mContext;

    public MovieReviewAdapter(Context context, int resource, List<MovieReviewsResults> reviewsResults) {

        super(context, resource, reviewsResults);

        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mReviewsResults = reviewsResults;
    }

    @Override
    public int getCount() {
        return mReviewsResults.size();
    }

    @Override
    public MovieReviewsResults getItem(int position) {
        return mReviewsResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.review_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        (holder.getAuthor()).setText(getItem(position).getAuthor());
        (holder.getContent()).setText(getItem(position).getContent());

        return convertView;
    }

    public static class ViewHolder {

        @Bind(R.id.review_author)
        public TextView mAuthor;

        @Bind(R.id.review_content)
        public TextView mContent;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public TextView getAuthor() {
            return mAuthor;
        }

        public TextView getContent() {
            return mContent;
        }
    }
}