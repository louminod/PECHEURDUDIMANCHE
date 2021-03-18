package com.hexa.pecheur_du_dimanche.api.localisation.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.models.Adresse;
import com.hexa.pecheur_du_dimanche.models.EnvironmentalCondition;
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
import java.util.logging.Logger;

public class ReverseLocationTask extends AsyncTask<String, Void, Adresse> {

    @Override
    protected Adresse doInBackground(String... params) {
        Adresse adresse = null;
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.API_ADRESSE_URL + "?lon=" + params[0] + "&lat=" + params[1]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            adresse = new Adresse((new JSONObject(httpResult)).getJSONArray("features").getJSONObject(0).getJSONObject("properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adresse;
    }
}