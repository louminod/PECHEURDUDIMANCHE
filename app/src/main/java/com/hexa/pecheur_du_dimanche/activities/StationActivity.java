package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.models.Station;
import com.hexa.pecheur_du_dimanche.services.LocalPersistence;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {

    private Context context;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        getSupportActionBar().hide();

        this.context = StationActivity.this;

        this.station = (Station) getIntent().getSerializableExtra("station");

        List<Station> stations = LocalPersistence.getStations(context);
        Log.i("STATIONS", String.valueOf(stations.size()));


        for (Station s : LocalPersistence.getStations(context)) {
            Log.i("STATIONS", s.getCodeStation());
            if (this.station.getCodeStation().equals(s.getCodeStation())) {
                this.station = s;
                break;
            }
        }

        TextView city = findViewById(R.id.stationCity);
        ImageButton saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        if (this.station.isFavorite()) {
            saveButton.setImageResource(R.drawable.save_full);
        } else {
            saveButton.setImageResource(R.drawable.save_empty);
        }

        city.setText(station.getLibelleCommune());
        backButton.setOnClickListener(value -> finish());

        saveButton.setOnClickListener((event) -> {
            this.station.setFavorite(!this.station.isFavorite());
            if (this.station.isFavorite()) {
                LocalPersistence.saveStation(context, this.station);
                saveButton.setImageResource(R.drawable.save_full);
            } else {
                LocalPersistence.unsaveStation(context, this.station);
                saveButton.setImageResource(R.drawable.save_empty);
            }
        });

        WaterTempApi.fetchStationChronique(station);
        WaterQualityApi.fetchStationEnvironmentalCondition(station);
        WaterHydrometryApi.fetchStationHydrometry(station);
        WaterFishStateApi.fetchStationFishState(station);
        Toast.makeText(context, "Station chargee", Toast.LENGTH_SHORT).show();


    }
}