package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.yelp);

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.yelp.com/v3/businesses/search?location=NYC", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","bearer " + "IGeH9oQlcaQpbdrzxIUBCDfC6zgIC4dkRt2_LEE2W99GHrW5JKl91db_nWarHKP9RgpzfouaXn8IW3q8HEwF_o3V6tIzgghXQCWnKq5MGurm9vC7ZaJWkXD5yYyyX3Yx");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        Intent text = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(text);
                        break;
                    case R.id.navigation_results:
                        Intent map = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(map);
                        break;
                    case R.id.navigation_favorites:
                        Intent history = new Intent(MainActivity.this, FavoritesActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}