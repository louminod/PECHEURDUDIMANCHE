package com.hexa.pecheur_du_dimanche.api.waterTemp;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiChroniqueTask;
import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiStationsTask;
import com.hexa.pecheur_du_dimanche.models.Station;


import java.util.ArrayList;
import java.util.List;

public abstract class WaterTempApi {
    public static List<Station> stationsForDepartment(String department) {
        List<Station> stations = new ArrayList<>();
        try {
            WaterTempApiStationsTask stationsTask = new WaterTempApiStationsTask();
            stations = stationsTask.execute(department).get();
        } catch (Exception exception) {
            Log.e("stationsForDepartment", exception.getMessage());
        }
        return stations;
    }

    public static List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        try {
            WaterTempApiStationsTask stationsTask = new WaterTempApiStationsTask();
            stations = stationsTask.execute().get();
        } catch (Exception exception) {
            Log.e("stations", exception.getMessage());
        }
        return stations;
    }

    public static void fetchStationChronique(Station station) {
        try {
            WaterTempApiChroniqueTask chroniqueTask = new WaterTempApiChroniqueTask();
            station.setChroniqueList(chroniqueTask.execute(station.getCodeStation()).get());
        } catch (Exception exception) {
            Log.e("fetchStationChronique", exception.getMessage());
        }
    }
}
