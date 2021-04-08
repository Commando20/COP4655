package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    //Make data instance global to access from all classes and functions in activity
    WeatherData data = MainActivity.getDataInstance();

    ListView lv;
    ConstraintLayout layout;

    String location = data.getName();
    double latitude = data.getLatitude();
    double longitude = data.getLongitude();

    long unixTimeInMillis = System.currentTimeMillis();
    long unixTime = System.currentTimeMillis() / 1000;

    int dayCounter = 1;
    String day;

    //Make an ArrayList for storing values for weather history
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        layout = findViewById(R.id.layoutHistory);
        lv = findViewById(R.id.list);

        String sunrise = MainActivity.sunrise;
        String sunset = MainActivity.sunset;
        int sunriseInt = Integer.parseInt(sunrise);
        int sunsetInt = Integer.parseInt(sunset);

        Log.e("RESULT", String.valueOf(unixTime));

        //If current time is after sunrise but before sunset
        if (unixTime > sunriseInt && unixTime < sunsetInt) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.daytime));
        }
        //If the current time is before sunrise
        else if (unixTime < sunriseInt) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
        }
        //If the current time is at sunset and the current time is less than sunset time plus an hour
        else if (unixTime == sunsetInt && unixTime < (sunsetInt + 3600)) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.sunset));
        }
        //If the current time is past sunset (nighttime)
        else if (unixTime > sunsetInt) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
        }

        //Get weather history from past 5 days
        //86400000 milliseconds is equal to 1 day
        //loop and decrement a day from time (max decrement of 5 days due to API restrictions)
        for(long i = unixTimeInMillis - 86400000; i > unixTimeInMillis - (86400000 * 6); i = i - 86400000) {
            getWeatherHistory(location, latitude, longitude, i / 1000);
        }

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

    public void getWeatherHistory(final String location, double latitude, double longitude, long time) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat=" + latitude + "&lon="
                + longitude + "&dt=" + time + "&appid=API_KEY&units=imperial";
        //Log.e("RESULT", URL);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Retrieves string from the response JSON Object and converts them into JSON object
                    JSONObject currentObject = response.getJSONObject("current");
                    String temp = currentObject.getString("temp");

                    double tempNum = Double.parseDouble(temp);
                    String temperature = Math.round(tempNum) + "\u00B0 F";

                    JSONArray weatherArray = currentObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String capitalizeDescription = description.substring(0, 1).toUpperCase() + description.substring(1);

                    //Log.e("RESULT", "temperature from this day is: " + temperature);
                    //Log.e("RESULT", "Location is: " + location);
                   // Log.e("RESULT", "Description of weather is: " + capitalizeDescription);
                    Log.e("RESULT", "-----------------------------------------------------------");
                    if (dayCounter == 1) {
                        day = "Yesterday";
                    } else day = dayCounter + " days ago";

                    //Create a new hash table to put values from weather history into
                    HashMap<String, String> weatherList = new HashMap<>();

                    weatherList.put("temperature", temperature);
                    weatherList.put("description", capitalizeDescription);
                    weatherList.put("location", location);
                    weatherList.put("day", day);
                    //Add hash table into ArrayList
                    arrayList.add(weatherList);

                    Log.e("RESULT", String.valueOf(arrayList));

                    //Create an adapter for ArrayList with only name and location for listView
                    ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, arrayList,
                            R.layout.list_item, new String[]{"temperature", "description", "location", "day"},
                            new int[]{R.id.temperature, R.id.description, R.id.location, R.id.day});
                    lv.setAdapter(adapter);

                    dayCounter++;
                } catch (JSONException e) { e.printStackTrace(); }
            }
        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError as parameter
                new Response.ErrorListener() {
                    @Override
                    //Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", String.valueOf(error));
                    }
                });
        queue.add(request);
    }
}