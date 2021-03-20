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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    private Adresse currentAddress;
    private Context context;

    public static List<Station> stations;
    public static HashMap<String, String> markerMap;
    public static boolean loadDone;

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (currentAddress == null) {
                super.onLocationResult(locationResult);
                locationResult.getLastLocation();
                currentLocation = locationResult.getLastLocation();
                currentAddress = APIAdresse.reverseLocation(currentLocation);
                if (firstTimeFlag && googleMap != null) {
                    animateCamera(currentLocation);
                    firstTimeFlag = false;
                }
                showLocationMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
            }

            if (currentAddress != null && !MapActivity.loadDone) {
                // MapActivity.stations = WaterTempApi.stationsForDepartment(currentAddress.getPostcode().substring(0, 2));
                new Thread(() -> {
                    runOnUiThread(() -> Toast.makeText(context, "Chargement des stations...", Toast.LENGTH_LONG).show());
                    MapActivity.stations = WaterTempApi.stations();
                    runOnUiThread(() -> Toast.makeText(context, "Chargement terminé", Toast.LENGTH_SHORT).show());

                    String lastStationToLoad = MapActivity.stations.get(MapActivity.stations.size() - 1).getCodeStation();

                    MapActivity.stations.forEach(station -> {
                        if (!station.isLoaded()) {
                            new Thread(() -> {
                                // WaterTempApi.fetchStationChronique(station);
                                // WaterQualityApi.fetchStationEnvironmentalCondition(station);
                                station.setLoaded(true);
                                if (station.getCodeStation().equals(lastStationToLoad)) {
                                    MapActivity.loadDone = true;
                                }
                            }).start();
                            runOnUiThread(() -> showStationMarker(station));
                        }
                    });
                }).start();
            }
        }
    };

    private void showLocationMarker(double latitude, double longitude) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.fisher);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        LatLng latLng = new LatLng(latitude, longitude);
        String id = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title("C'est vous !").icon(BitmapDescriptorFactory.fromBitmap(icon))).getId();
        markerMap.put(id, "C'est vous !");
    }

    private void showStationMarker(Station station) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.station);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
        String id = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title(station.getCodeStation() + " - " + station.getLibelleCommune()).icon(BitmapDescriptorFactory.fromBitmap(icon))).getId();
        markerMap.put(id, station.getCodeStation());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapActivity.stations = new ArrayList<>();
        MapActivity.markerMap = new HashMap<>();
        MapActivity.loadDone = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);

        findViewById(R.id.currentLocationImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null) {
                    MapActivity.this.animateCamera(currentLocation);
                }
            }
        });

        EditText etZip = findViewById(R.id.etZipCode);

        etZip.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etZip.getRight() - etZip.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (MapActivity.stations != null) {
                        boolean founded = false;
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
                        if (etZip.getText() != null && !founded) {
                            Toast.makeText(context, "Aucune station pour ce département !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
            }
            return false;
        });

        this.context = MapActivity.this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        CustomInfo customInfo = new CustomInfo(this);
        this.googleMap.setInfoWindowAdapter(customInfo);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerId = markerMap.get(marker.getId());
                if (!markerId.equals("C'est vous !")) {
                    Station station = findStationByCode(markerId);
                    // DO NOTHING
                }
                return false;
            }
        });
        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String stationCode = markerMap.get(marker.getId());
                Station station = findStationByCode(stationCode);
                if (station.isLoaded()) {
                    Intent intent = new Intent(MapActivity.this, StationActivity.class);
                    intent.putExtra("station", station);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "La station n'a pas encore fini de charger", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(600000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

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