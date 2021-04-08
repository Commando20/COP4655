package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    //Make data instance global to access from all classes and functions in activity
    WeatherData data = MainActivity.getDataInstance();

    TextView location, description, temperature, tempMax, tempMin, wind, pressure, humidity, sunrise, sunset, date;
    ImageView upArrow;
    ImageView downArrow;
    ConstraintLayout layout;

    long unixTime = System.currentTimeMillis() / 1000;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        layout = findViewById(R.id.layout);

        date = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        String dateTime = dateFormat.format(calendar.getTime());
        date.setText(dateTime);

        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        temperature = findViewById(R.id.temperature);
        upArrow = findViewById(R.id.upArrow);
        downArrow = findViewById(R.id.downArrow);
        tempMax = findViewById(R.id.Max);
        tempMin = findViewById(R.id.Min);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);

        location.setText(data.getName());
        description.setText(data.getDescription());

        temperature.setText(data.getTemperature() + "\u00B0 F");
        tempMax.setText(data.getTempMax() + "\u00B0 F");
        tempMin.setText(data.getTempMin()  + "\u00B0 F");
        wind.setText("Wind: " + data.getWind() + " mph");
        pressure.setText("Pressure: " + data.getPressure() + " hPa");
        humidity.setText("Humidity: " + data.getHumidity() + "%");
        sunrise.setText("Sunrise: " + data.getSunrise() + " AM");
        sunset.setText("Sunset: " + data.getSunset() + " PM");

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
            makeWhite();
            //make up and down arrows white
            upArrow.setImageResource(R.drawable.up_arrow);
            downArrow.setImageResource(R.drawable.down_arrow);
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
        }
        //If the current time is at sunset and the current time is less than sunset time plus an hour
        else if (unixTime == sunsetInt && unixTime < (sunsetInt + 3600)) {
            makeWhite();
            //make up and down arrows white
            upArrow.setImageResource(R.drawable.up_arrow);
            downArrow.setImageResource(R.drawable.down_arrow);
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.sunset));
        }
        //If the current time is past sunset (nighttime)
        else if (unixTime > sunsetInt) {
            makeWhite();
            //make up and down arrows white
            upArrow.setImageResource(R.drawable.up_arrow);
            downArrow.setImageResource(R.drawable.down_arrow);
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
        }

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

    public void makeWhite() {
        date.setTextColor(Color.parseColor("#FFFFFF"));
        location.setTextColor(Color.parseColor("#FFFFFF"));
        description.setTextColor(Color.parseColor("#FFFFFF"));
        temperature.setTextColor(Color.parseColor("#FFFFFF"));
        tempMax.setTextColor(Color.parseColor("#FFFFFF"));
        tempMin.setTextColor(Color.parseColor("#FFFFFF"));
        wind.setTextColor(Color.parseColor("#FFFFFF"));
        pressure.setTextColor(Color.parseColor("#FFFFFF"));
        humidity.setTextColor(Color.parseColor("#FFFFFF"));
        sunrise.setTextColor(Color.parseColor("#FFFFFF"));
        sunset.setTextColor(Color.parseColor("#FFFFFF"));
    }
}