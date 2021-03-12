package com.hexa.pecheur_du_dimanche.api.waterTemp;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiChroniqueTask;
import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiStationsTask;
import com.hexa.pecheur_du_dimanche.models.Chronique;
import com.hexa.pecheur_du_dimanche.models.Station;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class WaterTempApi {
    public static List<Station> stationsForDepartment(String department) {
        List<Station> stationList = new ArrayList<>();
        try {
            WaterTempApiStationsTask stationsTask = new WaterTempApiStationsTask();
            JSONArray stations = stationsTask.execute(department).get().getJSONArray("data");

            for (int i = 0; i < stations.length(); i++) {
                JSONObject jsonStation = stations.getJSONObject(i);
                Station station = new Station(jsonStation);

                WaterTempApiChroniqueTask chroniqueTask = new WaterTempApiChroniqueTask();
                JSONArray chroniques = chroniqueTask.execute(station.getCodeStation()).get().getJSONArray("data");
                List<Chronique> chroniqueList = new ArrayList<>();

                for (int j = 0; j < chroniques.length(); j++) {
                    JSONObject jsonChronique = chroniques.getJSONObject(i);
                    chroniqueList.add(new Chronique(jsonChronique));
                }

                station.setChroniqueList(chroniqueList);
                stationList.add(station);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationList;
    }
}
