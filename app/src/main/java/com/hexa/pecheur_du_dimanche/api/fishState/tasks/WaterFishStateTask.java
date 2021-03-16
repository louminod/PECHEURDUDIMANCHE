package com.hexa.pecheur_du_dimanche.api.fishState.tasks;

import android.os.AsyncTask;

import com.hexa.pecheur_du_dimanche.models.FishState;
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

public class WaterFishStateTask extends AsyncTask<String, Void, List<FishState>> {

    @Override
    protected List<FishState> doInBackground(String... params) {
        List<FishState> fishStateList = new ArrayList<>();
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.WATER_FISH_STATE_URL + "?code_station=" + params[0] + "&sort=desc&size=5");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            JSONArray array = (new JSONObject(httpResult)).getJSONArray("data");
            for (int j = 0; j < array.length(); j++) {
                JSONObject json = array.getJSONObject(j);
                fishStateList.add(new FishState(json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fishStateList;
    }
}