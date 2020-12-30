package com.example.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static WeatherData data = new WeatherData();
    public ImageView searchBtn;
    public ImageView locationBtn;
    public ImageView speechBtn;
    public EditText inputLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLocation = findViewById(R.id.input);
        searchBtn = findViewById(R.id.searchBtn);
        locationBtn = findViewById(R.id.locationBtn);
        speechBtn = findViewById(R.id.speechBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(results);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(results);
            }
        });

        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(results);
            }
        });

        //Get bottom nav id so an item select listener can be set for switching between activites
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_results: //If user goes to results page
                        Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                    case R.id.navigation_map: //If user goes to search page
                        Intent map = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_history: //If user goes to history page
                        Intent history = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }

    public static WeatherData getDataInstance() { return data; }

    /*public void getWeatherByLocation(final String location) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://api.yelp.com/v3/businesses/search?term=" + term + "&location=" + location;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //First item in reponse is an Array
                    JSONArray yelpArray = response.getJSONArray("businesses");

                    //Loop to get results for 15 businesses
                    for (int i = 0; i < 15; i++) {
                        JSONObject yelpObject = yelpArray.getJSONObject(i);
                        String name = yelpObject.getString("name");

                        JSONObject coordObj = yelpObject.getJSONObject("coordinates");

                        JSONObject locationObj = yelpObject.getJSONObject("location");
                        String address = locationObj.getString("address1");
                        String city = locationObj.getString("city");
                        String state = locationObj.getString("state");
                        String zipCode = locationObj.getString("zip_code");
                        String location = address + ", " + city + ", " + state + " " + zipCode;

                        Log.d("RESULT", name);
                        Log.d("RESULT", location);
                        Log.d("RESULT", yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        Log.d("RESULT", yelpObject.getString("is_closed"));
                        Log.d("RESULT", yelpObject.getString("display_phone"));
                    }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        queue.add(request);
    }

    public void getWeatherByGPS() {}*/
}