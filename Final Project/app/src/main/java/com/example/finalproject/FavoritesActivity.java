package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FavoritesActivity extends AppCompatActivity {

    YelpData data = SearchActivity.getDataInstance();
    ArrayList<HashMap<String, String>> favoriteList = new ArrayList<>();
    ListView lv;
    ListAdapter adapter;
    public static int positionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Get rid of pesky titlebar

        lv = findViewById(R.id.favoriteList);

        Task<QuerySnapshot> query = FirebaseFirestore.getInstance().collection(ProfileActivity.email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    String name = document.getString("Business Name");
                                    String rating = document.getString("Rating");
                                    String address = document.getString("Location");
                                    String hours = document.getString("Open or Closed");
                                    String phoneNumber = document.getString("Phone Number");
                                    String latitude = document.getString("Latitude");
                                    String longitude = document.getString("Longitude");

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
                                }
                                ListAdapter adapter = new SimpleAdapter(FavoritesActivity.this, favoriteList,
                                        R.layout.favorite_list_item, new String[]{"name", "location"},
                                        new int[]{R.id.favName, R.id.favLocation});
                                lv.setAdapter(adapter);
                                count++;

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @SuppressLint("UseCompatLoadingForDrawables")
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        SearchActivity.searchConducted = 2;
                                        HashMap<String, String> business = favoriteList.get(position);
                                        data.setName(business.get("name"));
                                        data.setRating(business.get("rating"));
                                        data.setAddress(business.get("location"));
                                        data.setOpenClosed(business.get("openClosed"));
                                        data.setPhoneNumber(business.get("phoneNumber"));
                                        data.setLat(business.get("latitude"));
                                        data.setLong(business.get("longitude"));

                                        positionCount = position;

                                        Intent intent = new Intent(FavoritesActivity.this, ResultsActivity.class);
                                        intent.putExtra("favoriteList", favoriteList);
                                        Toast.makeText(FavoritesActivity.this, "Fetching data", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            Log.d("favoriteResult", "Error getting documents: ", task.getException());
                        }
                        Log.d("favoriteResult", String.valueOf(count));
                    }
                });

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