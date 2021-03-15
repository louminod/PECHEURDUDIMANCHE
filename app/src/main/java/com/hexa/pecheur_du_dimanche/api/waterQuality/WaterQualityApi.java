package com.hexa.pecheur_du_dimanche.api.waterQuality;

import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.waterQuality.tasks.WaterQualityApiEnvCondTask;
import com.hexa.pecheur_du_dimanche.models.Station;

public abstract class WaterQualityApi {
    public static void fetchStationEnvironmentalCondition(Station station) {
        try {
            WaterQualityApiEnvCondTask envCondTask = new WaterQualityApiEnvCondTask();
            station.setEnvironmentalConditionList(envCondTask.execute(station.getCodeStation()).get());
        } catch (Exception exception) {
            Log.e("fetchStationEnvironmentalCondition", exception.getMessage());
        }
    }
}
