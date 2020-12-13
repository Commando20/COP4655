package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    TextView name;
    TextView rating;
    TextView location;
    TextView hours;
    TextView phoneNumber;
    ToggleButton star;
    YelpData data = SearchActivity.getDataInstance();
    String openClosed;
    public static String businessName;
    //.document("users/test"); alternates between storing in users and collections
    private DocumentReference docRef;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        star = findViewById(R.id.star);

        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        location = findViewById(R.id.location);
        hours = findViewById(R.id.hours);
        phoneNumber = findViewById(R.id.phoneNumber);

        name.setVisibility(View.VISIBLE);
        rating.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
        hours.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);

        businessName = data.getName();
        name.setText(businessName);
        rating.setText("Rating: " + data.getRating());
        location.setText("Address: " + data.getAddress());

        openClosed = data.getOpenClosed();
        if (openClosed != null) {
            if(openClosed.equals("false")) {
                openClosed = "Open";
            } else openClosed = "Closed";
            hours.setText("Open/Closed: " + openClosed);
        }

        phoneNumber.setText("Phone Number: " + data.getPhoneNumber());

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(ProfileActivity.email).document(businessName);

        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    star.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_filled));
                    Toast toast = Toast.makeText(ResultsActivity.this, data.getName() + " has been added to your favorites", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Map<String, Object> document = new HashMap<>();
                    document.put("Business Name", data.getName());
                    document.put("Rating", data.getRating());
                    document.put("Location", data.getAddress());
                    document.put("Open or Closed", openClosed);
                    document.put("Phone Number", data.getPhoneNumber());
                    document.put("Latitude", data.getLat());
                    document.put("Longitude", data.getLong());
                    docRef.set(document).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("RESULT", "Document has been saved");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("RESULT", "Document has not been saved");
                        }
                    });
                } else {
                    star.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart));

                    docRef.collection(ProfileActivity.email).document(businessName).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("FavoriteDelete", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("FavoriteDelete", "Error deleting document", e);
                                }
                            });

                    Toast toast = Toast.makeText(ResultsActivity.this, data.getName() + " has been removed from your favorites", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    star.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_filled));
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