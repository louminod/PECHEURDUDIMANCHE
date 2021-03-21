package com.hexa.pecheur_du_dimanche.api.fishState;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.fishState.tasks.WaterFishStateTask;
import com.hexa.pecheur_du_dimanche.models.Station;

public abstract class WaterFishStateApi {
    public static void fetchStationFishState(Station station) {
        try {
            WaterFishStateTask waterFishStateTask = new WaterFishStateTask();
            station.setFishStateList(waterFishStateTask.execute(station.getCodeStation()).get());
            Log.i("here", String.valueOf(station.getFishStateList().size()));
        } catch (Exception exception) {
            Log.e("fetchStationFishState", exception.getMessage());
        }
    }
}
