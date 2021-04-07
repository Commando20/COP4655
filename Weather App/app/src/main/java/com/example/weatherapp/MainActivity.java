package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

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
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Location-based services
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    public static WeatherData data = new WeatherData();
    public ImageView searchBtn;
    public ImageView locationBtn;
    public ImageView speechBtn;
    public EditText inputLocation;
    public ConstraintLayout layout;

    String location;
    double latitude, longitude;
    long unixTime = System.currentTimeMillis() / 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Once application starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
                //If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) { e.printStackTrace(); }

        inputLocation = findViewById(R.id.input);
        searchBtn = findViewById(R.id.searchBtn);
        locationBtn = findViewById(R.id.locationBtn);
        speechBtn = findViewById(R.id.speechBtn);
        layout = findViewById(R.id.layout);

        //If search button is clicked, collect weather data based on input
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLocation.getText().toString().isEmpty()) {
                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("You must first input a location if you are going to " +
                                       "search by location.");

                    // Set Alert Title
                    builder.setTitle("Alert!");

                    // Set Cancelable false for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name OnClickListener method is use of
                    // DialogInterface interface.
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When the user click yes button
                            // then app will close
                            // dialog.cancel();
                        }
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    getWeatherByLocation();
                }
            }
        });

        //If current location button is clicked, get user's location and get weather data
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create class object
                gps = new GPSTracker(MainActivity.this);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                getWeatherByGPS();
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
                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                switch (item.getItemId()) {
                    case R.id.navigation_results: //If user goes to results page
                        Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                    case R.id.navigation_map: //If user goes to search page
                        // Set the message show for the Alert time
                        builder.setMessage("You must first input a location in order to view the" +
                                " map of that area.");

                        // Set Alert Title
                        builder.setTitle("Alert!");

                        // Set Cancelable false for when the user clicks on the outside
                        // the Dialog Box then it will remain show
                        builder.setCancelable(false);

                        // Set the positive button with yes name OnClickListener method is use of
                        // DialogInterface interface.
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // When the user click yes button
                                // then app will close
                                // dialog.cancel();
                            }
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialogMap = builder.create();
                        // Show the Alert Dialog box
                        alertDialogMap.show();
                        break;
                    case R.id.navigation_history: //If user goes to history page
                        // Set the message show for the Alert time
                        builder.setMessage("You must first input a location in order to view the" +
                                           " weather history of that location.");

                        // Set Alert Title
                        builder.setTitle("Alert!");

                        // Set Cancelable false for when the user clicks on the outside
                        // the Dialog Box then it will remain show
                        builder.setCancelable(false);

                        // Set the positive button with yes name OnClickListener method is use of
                        // DialogInterface interface.
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // When the user click yes button then app will close dialog.cancel();
                            }
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialogHistory = builder.create();
                        // Show the Alert Dialog box
                        alertDialogHistory.show();
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
            URL = "https://api.openweathermap.org/data/2.5/weather?zip=" + input + "&appid=fef8540a70d2ee7ba4534ac73d4bd84b&units=imperial";
        } else {
            URL = "https://api.openweathermap.org/data/2.5/weather?q=" + input + "&appid=fef8540a70d2ee7ba4534ac73d4bd84b&units=imperial";
        }

        Log.e("RESULT", "url " + URL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
            @Override
            public void onResponse(JSONObject response) {
                try {
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
                    data.setLatitude(latitude);
                    data.setLongitude(longitude);

                    Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                    startActivity(results);
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

    public void getWeatherByGPS() {
        if(gps.canGetLocation()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=fef8540a70d2ee7ba4534ac73d4bd84b&units=imperial";
            Log.e("RESULT", "url " + URL);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
                @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
                @Override
                public void onResponse(JSONObject response) {
                    try {
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
                        //Log.e("RESULT", String.valueOf(dateSunrise));
                        //Log.e("RESULT", String.valueOf(dateSunset));

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
                        data.setLatitude(latitude);
                        data.setLongitude(longitude);

                        Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(results);
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
        }else {
            // can't get location, GPS or Network is not enabled, Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
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