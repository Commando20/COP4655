package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static WeatherData data = new WeatherData();
    public ImageView searchBtn;
    public ImageView locationBtn;
    public ImageView speechBtn;
    public EditText inputLocation;
    public ConstraintLayout layout;

    String location;
    double latitude, longitude;
    long unixTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        inputLocation = findViewById(R.id.input);
        searchBtn = findViewById(R.id.searchBtn);
        locationBtn = findViewById(R.id.locationBtn);
        speechBtn = findViewById(R.id.speechBtn);
        layout = findViewById(R.id.layout);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                getWeatherByLocation();
                getWeatherHistory(location, latitude, longitude, 1609465557);
                Log.e("RESULT", "location is " + location);

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

    public void getWeatherByLocation() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String input = inputLocation.getText().toString();
        String URL;

        //Change the API URL to have a parameter of either zip code or not
        //if the user's input is a number or not
        if (Utility.numberOrNot(input)){
            URL = "https://api.openweathermap.org/data/2.5/weather?zip=" + input + "&appid=YOUR_API_KEY&units=imperial";
        } else {
            URL = "https://api.openweathermap.org/data/2.5/weather?q=" + input + "&appid=YOUR_API_KEY&units=imperial";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent text = new Intent(MainActivity.this, ResultsActivity.class);

                    //Retrieves string from the response JSON Object and converts them into JSON object
                    location = response.getString("name");
                    data.setName(location);
                    Log.e("RESULT", "location is " + location);

                    JSONObject mainObject = response.getJSONObject("main");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String capitalize = description.substring(0, 1).toUpperCase() + description.substring(1);
                    data.setDescription(capitalize);

                    String temperature = mainObject.getString("temp");
                    double temp = Double.parseDouble(temperature);
                    data.setTemperature(String.valueOf(Math.round(temp)));

                    String temp_max = mainObject.getString("temp_max");
                    double max_temp = Double.parseDouble(temp_max);
                    String temp_min = mainObject.getString("temp_min");
                    double min_temp = Double.parseDouble(temp_min);
                    String temp_maxmin = Math.round(max_temp) + "\u00B0 F / " + Math.round(min_temp) + "\u00B0 F";
                    data.setTempMaxMin(temp_maxmin);

                    JSONObject windObject = response.getJSONObject("wind");
                    String wind = windObject.getString("speed");
                    data.setWind(wind);

                    String pressure = mainObject.getString("pressure");
                    data.setPressure(pressure);

                    String humidity = mainObject.getString("humidity");
                    data.setHumidity(humidity);

                    long unixTime = System.currentTimeMillis() / 1000L;
                    JSONObject sysObject = response.getJSONObject("sys");
                    String sunrise = sysObject.getString("sunrise");
                    String sunset = sysObject.getString("sunset");
                    int sunriseInt = Integer.parseInt(sunrise);
                    int sunsetInt = Integer.parseInt(sunset);

                    if (unixTime > sunriseInt && unixTime < sunsetInt)
                        Log.e("RESULT", "it is night before sunrise");
                    else if (unixTime < sunriseInt)
                        Log.e("RESULT", "it is night before sunrise");
                    else if (unixTime > sunsetInt) {
                        Log.e("RESULT", "it is night after sunset");
                        //layout.setBackgroundResource(R.drawable.night);
                        getWindow().getDecorView().setBackgroundResource(R.drawable.night);
                    }

                    //Need to multiply by 1000L or else date will be in 1970
                    Date dateSunrise = new Date(Long.parseLong(sunrise) * 1000L);
                    Date dateSunset = new Date(Long.parseLong(sunset) * 1000L);
                    Log.e("RESULT", String.valueOf(dateSunrise));
                    Log.e("RESULT", String.valueOf(dateSunset));

                    DateFormat format = new SimpleDateFormat("hh:mm");
                    String formattedSunrise = format.format(dateSunrise);
                    String formattedSunset = format.format(dateSunset);
                    data.setSunrise(formattedSunrise);
                    data.setSunset(formattedSunset);

                    JSONObject coordObject = response.getJSONObject("coord");
                    String lat = coordObject.getString("lat");
                    latitude = Double.parseDouble(lat);
                    String lon = coordObject.getString("lon");
                    longitude = Double.parseDouble(lon);
                    data.setLatitude(lat);
                    data.setLongitude(lon);

                    startActivity(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        // The final parameter overrides the method onErrorResponse() and passes VolleyError
        //as a parameter
        new Response.ErrorListener() {
            @Override
            //Handles errors that occur due to Volley
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", String.valueOf(error));
            }
        });
        queue.add(request);
    }

    public void getWeatherHistory(final String location, double latitude, double longitude, long time) {
        //Make an ArrayList for storing values for weather history
        final ArrayList<HashMap<String, String>> list = new ArrayList<>();
        final ListView lv = findViewById(R.id.list);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat=" + latitude + "&lon="
                + longitude + "&dt=" + time + "&appid=YOUR_API_KEY&units=imperial";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Retrieves string from the response JSON Object and converts them into JSON object
                    JSONObject currentObject = response.getJSONObject("current");
                    String temperature = currentObject.getString("temp");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String capitalize = description.substring(0, 1).toUpperCase() + description.substring(1);

                    //Create a new hash table to put values from weather history into
                    HashMap<String, String> weatherList = new HashMap<>();
                    weatherList.put("location", location);
                    weatherList.put("description", capitalize);
                    weatherList.put("temperature", temperature);
                    //Add hash table into ArrayList
                    list.add(weatherList);

                    //Create an adapter for ArrayList with only name and location for listView
                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, list,
                            R.layout.list_item, new String[]{"location", "description", "temperature"},
                            new int[]{R.id.location, R.id.description, R.id.temperature});
                    lv.setAdapter(adapter);

                    Log.e("RESULT", location);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

class Utility {
    static boolean numberOrNot(String userInput) {
        try {
            Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}