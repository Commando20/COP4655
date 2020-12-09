package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public static YelpData data = new YelpData();
    public ImageButton searchBtn;
    public ImageButton locationBtn;
    private EditText inputName;
    private EditText inputLocation;
    ArrayList<HashMap<String, String>> nameList;
    private ListView lv;

    String itemName, itemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBtn = findViewById(R.id.searchbtn);
        locationBtn = findViewById(R.id.locationbtn);
        searchBtn = findViewById(R.id.searchbtn);
        inputName = findViewById(R.id.inputName);
        inputLocation = findViewById(R.id.inputLocation);
        nameList = new ArrayList<>();
        lv = findViewById(R.id.list);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String location = inputLocation.getText().toString();

                if (location.isEmpty() || name.isEmpty()) {
                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    // Set Alert Title
                    builder.setTitle("Alert!");
                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);
                    //Set the message show for the Alert time
                    builder.setMessage("You must provide an input to both searches if you want to search for a business");
                    // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When the user click yes button then app will close
                            dialog.cancel();
                        }
                    });
                    //Create the Alert dialog
                    AlertDialog alertDialog = builder.create();
                    //Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    getYelpByTermAndLocation(name, location);
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent home = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_favorites:
                        Intent favorites = new Intent(SearchActivity.this, FavoritesActivity.class);
                        startActivity(favorites);
                        break;
                    case R.id.navigation_results:
                        Intent results = new Intent(SearchActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                }
                return true;
            }
        });
    }

    public static YelpData getDataInstance() { return data; }

    public void getYelpByTermAndLocation (final String term, final String location) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://api.yelp.com/v3/businesses/search?term=" + term + "&location=" + location;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray yelpArray = response.getJSONArray("businesses");

                    for (int i = 0; i < 15; i++) { //Loop to get results for 15 businesses
                        JSONObject yelpObject = yelpArray.getJSONObject(i);
                        String name = yelpObject.getString("name");

                        JSONObject locationObj = yelpObject.getJSONObject("location");
                        String address = locationObj.getString("address1");
                        String city = locationObj.getString("city");
                        String state = locationObj.getString("state");
                        String zipCode = locationObj.getString("zip_code");

                        String location = address + ", " + city + ", " + state + " " + zipCode;
                        HashMap<String, String> businessList = new HashMap<>();
                        businessList.put("name", name);
                        businessList.put("location", location);
                        nameList.add(businessList);

                        data.setName(yelpObject.getString("name"));
                        data.setRating(yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        data.setAddress(address + ", " + city + ", " + state + " " + zipCode);
                        data.setOpenClosed(yelpObject.getString("is_closed"));
                        data.setPhoneNumber(yelpObject.getString("display_phone"));

                        JSONObject coordObj = yelpObject.getJSONObject("coordinates");
                        data.setLat(coordObj.getString("latitude"));
                        data.setLong(coordObj.getString("longitude"));

                    }
                    //do another search, connect to api, and find same name of business
                    ListAdapter adapter = new SimpleAdapter(SearchActivity.this, nameList,
                            R.layout.list_item, new String[]{"name","location"},
                            new int[]{R.id.name, R.id.rating});
                    //((BaseAdapter)adapter).notifyDataSetChanged();
                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            /*RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            String URL = "https://api.yelp.com/v3/businesses/search?term=" + term + "&location=" + location;
                            System.out.println(term + "   " + location);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL , new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray yelpArray = response.getJSONArray("businesses");
                                        int total = Integer.parseInt(response.getString("total"));

                                        HashMap<String, String> business = nameList.get(position);
                                        System.out.println(business);
                                        itemName = business.get("name");
                                        System.out.println(itemName);
                                        itemName = business.get("location");
                                        System.out.println(itemLocation);

                                        for (int i = 0; i < total; i++) { //Loop to get results for 20 businesses
                                            JSONObject yelpObject = yelpArray.getJSONObject(i);
                                            String name = yelpObject.getString("name");

                                            JSONObject locationObj = yelpObject.getJSONObject("location");
                                            String address = locationObj.getString("address1");
                                            String city = locationObj.getString("city");
                                            String state = locationObj.getString("state");
                                            String zipCode = locationObj.getString("zip_code");
                                            String location = address + ", " + city + ", " + state + " " + zipCode;

                                            if (itemName.equals(name)) {
                                                System.out.println("it appears that " + itemName + "and " + name + " are a match.");
                                                data.setName(yelpObject.getString("name"));
                                                data.setRating(yelpObject.getString("rating") + " / 5.0 rating out of "
                                                        + yelpObject.getString("review_count") + " reviews");
                                                data.setAddress(address + ", " + city + ", " + state + " " + zipCode);
                                                data.setOpenClosed(yelpObject.getString("is_closed"));
                                                data.setPhoneNumber(yelpObject.getString("display_phone"));

                                                JSONObject coordObj = yelpObject.getJSONObject("coordinates");
                                                data.setLat(coordObj.getString("latitude"));
                                                data.setLong(coordObj.getString("longitude"));
                                            }
                                        }
                                    } catch (JSONException e) { e.printStackTrace(); }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Volley error");
                                }
                            }) {
                                @Override //This is for Headers If You Needed
                                public Map<String,String> getHeaders() {
                                    Map<String,String> params = new HashMap<>();
                                    params.put("Authorization","bearer " + "IGeH9oQlcaQpbdrzxIUBCDfC6zgIC4dkRt2_LEE2W99GHrW5JKl91db_nWarHKP9RgpzfouaXn8IW3q8HEwF_o3V6tIzgghXQCWnKq5MGurm9vC7ZaJWkXD5yYyyX3Yx");
                                    return params;
                                }
                            };
                            queue.add(request);*/
                            Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
                            Toast.makeText(SearchActivity.this,"Fetching data for " + itemName, Toast.LENGTH_SHORT).show();
                            //System.out.println(nameList.get(position));
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error");
            }
        }) {
            @Override //This is for Headers If You Needed
            public Map<String,String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","bearer " + "IGeH9oQlcaQpbdrzxIUBCDfC6zgIC4dkRt2_LEE2W99GHrW5JKl91db_nWarHKP9RgpzfouaXn8IW3q8HEwF_o3V6tIzgghXQCWnKq5MGurm9vC7ZaJWkXD5yYyyX3Yx");
                return params;
            }
        };
        queue.add(request);
    }
}