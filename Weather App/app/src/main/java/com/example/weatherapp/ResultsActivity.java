package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    TextView location, description, temperature, tempMaxMin,
             wind, pressure, humidity, sunrise, sunset, coordinates;

    //Make data instance global to access from all classes and functions in activity
    WeatherData data = MainActivity.getDataInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        temperature = findViewById(R.id.temperature);
        tempMaxMin = findViewById(R.id.MaxMin);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        coordinates = findViewById(R.id.coordinates);

        location.setText(data.getName());
        description.setText(data.getDescription());
        temperature.setText(data.getTemperature() + "\u00B0 F");
        tempMaxMin.setText(data.getTempMaxMin());
        wind.setText("Wind: " + data.getWind() + " mph");
        pressure.setText("Pressure: " + data.getPressure() + " hPa");
        humidity.setText("Humidity: " + data.getHumidity() + "%");
        sunrise.setText("Sunrise: " + data.getSunrise() + " AM");
        sunset.setText("Sunset: " + data.getSunset() + " PM");
        coordinates.setText("Coordinates: " + data.getLatitude() + "\u00B0 , "
                            + data.getLongitude() + "\u00B0");

        //Get bottom nav id so an item select listener can be set for switching between activites
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_results);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: //If user goes to home page
                        Intent home = new Intent(ResultsActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_map: //If user goes to search page
                        Intent map = new Intent(ResultsActivity.this, MapActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_history: //If user goes to history page
                        Intent history = new Intent(ResultsActivity.this, HistoryActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}