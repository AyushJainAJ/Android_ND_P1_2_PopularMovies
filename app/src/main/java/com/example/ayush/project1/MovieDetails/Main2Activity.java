package com.example.ayush.project1.MovieDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ayush.project1.R;
import com.example.ayush.project1.SettingsActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putString("id", getIntent().getStringExtra("Selected"));

            Main2Fragment fragInfo = new Main2Fragment();
            fragInfo.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragInfo)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}