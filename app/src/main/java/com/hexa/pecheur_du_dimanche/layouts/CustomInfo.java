package com.hexa.pecheur_du_dimanche.layouts;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.activities.MapActivity;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.models.Station;

import java.util.HashMap;
import java.util.List;

/**
 * Used to display a custom info view above a marker.
 */
public class CustomInfo implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfo(Context ctx) {
        this.context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_info, null);

        // Bind the element of the view
        TextView city = view.findViewById(R.id.infoCity);
        TextView waterName = view.findViewById(R.id.infoWaterName);
        TextView height = view.findViewById(R.id.infoHeight);
        TextView waterTemp = view.findViewById(R.id.infoWaterTemp);
        TextView waterQuality = view.findViewById(R.id.infoWaterQuality);

        // Get the station
        String stationCode = MapActivity.markerMap.get(marker.getId());
        Station station = findStationByCode(stationCode);

        if (station != null) {
            // Get specific data
            WaterTempApi.fetchStationChronique(station);
            WaterQualityApi.fetchStationEnvironmentalCondition(station);

            // Display the data
            city.setText(station.getLibelleCommune());
            waterName.setText(station.getLibelleCoursEau());
            height.setText(String.format("altitude: %.1f m", station.getAltitude()));

            // Display chronique data
            if (station.getChroniqueList() != null && station.getChroniqueList().size() > 0) {
                waterTemp.setText(String.format("eau: %.1f °C", station.getChroniqueList().get(0).getResultat()));
            } else {
                waterTemp.setText("eau: chargement...");
            }

            // Display environmental conditions data
            if (station.getEnvironmentalConditionList() != null && station.getEnvironmentalConditionList().size() > 0) {
                waterQuality.setText(String.format("qualité eau: %s", station.getEnvironmentalConditionList().get(0).getLibelleQualification()));
            } else {
                waterQuality.setText("qualité eau: chargement...");
            }
        } else {
            if (stationCode.equals("C'est vous !")) {
                city.setText(stationCode);
            } else {
                city.setText("Chargement...");
            }
        }
        return view;
    }

    /**
     * Find a station by is code.
     *
     * @param code The station code
     * @return The corresponding station
     */
    private Station findStationByCode(String code) {
        Station result = null;
        if (MapActivity.stations.size() > 0) {
            for (Station station : MapActivity.stations) {
                if (station.getCodeStation().equals(code)) {
                    result = station;
                }
            }
        }
        return result;
    }
}
