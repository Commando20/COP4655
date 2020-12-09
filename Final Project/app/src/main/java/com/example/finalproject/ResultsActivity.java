package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class ResultsActivity extends AppCompatActivity {

    TextView name;
    TextView rating;
    TextView price;
    TextView location;
    TextView hours;
    TextView phoneNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        YelpData data = SearchActivity.getDataInstance();
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.price);
        location = findViewById(R.id.location);
        hours = findViewById(R.id.hours);
        phoneNumber = findViewById(R.id.phoneNumber);

        name.setText(data.getName());
        rating.setText("Rating: " + data.getRating());

        //String phoneNum = data.getPhoneNumber().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d+)", "($2) $2-$4");

        location.setText("Address: " + data.getAddress());

        String openClosed = data.getOpenClosed();
        if(openClosed.equals("false")) {
            openClosed = "Closed";
        } else openClosed = "Open";
        hours.setText("Hours: " + openClosed);

        phoneNumber.setText("Phone Number: " + data.getPhoneNumber());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_results);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent text = new Intent(ResultsActivity.this, MainActivity.class);
                        startActivity(text);
                        break;
                    case R.id.navigation_search:
                        Intent map = new Intent(ResultsActivity.this, SearchActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_favorites:
                        Intent history = new Intent(ResultsActivity.this, FavoritesActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}