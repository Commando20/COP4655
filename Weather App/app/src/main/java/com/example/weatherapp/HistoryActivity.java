package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar


        //Get bottom nav id so an item select listener can be set for switching between activites
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: //If user goes to home page
                        Intent home = new Intent(HistoryActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_results: //If user goes to results page
                        Intent results = new Intent(HistoryActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                    case R.id.navigation_map: //If user goes to search page
                        Intent map = new Intent(HistoryActivity.this, MapActivity.class);
                        startActivity(map);
                        break;
                }
                return true;
            }
        });
    }
}