package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.models.Chronique;
import com.hexa.pecheur_du_dimanche.models.EnvironmentalCondition;
import com.hexa.pecheur_du_dimanche.models.FishState;
import com.hexa.pecheur_du_dimanche.models.HydrometryObservation;
import com.hexa.pecheur_du_dimanche.models.Station;
import com.hexa.pecheur_du_dimanche.services.LocalPersistence;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity display the information of a station.
 */
public class StationActivity extends AppCompatActivity {

    // Attributes of the activity
    private Context context;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        // Hide the actionBar
        getSupportActionBar().hide();

        // Define the attributes
        this.context = StationActivity.this;
        this.station = (Station) getIntent().getSerializableExtra("station");

        // Check the persistence
        this.checkPersistence();

        // Load the station data from the APIs
        this.loadData();

        // Load the custom navBar
        this.loadNavBar();

        // Load the contence
        this.loadContent();
    }

    /**
     * Bind the elements from the view and display the information
     */
    private void loadContent() {
        ((TextView) findViewById(R.id.tvDepartment)).setText(String.format("Département: %s", station.getLibelleDepartement()));
        ((TextView) findViewById(R.id.tvCoursEau)).setText(String.format("Cours d'eau: %s", station.getLibelleCoursEau()));
        ((TextView) findViewById(R.id.tvCodeStation)).setText(String.format("Code station: %s", station.getCodeStation()));
        ((TextView) findViewById(R.id.tvDateMajStation)).setText(String.format("Mise à jour: %s", station.getDateMajInfos()));

        HydrometryObservation hydrometryObservationLast = station.getHydrometryObservationList().get(0);
        ((TextView) findViewById(R.id.tvHydroDate)).setText(hydrometryObservationLast.getDateObs().toString().replace("T", " "));
        ((TextView) findViewById(R.id.tvHydroResults)).setText(String.format("Resultat: %s", hydrometryObservationLast.getResultatObs()));
        ((TextView) findViewById(R.id.tvHydroMethod)).setText(String.format("Méthode: %s", hydrometryObservationLast.getCodeMethodeObs()));

        EnvironmentalCondition environmentalConditionLast = station.getEnvironmentalConditionList().get(0);
        ((TextView) findViewById(R.id.tvCondDate)).setText(environmentalConditionLast.getDatePrelevement().toString().replace("T", " "));
        ((TextView) findViewById(R.id.tvCondUnite)).setText(String.format("Unite: %s %s", environmentalConditionLast.getCodeUnite(), environmentalConditionLast.getLibelleUnite()));
        ((TextView) findViewById(R.id.tvCondQualification)).setText(String.format("Qualification: %s", environmentalConditionLast.getLibelleQualification()));

        Chronique chroniqueLast = station.getChroniqueList().get(0);
        ((TextView) findViewById(R.id.tvChroDate)).setText(chroniqueLast.getDateHeureMesure().toString().replace("T", " "));
        ((TextView) findViewById(R.id.tvChroUnite)).setText(String.format("Temp eau: %s %s", chroniqueLast.getCodeUnite(), chroniqueLast.getSymboleUnite()));
        ((TextView) findViewById(R.id.tvChroQualif)).setText(String.format("Qualification: %s", chroniqueLast.getLibelleQualification()));

        List<FishState> fishStates = station.getFishStateList();
        ListView listView = (ListView) findViewById(R.id.lvFish);
        List<String> items = new ArrayList<>();
        if (fishStates.size() > 0) {
            for (FishState fishState : fishStates) {
                items.add(fishState.getNomPoisson());
                Log.i("-----", fishState.getNomPoisson());
            }
        } else {
            items.add("Aucune donnée piscicole pour cette station");
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
        listView.setAdapter(adapter);

    }

    /**
     * Fetch data from the APIs and fill the station with it.
     */
    private void loadData() {
        WaterTempApi.fetchStationChronique(station);
        WaterQualityApi.fetchStationEnvironmentalCondition(station);
        WaterHydrometryApi.fetchStationHydrometry(station);
        WaterFishStateApi.fetchStationFishState(station);
        Toast.makeText(context, "Station loaded", Toast.LENGTH_SHORT).show();
    }

    /**
     * Load the custom navBar
     */
    private void loadNavBar() {
        ((TextView) findViewById(R.id.stationCity)).setText(station.getLibelleCommune());
        findViewById(R.id.backButton).setOnClickListener(value -> finish());

        ImageButton saveButton = findViewById(R.id.saveButton);

        // Check if the station is marked as a favorite and display a specific heart
        if (this.station.isFavorite()) {
            saveButton.setImageResource(R.drawable.save_full);
        } else {
            saveButton.setImageResource(R.drawable.save_empty);
        }
        saveButton.setOnClickListener((event) -> {
            this.station.setFavorite(!this.station.isFavorite());

            // Save or unsave the station and change the button image
            if (this.station.isFavorite()) {
                LocalPersistence.saveStation(context, this.station);
                saveButton.setImageResource(R.drawable.save_full);
            } else {
                LocalPersistence.unsaveStation(context, this.station);
                saveButton.setImageResource(R.drawable.save_empty);
            }
        });
    }

    // Check if the station exist in the persistence and replace it
    private void checkPersistence() {
        for (Station s : LocalPersistence.getStations(context)) {
            if (this.station.getCodeStation().equals(s.getCodeStation())) {
                this.station = s;
                break;
            }
        }
    }
}