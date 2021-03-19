package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.models.Station;

import android.os.Bundle;
import android.util.Log;

public class StationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        getSupportActionBar().hide();

        Station station = (Station) getIntent().getSerializableExtra("station");
        Log.i("INFO", station.getCodeStation());
    }
}