package com.example.ayush.project1.MovieDetails.AllDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ayush.project1.R;
import com.example.ayush.project1.Utilities.SettingsActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsFragment mFragInfo;
    private boolean isFavorite;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            isFavorite = getIntent().getBooleanExtra("Preference", false);
            id = getIntent().getStringExtra("Selected");

            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            bundle.putBoolean("preference",isFavorite);

            mFragInfo = new MovieDetailsFragment();
            mFragInfo.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, mFragInfo)
                    .commit();
        }
    }
}