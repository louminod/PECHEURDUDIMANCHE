package com.hexa.pecheur_du_dimanche.api.fishState;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.fishState.tasks.WaterFishStateTask;
import com.hexa.pecheur_du_dimanche.models.Station;

public abstract class WaterFishStateApi {
    /**
     * Fill a station with fish information
     * @param station
     */
    public static void fetchStationFishState(Station station) {
        try {
            // Create and execute the task
            WaterFishStateTask waterFishStateTask = new WaterFishStateTask();
            station.setFishStateList(waterFishStateTask.execute(station.getCodeStation()).get());
        } catch (Exception exception) {
            Log.e("fetchStationFishState", exception.getMessage());
        }
    }
}
