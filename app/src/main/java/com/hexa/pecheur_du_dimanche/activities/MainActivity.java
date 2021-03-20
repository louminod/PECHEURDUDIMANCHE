package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.adapters.StationAdapter;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.models.Station;
import com.hexa.pecheur_du_dimanche.services.LocalPersistence;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button buttonGo = findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(value ->
                startActivity(new Intent(MainActivity.this, MapActivity.class))
        );

       this.loadStations();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.loadStations();
    }

    private void loadStations() {
        this.stations = LocalPersistence.getStations(MainActivity.this);

        ListView listView = findViewById(R.id.listStations);
        TextView textViewInfo = findViewById(R.id.tvInfo);

        if(this.stations.size() == 0){
            textViewInfo.setText("Aucune station favorite");
        } else {
            textViewInfo.setText("Stations favorites");
        }

        StationAdapter adapter = new StationAdapter(this, (ArrayList<Station>) stations);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Station station = (Station) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, StationActivity.class);
            intent.putExtra("station", station);
            startActivity(intent);
        });
    }
}
