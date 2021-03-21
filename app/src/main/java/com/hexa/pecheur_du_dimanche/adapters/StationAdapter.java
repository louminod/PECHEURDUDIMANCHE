package com.hexa.pecheur_du_dimanche.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.models.Station;

import java.util.ArrayList;

/**
 * This adapter is used to display stations in the listView of the mainActivity
 */
public class StationAdapter extends ArrayAdapter<Station> {
    public StationAdapter(Context context, ArrayList<Station> stations) {
        super(context, 0, stations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the station for this position
        Station station = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_station, parent, false);
        }

        // Set the elements
        TextView tvName = (TextView) convertView.findViewById(R.id.tvCommune);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvWaterName);

        // Populate the data into the template view using the station
        tvName.setText(station.getLibelleCommune());
        tvHome.setText(station.getLibelleCoursEau());

        // Return the completed view to render on screen
        return convertView;
    }
}
