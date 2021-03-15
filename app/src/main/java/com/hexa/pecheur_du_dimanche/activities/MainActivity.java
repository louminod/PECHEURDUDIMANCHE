package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiStationsTask;
import com.hexa.pecheur_du_dimanche.models.Station;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonTest = findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(value -> {
            List<Station> stationList = WaterTempApi.stationsForDepartment("94");
            stationList.forEach(station -> new Thread(apiFetch(station)).start());
        });
    }

    private Runnable apiFetch(Station station) {
        return () -> {
            WaterTempApi.fetchStationChronique(station);
            WaterQualityApi.fetchStationEnvironmentalCondition(station);
            Log.i("MAIN", String.format("Station %s filled", station.getCodeStation()));
        };
    }
}