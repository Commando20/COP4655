package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                getWeatherByLocation();

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
            URL = "https://api.openweathermap.org/data/2.5/weather?zip=" + input + "&appid=fef8540a70d2ee7ba4534ac73d4bd84b";
        } else {
            URL = "https://api.openweathermap.org/data/2.5/weather?q=" + input + "&appid=fef8540a70d2ee7ba4534ac73d4bd84b";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent text = new Intent(MainActivity.this, ResultsActivity.class);

                    //Retrieves string from the response JSON Object and converts them into javascript object
                    String location = response.getString("name");

                    JSONObject mainObject = response.getJSONObject("main");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String capitalize = description.substring(0, 1).toUpperCase() + description.substring(1);

                    String temperature = mainObject.getString("temp");
                    double temp = Double.parseDouble(temperature);
                    double fahrenheit_double = ((temp - 273.15) * 9 / 5) + 32;
                    int fahrenheit = (int) fahrenheit_double;
                    temperature =  fahrenheit + "\u00B0 F";

                    String temp_max = mainObject.getString("temp_max");
                    double max = Double.parseDouble(temp_max);
                    double max_conversion = ((max - 273.15) * 1.8) + 32;
                    int max_temp = (int) max_conversion;

                    String temp_min = mainObject.getString("temp_min");
                    double min = Double.parseDouble(temp_min);
                    double min_conversion = ((min - 273.15) * 1.8) + 32;
                    int min_temp = (int) min_conversion;
                    String temp_maxmin = max_temp + "\u00B0 F / " + min_temp + "\u00B0 F";

                    JSONObject windObject = response.getJSONObject("wind");
                    String wind = windObject.getString("speed");
                    wind = "Wind: " + wind + " m/s";

                    String pressure = mainObject.getString("pressure");
                    pressure = "Pressure: " + pressure + " hPa";


                    String humidity = mainObject.getString("humidity");
                    humidity = "Humidity: " + humidity + "%";

                    JSONObject sysObject = response.getJSONObject("sys");
                    String sunrise = sysObject.getString("sunrise");
                            /*int sunriseInt = Integer.parseInt(sunrise) * 1000;
                            LocalTime rise = LocalTime.from(sunriseInt);
                            int riseHours = sunriseInt.getHour();
                            int riseMinutes = rise.getMinute();
                            riseHours = (riseHours > 12) ? (riseHours - 12) : riseHours;
                            riseMinutes = (riseMinutes > 12) ? (riseMinutes - 12) : riseMinutes;*/

                    String sunset = sysObject.getString("sunset");
                    sunrise = "Sunrise: " + sunrise + " AM";
                    sunset = "Sunset: " + sunset + " PM";


                    JSONObject coordObject = response.getJSONObject("coord");
                    String lon = coordObject.getString("lon");
                    //longitudeValue = Double.parseDouble(lon);
                    String lat = coordObject.getString("lat");
                    //latitudeValue = Double.parseDouble(lat);
                    String coords = "Coordinates: " + lat + "\u00B0, " + lon + "\u00B0";


                    Log.e("RESULT", location);
                    Log.e("RESULT", capitalize);
                    Log.e("RESULT", temperature);
                    Log.e("RESULT", temp_maxmin);
                    Log.e("RESULT", wind);
                    Log.e("RESULT", pressure);
                    Log.e("RESULT", humidity);
                    Log.e("RESULT", sunrise);
                    Log.e("RESULT", sunset);
                    Log.e("RESULT", coords);

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
