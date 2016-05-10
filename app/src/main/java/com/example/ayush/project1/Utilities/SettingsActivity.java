package com.example.ayush.project1.Utilities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by AJ on 09-05-2016.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
