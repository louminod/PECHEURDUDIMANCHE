package com.hexa.pecheur_du_dimanche.api.hydrometry.tasks;

import android.os.AsyncTask;

import com.hexa.pecheur_du_dimanche.models.EnvironmentalCondition;
import com.hexa.pecheur_du_dimanche.models.HydrometryObservation;
import com.hexa.pecheur_du_dimanche.tools.Converters;
import com.hexa.pecheur_du_dimanche.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WaterHydrometryObservationsTask extends AsyncTask<String, Void, List<HydrometryObservation>> {

    @Override
    protected List<HydrometryObservation> doInBackground(String... params) {
        List<HydrometryObservation> hydrometryObservationList = new ArrayList<>();
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.WATER_HYDROMETRY_OBSERVATIONS_URL + "?code_station=" + params[0] + "&sort=desc&size=5");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            JSONArray array = (new JSONObject(httpResult)).getJSONArray("data");
            for (int j = 0; j < array.length(); j++) {
                JSONObject json = array.getJSONObject(j);
                hydrometryObservationList.add(new HydrometryObservation(json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hydrometryObservationList;
    }
}