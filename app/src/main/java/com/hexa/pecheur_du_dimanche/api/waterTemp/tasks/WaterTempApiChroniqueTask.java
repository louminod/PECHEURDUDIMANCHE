package com.hexa.pecheur_du_dimanche.api.waterTemp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.models.Chronique;
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

public class WaterTempApiChroniqueTask extends AsyncTask<String, Void, List<Chronique>> {

    @Override
    protected List<Chronique> doInBackground(String... params) {
        List<Chronique> chroniqueList = new ArrayList<>();
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.WATER_TEMP_CHRONIQUE_URL + "?code_station=" + params[0] + "&sort=desc&size=2");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            JSONArray chroniques = (new JSONObject(httpResult)).getJSONArray("data");
            for (int j = 0; j < chroniques.length(); j++) {
                JSONObject jsonChronique = chroniques.getJSONObject(j);
                chroniqueList.add(new Chronique(jsonChronique));
            }

            // Log.i("JSON", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chroniqueList;
    }
}
