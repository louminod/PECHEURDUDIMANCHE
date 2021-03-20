package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.models.Station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StationActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        getSupportActionBar().hide();

        this.context = StationActivity.this;

        Station station = (Station) getIntent().getSerializableExtra("station");

        TextView city = findViewById(R.id.stationCity);
        ImageButton saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        city.setText(station.getLibelleCommune());
        backButton.setOnClickListener(value -> finish());

        new Thread(() -> {
            WaterHydrometryApi.fetchStationHydrometry(station);
            WaterFishStateApi.fetchStationFishState(station);
            Toast.makeText(context, "Station chargee", Toast.LENGTH_SHORT).show();
        }).run();


    }
}