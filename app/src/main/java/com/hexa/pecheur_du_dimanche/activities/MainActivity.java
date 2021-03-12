package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hexa.pecheur_du_dimanche.R;
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
        buttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Station> stationList = WaterTempApi.stationsForDepartment("94");
                stationList.forEach(station -> {
                    station.getChroniqueList().forEach(chronique -> {
                        Log.i(station.getLibelleStation(), String.valueOf(chronique.getResultat()));
                    });
                });
            }
        });
    }
}