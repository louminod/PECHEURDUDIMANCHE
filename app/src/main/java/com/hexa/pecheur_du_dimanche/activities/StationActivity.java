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

        this.loadData();
        this.checkPersistence();
        this.loadNavBar();
        this.loadContent();
    }

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

    private void loadData() {
        WaterTempApi.fetchStationChronique(station);
        WaterQualityApi.fetchStationEnvironmentalCondition(station);
        WaterHydrometryApi.fetchStationHydrometry(station);
        WaterFishStateApi.fetchStationFishState(station);
        Toast.makeText(context, "Station loaded", Toast.LENGTH_SHORT).show();
    }

    private void loadNavBar() {
        ((TextView) findViewById(R.id.stationCity)).setText(station.getLibelleCommune());
        findViewById(R.id.backButton).setOnClickListener(value -> finish());

        ImageButton saveButton = findViewById(R.id.saveButton);
        if (this.station.isFavorite()) {
            saveButton.setImageResource(R.drawable.save_full);
        } else {
            saveButton.setImageResource(R.drawable.save_empty);
        }
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
    }

    private void checkPersistence() {
        List<Station> stations = LocalPersistence.getStations(context);

        for (Station s : LocalPersistence.getStations(context)) {
            if (this.station.getCodeStation().equals(s.getCodeStation())) {
                this.station = s;
                break;
            }
        }
    }
}