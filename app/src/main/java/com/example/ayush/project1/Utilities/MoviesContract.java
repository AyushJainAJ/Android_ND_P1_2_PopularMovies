package com.example.ayush.project1.Utilities;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.ayush.project1.BuildConfig;

public class MoviesContract {
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + BuildConfig.CONTENT_AUTHORITY);

    public static final class MoviesEntry implements BaseColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + BuildConfig.CONTENT_AUTHORITY;

        public static final String TABLE_NAME = BuildConfig.KEY_TABLE_MOVIES;

        public static final String COLUMN_ID = BuildConfig.KEY_ID;
        public static final String COLUMN_TITLE = BuildConfig.KEY_TITLE;
        public static final String COLUMN_OVERVIEW = BuildConfig.KEY_OVERVIEW;
        public static final String COLUMN_RATING = BuildConfig.KEY_RATING;
        public static final String COLUMN_RELEASE_DATE = BuildConfig.KEY_RELEASE_DATE;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(BASE_CONTENT_URI, id);
        }
    }
}
