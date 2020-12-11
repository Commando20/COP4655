package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResultsActivity extends AppCompatActivity {
    TextView name;
    TextView rating;
    TextView price;
    TextView location;
    TextView hours;
    TextView phoneNumber;
    ToggleButton heart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        heart = findViewById(R.id.heart);

        YelpData data = SearchActivity.getDataInstance();
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.price);
        location = findViewById(R.id.location);
        hours = findViewById(R.id.hours);
        phoneNumber = findViewById(R.id.phoneNumber);

        name.setText(data.getName());
        rating.setText("Rating: " + data.getRating());
        location.setText("Address: " + data.getAddress());

        String openClosed = data.getOpenClosed();
        if (openClosed != null) {
            if(openClosed.equals("false")) {
                openClosed = "Open";
            } else openClosed = "Closed";
            hours.setText("Open/Closed: " + openClosed);
        }

        phoneNumber.setText("Phone Number: " + data.getPhoneNumber());

        name.setVisibility(View.VISIBLE);
        rating.setVisibility(View.VISIBLE);
        price.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
        hours.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);

        heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_filled));
                    @SuppressLint("ShowToast")
                    Toast toast = Toast.makeText(ResultsActivity.this, data.getName() + " has been added to your favorites", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
                    @SuppressLint("ShowToast")
                    Toast toast = Toast.makeText(ResultsActivity.this, data.getName() + " has been removed from your favorites", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_results);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent home = new Intent(ResultsActivity.this, ProfileActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_search:
                        Intent search = new Intent(ResultsActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.navigation_favorites:
                        Intent favorites = new Intent(ResultsActivity.this, FavoritesActivity.class);
                        startActivity(favorites);
                        break;
                }
                return true;
            }
        });
    }
}