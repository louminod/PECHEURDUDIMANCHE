package com.hexa.pecheur_du_dimanche.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.localisation.APIAdresse;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.layouts.CustomInfo;
import com.hexa.pecheur_du_dimanche.models.Adresse;
import com.hexa.pecheur_du_dimanche.models.Station;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity is responsible of display a map with the user position and all the known stations.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Google map service
    private GoogleMap googleMap;

    // First time opening the map
    private boolean firstTimeFlag = true;

    // The current user position (converted as an address)
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Adresse currentAddress;

    // The application context
    private Context context;

    // Static attributes allowing access from everywhere
    public static List<Station> stations;
    public static HashMap<String, String> markerMap;
    public static boolean loadDone;

    /**
     * This callback is triggered by a location change. It will reload the current location and the stations.
     */
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // Get the current location and convert it to an address
            if (currentAddress == null) {
                super.onLocationResult(locationResult);
                locationResult.getLastLocation();
                currentLocation = locationResult.getLastLocation();

                // Use an API to reverse the location to an address
                currentAddress = APIAdresse.reverseLocation(currentLocation);

                // If it is the first map opening, display the current location with an animation
                if (firstTimeFlag && googleMap != null) {
                    animateCamera(currentLocation);
                    firstTimeFlag = false;
                }

                // Display a marker on the map
                showLocationMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
            }

            // If there is a currant address and the stations are not loaded yet
            if (currentAddress != null && !MapActivity.loadDone) {

                // Create a thread responsible of loading the stations
                new Thread(() -> {
                    // Load the stations from the API
                    runOnUiThread(() -> Toast.makeText(context, "Chargement des stations...", Toast.LENGTH_LONG).show());
                    MapActivity.stations = WaterTempApi.stations();
                    runOnUiThread(() -> Toast.makeText(context, "Chargement terminé", Toast.LENGTH_SHORT).show());

                    // Show each station on the map
                    MapActivity.stations.forEach(station -> {
                        if (!station.isLoaded()) {
                            station.setLoaded(true);
                            runOnUiThread(() -> showStationMarker(station));
                        }
                    });

                    // Set the loading state to done
                    MapActivity.loadDone = true;
                }).start();
            }
        }
    };

    // Show the user location marker
    private void showLocationMarker(double latitude, double longitude) {
        // Set a specific icon for the marker
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.fisher);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        // Display the marker
        LatLng latLng = new LatLng(latitude, longitude);
        String id = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("C'est vous !").icon(BitmapDescriptorFactory.fromBitmap(icon))).getId();
        markerMap.put(id, "C'est vous !");
    }

    // Show a station on the map
    private void showStationMarker(Station station) {
        // Set a specific icon for the marker
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.station);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        // Display the marker
        LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
        String id = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title(station.getCodeStation() + " - " + station.getLibelleCommune()).icon(BitmapDescriptorFactory.fromBitmap(icon))).getId();
        markerMap.put(id, station.getCodeStation());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Hide the actionBar
        getSupportActionBar().hide();

        // Save the context
        this.context = MapActivity.this;

        // Initialize the statics variables
        MapActivity.stations = new ArrayList<>();
        MapActivity.markerMap = new HashMap<>();
        MapActivity.loadDone = false;

        // Bind the map fragment with the activity
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);

        // Add an action on the imageButton click
        findViewById(R.id.currentLocationImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null) {
                    // Relocate the view on the current user location
                    MapActivity.this.animateCamera(currentLocation);
                }
            }
        });

        // Bind the department research bar and add an action
        EditText etZip = findViewById(R.id.etZipCode);
        etZip.setOnTouchListener((v, event) -> {
            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            // Check if the drawable is clicked
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etZip.getRight() - etZip.getCompoundDrawables()[2].getBounds().width())) {
                    if (MapActivity.stations != null) {
                        boolean founded = false;

                        // Check if it exist a station for the entered zip code and show it if yes
                        for (Station station : MapActivity.stations) {
                            if (station.getCodeDepartement().equals(etZip.getText().toString())) {
                                Location location = new Location("");
                                location.setLatitude(station.getLatitude());
                                location.setLongitude(station.getLongitude());
                                MapActivity.this.animateCamera(location);
                                founded = true;
                                break;
                            }
                        }
                        // If there is no station, display a message
                        if (etZip.getText() != null && !founded) {
                            Toast.makeText(context, "Aucune station pour ce département !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Define a custom infoView to be displayed on a marker click
        CustomInfo customInfo = new CustomInfo(this);
        this.googleMap.setInfoWindowAdapter(customInfo);

        // Add an action on the click
        this.googleMap.setOnInfoWindowClickListener(marker -> {
            // Get the marker
            String markerId = markerMap.get(marker.getId());

            // If the marker is not the user marker
            if (!markerId.equals("C'est vous !")) {
                // Get the matching station
                Station station = findStationByCode(markerId);

                // If the station has been loaded display the station activity
                if (station.isLoaded()) {
                    Intent intent = new Intent(MapActivity.this, StationActivity.class);
                    intent.putExtra("station", station);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "La station n'a pas encore fini de charger", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Vous n'êtes pas une station !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Create a trigger on user location updated (each 10 minutes).
     * Add a callback on location change
     */
    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(600000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    /**
     * Check if the google play service is available for google maps correct usage.
     *
     * @return true or false
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Install google play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    /**
     * Make an animation with the camera going to a specified location.
     *
     * @param location
     */
    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        googleMap = null;
    }

    /**
     * Find a station by is code.
     *
     * @param code The station code
     * @return The corresponding station
     */
    private Station findStationByCode(String code) {
        Station result = null;
        for (Station station : MapActivity.stations) {
            if (station.getCodeStation().equals(code)) {
                result = station;
            }
        }
        return result;
    }
}