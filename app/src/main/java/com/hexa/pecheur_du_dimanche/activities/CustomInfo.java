package com.hexa.pecheur_du_dimanche.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.models.Station;

import java.util.HashMap;
import java.util.List;

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

        TextView city = view.findViewById(R.id.infoCity);
        TextView waterName = view.findViewById(R.id.infoWaterName);
        TextView height = view.findViewById(R.id.infoHeight);
        TextView waterTemp = view.findViewById(R.id.infoWaterTemp);
        TextView waterQuality = view.findViewById(R.id.infoWaterQuality);

        String stationCode = MapActivity.markerMap.get(marker.getId());
        Station station = findStationByCode(stationCode);

        if (station != null) {
            city.setText(station.getLibelleCommune());
            waterName.setText(station.getLibelleCoursEau());
            height.setText(String.format("altitude: %.1f m", station.getAltitude()));
            if (station.getChroniqueList() != null) {
                waterTemp.setText(String.format("eau: %.1f °C", station.getChroniqueList().get(0).getResultat()));
            }
            if (station.getEnvironmentalConditionList() != null) {
                waterQuality.setText(String.format("qualité eau: %s",station.getEnvironmentalConditionList().get(0).getLibelleQualification()));
            }
        } else {
            if(stationCode.equals("C'est vous !")) {
                city.setText(stationCode);
            } else {
                city.setText("Chargement...");
            }
        }
        return view;
    }

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
