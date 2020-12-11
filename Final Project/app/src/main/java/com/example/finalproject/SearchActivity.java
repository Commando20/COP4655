package com.example.finalproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;

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
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps; //GPSTracker class
    double latitude, longitude;

    public static YelpData data = new YelpData();
    public ImageButton searchBtn;
    public ImageButton locationBtn;
    private EditText inputName;
    private EditText inputLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try { //If any permission not allowed by user, this condition will execute every time
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) { e.printStackTrace(); }

        searchBtn = findViewById(R.id.searchbtn);
        locationBtn = findViewById(R.id.locationbtn);
        searchBtn = findViewById(R.id.searchbtn);
        inputName = findViewById(R.id.inputName);
        inputLocation = findViewById(R.id.inputLocation);

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
                    Toast.makeText(SearchActivity.this, "Fetching results located in " + location, Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                //Keep location input to throw alert if user has a location search and presses location button
                String location = inputLocation.getText().toString();

                gps = new GPSTracker(SearchActivity.this);

                //Check if GPS enabled
                if(gps.canGetLocation()){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }else {
                    //can't get location
                    //GPS or Network is not enabled
                    //Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                if (name.isEmpty()) {
                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("Alert!"); //Set Alert Title
                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);
                    //Set the message show for the Alert time
                    builder.setMessage("You must provide a business name or type if you want to conduct a search");
                    //Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When the user click yes button then app will close
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create(); //Create the Alert dialog
                    alertDialog.show(); //Show the Alert Dialog box
                } else {
                    Toast.makeText(SearchActivity.this, "Fetching results based on current location", Toast.LENGTH_SHORT).show();
                    getYelpByTermAndGPS(name, latitude, longitude);
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
                        Intent home = new Intent(SearchActivity.this, ProfileActivity.class);
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
        final ArrayList<HashMap<String, String>> nameList = new ArrayList<>();
        final ListView lv = findViewById(R.id.list);
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

                        JSONObject coordObj = yelpObject.getJSONObject("coordinates");

                        JSONObject locationObj = yelpObject.getJSONObject("location");
                        String address = locationObj.getString("address1");
                        String city = locationObj.getString("city");
                        String state = locationObj.getString("state");
                        String zipCode = locationObj.getString("zip_code");

                        String location = address + ", " + city + ", " + state + " " + zipCode;
                        HashMap<String, String> businessList = new HashMap<>();
                        businessList.put("name", name);
                        businessList.put("location", location);
                        businessList.put("rating", yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        businessList.put("openClosed", yelpObject.getString("is_closed"));
                        businessList.put("phoneNumber", yelpObject.getString("display_phone"));
                        businessList.put("latitude", coordObj.getString("latitude"));
                        businessList.put("longitude", coordObj.getString("longitude"));
                        nameList.add(businessList);

                        Log.d("RESULT", name);
                        Log.d("RESULT", location);
                        Log.d("RESULT", yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        Log.d("RESULT", yelpObject.getString("is_closed"));
                        Log.d("RESULT", yelpObject.getString("display_phone"));
                        Log.d("RESULT", name);
                    }
                    //do another search, connect to api, and find same name of business
                    ListAdapter adapter = new SimpleAdapter(SearchActivity.this, nameList,
                            R.layout.list_item, new String[]{"name","location"},
                            new int[]{R.id.name, R.id.rating});
                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            HashMap<String, String> business = nameList.get(position);
                            data.setName(business.get("name"));
                            data.setAddress(business.get("rating"));
                            data.setRating(business.get("location"));
                            data.setOpenClosed(business.get("openClosed"));
                            data.setPhoneNumber(business.get("phoneNumber"));
                            data.setLat(business.get("latitude"));
                            data.setLong(business.get("longitude"));

                            Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
                            Toast.makeText(SearchActivity.this,"Fetching data", Toast.LENGTH_SHORT).show();
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

    public void getYelpByTermAndGPS(final String term, final double latitude, final double longitude) {
        final ArrayList<HashMap<String, String>> nameList = new ArrayList<>();
        final ListView lv = findViewById(R.id.list);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://api.yelp.com/v3/businesses/search?term=" + term + "&latitude=" + latitude + "&longitude=" + longitude;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray yelpArray = response.getJSONArray("businesses");

                    for (int i = 0; i < 8; i++) { //Loop to get results for 15 businesses
                        JSONObject yelpObject = yelpArray.getJSONObject(i);
                        String name = yelpObject.getString("name");

                        JSONObject coordObj = yelpObject.getJSONObject("coordinates");

                        JSONObject locationObj = yelpObject.getJSONObject("location");
                        String address = locationObj.getString("address1");
                        String city = locationObj.getString("city");
                        String state = locationObj.getString("state");
                        String zipCode = locationObj.getString("zip_code");

                        String location = address + ", " + city + ", " + state + " " + zipCode;
                        HashMap<String, String> businessList = new HashMap<>();
                        businessList.put("name", name);
                        businessList.put("location", location);
                        businessList.put("rating", yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        businessList.put("openClosed", yelpObject.getString("is_closed"));
                        businessList.put("phoneNumber", yelpObject.getString("display_phone"));
                        businessList.put("latitude", coordObj.getString("latitude"));
                        businessList.put("longitude", coordObj.getString("longitude"));
                        nameList.add(businessList);

                        Log.d("RESULT", name);
                        Log.d("RESULT", location);
                        Log.d("RESULT", yelpObject.getString("rating") + " / 5.0 rating out of "
                                + yelpObject.getString("review_count") + " reviews");
                        Log.d("RESULT", yelpObject.getString("is_closed"));
                        Log.d("RESULT", yelpObject.getString("display_phone"));
                        Log.d("RESULT", name);
                    }

                    ListAdapter adapter = new SimpleAdapter(SearchActivity.this, nameList,
                            R.layout.list_item, new String[]{"name","location"},
                            new int[]{R.id.name, R.id.rating});
                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            HashMap<String, String> business = nameList.get(position);
                            data.setName(business.get("name"));
                            data.setAddress(business.get("rating"));
                            data.setRating(business.get("location"));
                            data.setOpenClosed(business.get("openClosed"));
                            data.setPhoneNumber(business.get("phoneNumber"));
                            data.setLat(business.get("latitude"));
                            data.setLong(business.get("longitude"));

                            Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
                            Toast.makeText(SearchActivity.this,"Fetching data", Toast.LENGTH_SHORT).show();
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