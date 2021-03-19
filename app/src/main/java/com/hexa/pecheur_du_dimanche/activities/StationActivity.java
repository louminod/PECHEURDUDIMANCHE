package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.models.Station;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class StationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        getSupportActionBar().hide();

        Station station = (Station) getIntent().getSerializableExtra("station");

        TextView city = findViewById(R.id.stationCity);
        ImageButton saveButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.backButton);

        city.setText(station.getLibelleCommune());

        backButton.setOnClickListener(value -> finish());
    }
}