package com.hexa.pecheur_du_dimanche.api.waterQuality.tasks;

import android.os.AsyncTask;

import com.hexa.pecheur_du_dimanche.models.Chronique;
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

public class WaterQualityApiEnvCondTask extends AsyncTask<String, Void, List<EnvironmentalCondition>> {

    @Override
    protected List<EnvironmentalCondition> doInBackground(String... params) {
        List<EnvironmentalCondition> environmentalConditionList = new ArrayList<>();
        try {
            // Make the connection and open the stream
            URL url = new URL(Constants.WATER_QUALITY_ENV_PC_URL + "?code_station=" + params[0] + "&sort=desc&size=2");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Perform a request and format the result to be a correct json format
            String httpResult = Converters.readStream(in);

            JSONArray envConds = (new JSONObject(httpResult)).getJSONArray("data");
            for (int j = 0; j < envConds.length(); j++) {
                JSONObject jsonEnvCond = envConds.getJSONObject(j);
                environmentalConditionList.add(new EnvironmentalCondition(jsonEnvCond));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return environmentalConditionList;
    }
}