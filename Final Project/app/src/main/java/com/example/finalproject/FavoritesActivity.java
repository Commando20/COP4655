package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent text = new Intent(FavoritesActivity.this, ProfileActivity.class);
                        startActivity(text);
                        break;
                    case R.id.navigation_results:
                        Intent map = new Intent(FavoritesActivity.this, ResultsActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_search:
                        Intent history = new Intent(FavoritesActivity.this, SearchActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }


}