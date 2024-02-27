package com.example.passion_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User_main extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICKER = 102;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private EditText searchEditText;
    private ImageView cameraImage;
    private Button searchButton;
    private Uri imageUri;
    private ListView shopListView;

    private ArrayAdapter<String> placeAdapter;  // Adapter to populate the ListView
    private List<String> placeNames = new ArrayList<>();

    private PlacesClient placesClient;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Initialize the Places SDK with your API key (add your API key to strings.xml).
        Places.initialize(getApplicationContext(), getString(R.string.google_places_api_key));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Initialize the Places Client
        placesClient = Places.createClient(this);

        searchEditText = findViewById(R.id.searchEditText);
        cameraImage = findViewById(R.id.cameraIcon);
        searchButton = findViewById(R.id.searchButton);
        shopListView = findViewById(R.id.shopListView);

        placeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placeNames);
        shopListView.setAdapter(placeAdapter);

        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraOrImagePicker();
            }
        });

        // Set a click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyword = searchEditText.getText().toString();
                performSearch(searchKeyword);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_explore) {
                            // Handle Explore selection (e.g., start Explore activity)
                            startActivity(new Intent(User_main.this, User_explore.class));
                            finish(); // Close the current activity
                            return true;
                        } else if (itemId == R.id.navigation_main) {
                            // Do nothing (already in User_Main)
                            return true;
                        } else if (itemId == R.id.navigation_account) {
                            // Handle Account selection (e.g., start Account activity)
                            startActivity(new Intent(User_main.this, User_account.class));
                            finish(); // Close the current activity
                            return true;
                        }
                        return false;
                    }
                }
        );


    }

    private void openCameraOrImagePicker() {
        // Build an AlertDialog with two options: "Gallery" and "Camera"
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Gallery option selected
                        openImagePicker();
                        break;
                    case 1:
                        // Camera option selected
                        openCamera();
                        break;
                }
            }
        });

        builder.show();
    }

    private void openImagePicker() {
        // Create an intent to open the gallery for image selection
        // Implement your logic for image selection here.
    }

    private void openCamera() {
        // Create an intent to open the camera
        // Implement your logic for taking a photo here.
    }

    private void performSearch(String keyword) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        // Get the last known location
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Use the user's current location to add a location bias to the request
                        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

                        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                .setSessionToken(token)
                                .setQuery(keyword)
                                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                .setLocationBias(RectangularBounds.newInstance(
                                        new LatLng(location.getLatitude() - 0.01, location.getLongitude() - 0.01),
                                        new LatLng(location.getLatitude() + 0.01, location.getLongitude() + 0.01)
                                ))
                                .build();

                        // ... existing code to find place predictions ...


                        // Use PlacesClient to find place predictions
                        PlacesClient placesClient = Places.createClient(this);
                        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                            // Extract predicted place names
                            List<String> predictedPlaces = new ArrayList<>();
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                predictedPlaces.add(String.valueOf(prediction.getFullText(null)));
                            }
                        /*
                            // Choose the first prediction for demonstration (you can choose based on user interaction)
                            String selectedPlace = predictedPlaces.get(0);

                            // Extract the place ID from the selected prediction
                            AutocompletePrediction selectedPrediction = response.getAutocompletePredictions().get(0); // Get the first prediction
                            String selectedPlaceId = String.valueOf(selectedPrediction.getPlaceId());
                        */

                            placeAdapter.clear();
                            placeAdapter.addAll(predictedPlaces);
                            placeAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView

                            // Optionally, handle the ListView item clicks to fetch more details about the selected place
                            shopListView.setOnItemClickListener((parent, view, position, id) -> {
                                String selectedPlaceId = response.getAutocompletePredictions().get(position).getPlaceId();
                                // Call the searchForPlaces method to fetch details of the selected place
                                searchForPlaces(selectedPlaceId);
                            });
                        }).addOnFailureListener((exception) -> {
                            // Handle errors
                            Log.e("User_main", "Error finding predictions: " + exception.getMessage());
                        });
                    }
                });
    }
            private void searchForPlaces(String selectedPlaceId) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else {
                    // Location permission is already granted
                    Log.d("User_main", "Location permission is granted. Fetching place details.");

                    // Create a FetchPlaceRequest for the selected place ID
                    FetchPlaceRequest request = FetchPlaceRequest.newInstance(selectedPlaceId, Arrays.asList(Place.Field.values()));

                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                        Place place = response.getPlace();

                        // Extract and display shop info
                        String shopName = place.getName();
                        String shopAddress = place.getAddress();
                        String shopNumber = place.getPhoneNumber();

                        // Update UI or display info here, e.g.,
                        Log.d("User_main", "Shop name: " + shopName);
                        Log.d("User_main", "Shop address: " + shopAddress);
                        Log.d("User_main", "Shop number: " + shopNumber);
                        // ... display info in your UI elements
                    }).addOnFailureListener((exception) -> {
                        // Handle errors
                        Log.e("User_main", "Error fetching place details: " + exception.getMessage());
                    });
                }
            }

}

