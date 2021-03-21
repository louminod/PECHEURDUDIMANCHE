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

/**
 * This is the first activity of the application. It is composed of the list of the favorites stations, the logo and a button to open the map.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the actionBar
        getSupportActionBar().hide();

        // Bind the button and define an action on click
        Button buttonGo = findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(value ->
                startActivity(new Intent(MainActivity.this, MapActivity.class))
        );

        // Load the stations
        this.loadStations();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Load the stations
        this.loadStations();
    }

    /**
     * Load the stations marked as favorites from the persistence system.
     */
    private void loadStations() {
        // Get the saved stations
        List<Station> stations = LocalPersistence.getStations(MainActivity.this);

        // Bind the view elements
        ListView listView = findViewById(R.id.listStations);
        TextView textViewInfo = findViewById(R.id.tvInfo);

        // Check if there are saved stations
        if (stations.size() == 0) {
            textViewInfo.setText("Aucune station favorite");
        } else {
            textViewInfo.setText("Stations favorites");
        }

        // Create a specific adapter for display a custom item
        StationAdapter adapter = new StationAdapter(this, (ArrayList<Station>) stations);
        listView.setAdapter(adapter);

        // Add a listener to open a new activity on an item click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected station
            Station station = (Station) parent.getItemAtPosition(position);

            // Create the intent, add the station and start a new activity
            Intent intent = new Intent(MainActivity.this, StationActivity.class);
            intent.putExtra("station", station);
            startActivity(intent);
        });
    }
}
