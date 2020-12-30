package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);




        //Get bottom nav id so an item select listener can be set for switching between activites
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: //If user goes to home page
                        Intent home = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_results: //If user goes to results page
                        Intent results = new Intent(SearchActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                    case R.id.navigation_history: //If user goes to history page
                        Intent history = new Intent(SearchActivity.this, HistoryActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}
