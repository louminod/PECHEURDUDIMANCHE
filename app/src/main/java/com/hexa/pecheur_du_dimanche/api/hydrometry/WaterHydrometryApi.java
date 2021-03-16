package com.hexa.pecheur_du_dimanche.api.hydrometry;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.hydrometry.tasks.WaterHydrometryObservationsTask;
import com.hexa.pecheur_du_dimanche.api.waterTemp.tasks.WaterTempApiChroniqueTask;
import com.hexa.pecheur_du_dimanche.models.Station;

public abstract class WaterHydrometryApi {
    public static void fetchStationHydrometry(Station station) {
        try {
            WaterHydrometryObservationsTask hydrometryObservationsTask = new WaterHydrometryObservationsTask();
            station.setHydrometryObservationList(hydrometryObservationsTask.execute(station.getCodeStation()).get());
        } catch (Exception exception) {
            Log.e("fetchStationHydrometry", exception.getMessage());
        }
    }
}
