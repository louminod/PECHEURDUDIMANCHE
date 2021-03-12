package com.hexa.pecheur_du_dimanche.api.waterTemp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.tools.Converters;
import com.hexa.pecheur_du_dimanche.utils.Constants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WaterTempApiStationsTask extends AsyncTask<String, Void, JSONObject> {
    private JSONObject result;

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.WATER_TEMP_STATIONS_URL + "?code_departement=" + params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            // Convert the String result to a JSON
            result = new JSONObject(httpResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
