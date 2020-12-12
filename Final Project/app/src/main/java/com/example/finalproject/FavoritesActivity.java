package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FavoritesActivity extends AppCompatActivity {

    YelpData data = SearchActivity.getDataInstance();
    ArrayList<HashMap<String, String>> favoriteList = new ArrayList<>();
    ListView lv;
    String name, rating, address, hours, phoneNumber, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        lv = findViewById(R.id.list);

        Task<QuerySnapshot> query = FirebaseFirestore.getInstance().collection(ProfileActivity.email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.exists()) {
                                    name = document.getString("Business Name");
                                    rating = document.getString("Rating");
                                    //address = documentSnapshot.getString("Location");
                                    // hours = documentSnapshot.getString("Open/Closed");
                                    phoneNumber = document.getString("Phone Number");
                                    latitude = document.getString("Latitude");
                                    longitude = document.getString("Longitude");
                                    //Could also do Map<String, Object> myData = documentSnapshot.getData();

                                    HashMap<String, String> favBusinessList = new HashMap<>();
                                    favBusinessList.put("name", name);
                                    favBusinessList.put("location", address);
                                    favBusinessList.put("rating", rating);
                                    favBusinessList.put("openClosed", hours);
                                    favBusinessList.put("phoneNumber", phoneNumber);
                                    favBusinessList.put("latitude", latitude);
                                    favBusinessList.put("longitude", longitude);
                                    favoriteList.add(favBusinessList);

                                    Log.d("FavoriteResult", name);
                                    // Log.d("FavoriteResult", address);
                                    Log.d("FavoriteResult", rating);
                                    //Log.d("FavoriteResult", hours);
                                    Log.d("FavoriteResult", phoneNumber);
                                    Log.d("FavoriteResult", latitude);
                                    Log.d("FavoriteResult", longitude);
                                }
                                ListAdapter adapter = new SimpleAdapter(FavoritesActivity.this, favoriteList,
                                        R.layout.favorite_list_item, new String[]{"name","location"},
                                        new int[]{R.id.name, R.id.location});
                                lv.setAdapter(adapter);
                                count++;
                            }
                        } else {
                            Log.d("favoriteResult", "Error getting documents: ", task.getException());
                        }
                        Log.d("favoriteResult", String.valueOf(count));
                    }
                });

        /*docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) { //Document snapshot is an object that represents a document
                if(documentSnapshot.exists()) {
                    name = documentSnapshot.getString("Business Name");
                    rating = documentSnapshot.getString("Rating");
                    //address = documentSnapshot.getString("Location");
                   // hours = documentSnapshot.getString("Open/Closed");
                    phoneNumber = documentSnapshot.getString("Phone Number");
                    latitude = documentSnapshot.getString("Latitude");
                    longitude = documentSnapshot.getString("Longitude");
                    //Could also do Map<String, Object> myData = documentSnapshot.getData();

                    HashMap<String, String> favBusinessList = new HashMap<>();
                    favBusinessList.put("name", name);
                    favBusinessList.put("location", address);
                    favBusinessList.put("rating", rating);
                    favBusinessList.put("openClosed", hours);
                    favBusinessList.put("phoneNumber", phoneNumber);
                    favBusinessList.put("latitude", latitude);
                    favBusinessList.put("longitude", longitude);
                    favoriteList.add(favBusinessList);

                    Log.d("FavoriteResult", name);
                   // Log.d("FavoriteResult", address);
                    Log.d("FavoriteResult", rating);
                    //Log.d("FavoriteResult", hours);
                    Log.d("FavoriteResult", phoneNumber);
                    Log.d("FavoriteResult", latitude);
                    Log.d("FavoriteResult", longitude);
                }
                ListAdapter adapter = new SimpleAdapter(FavoritesActivity.this, favoriteList,
                        R.layout.favorite_list_item, new String[]{"name","location"},
                        new int[]{R.id.name, R.id.location});
                lv.setAdapter(adapter);
            }
        });*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent text = new Intent(FavoritesActivity.this, ProfileActivity.class);
                        startActivity(text);
                        break;
                    case R.id.navigation_results:
                        Intent map = new Intent(FavoritesActivity.this, ResultsActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_search:
                        Intent history = new Intent(FavoritesActivity.this, SearchActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}