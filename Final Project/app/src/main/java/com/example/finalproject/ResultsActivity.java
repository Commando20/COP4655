package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class ResultsActivity extends AppCompatActivity {
    public static TextView name;
    public static TextView rating;
    public static TextView location;
    public static TextView hours;
    public static TextView phoneNumber;
    public static ToggleButton star;
    public static Fragment map;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        star = findViewById(R.id.star);

        YelpData data = SearchActivity.getDataInstance();
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
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
        location.setVisibility(View.VISIBLE);
        hours.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);

        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    star.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_filled));
                    @SuppressLint("ShowToast")
                    Toast toast = Toast.makeText(ResultsActivity.this, data.getName() + " has been added to your favorites", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    star.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));
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
                        if (MainActivity.signIn == 0) {
                            Intent home = new Intent(ResultsActivity.this, MainActivity.class);
                            startActivity(home);
                        } else {
                            Intent home = new Intent(ResultsActivity.this, ProfileActivity.class);
                            startActivity(home);
                        }
                        break;
                    case R.id.navigation_search:
                        Intent search = new Intent(ResultsActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.navigation_favorites:
                        if (MainActivity.signIn == 0) {
                            // Create the object of AlertDialog Builder class
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);
                            // Set Alert Title
                            builder.setTitle("Alert!");
                            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                            builder.setCancelable(false);
                            //Set the message show for the Alert time
                            builder.setMessage("You must first login to be able to use this feature");
                            // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
                            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //When the user click yes button then app will close
                                    dialog.cancel();
                                }
                            });
                            //Create the Alert dialog
                            AlertDialog alertDialog = builder.create();
                            //Show the Alert Dialog box
                            alertDialog.show();
                        } else {
                            Intent favorites = new Intent(ResultsActivity.this, FavoritesActivity.class);
                            startActivity(favorites);
                        }
                        break;
                }
                return true;
            }
        });
    }
}