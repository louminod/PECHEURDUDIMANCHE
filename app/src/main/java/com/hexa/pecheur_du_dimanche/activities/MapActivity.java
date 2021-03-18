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
import com.hexa.pecheur_du_dimanche.models.Adresse;
import com.hexa.pecheur_du_dimanche.models.Station;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    private Adresse currentAddress;
    private List<Station> stations;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null) {
                MapActivity.this.animateCamera(currentLocation);
            }
        }
    };

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            locationResult.getLastLocation();
            currentLocation = locationResult.getLastLocation();
            currentAddress = APIAdresse.reverseLocation(currentLocation);
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            showLocationMarker(currentLocation.getLatitude(), currentLocation.getLongitude(), "C'est vous !");

            if (currentAddress != null) {
                stations = WaterTempApi.stationsForDepartment(currentAddress.getPostcode().substring(0, 2));
                //stations = WaterTempApi.stations();
            }

            stations.forEach(station -> {
                showStationMarker(station);
            });
        }
    };

    private void showLocationMarker(double latitude, double longitude, String title) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.fisher);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(icon)));
    }

    private void showStationMarker(Station station) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.station);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 150, 150, false);

        LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng).title(station.getLibelleCommune()).icon(BitmapDescriptorFactory.fromBitmap(icon)));
    }

    private Runnable apiFetch(Station station) {
        return () -> {
            WaterTempApi.fetchStationChronique(station);
            WaterQualityApi.fetchStationEnvironmentalCondition(station);
            WaterHydrometryApi.fetchStationHydrometry(station);
            WaterFishStateApi.fetchStationFishState(station);
            Log.i("MAIN", String.format("Station %s filled", station.getCodeStation()));
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);
        findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                String markerId = marker.getId();

                Toast.makeText(MapActivity.this, "Clicked location is " + markerId, Toast.LENGTH_SHORT).show();
                return false;
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
}