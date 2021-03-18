package com.hexa.pecheur_du_dimanche.api.localisation;

import android.location.Location;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.fishState.tasks.WaterFishStateTask;
import com.hexa.pecheur_du_dimanche.api.localisation.tasks.ReverseLocationTask;
import com.hexa.pecheur_du_dimanche.models.Adresse;

public class APIAdresse {
    public static Adresse reverseLocation(Location location) {
        try {
            ReverseLocationTask reverseLocationTask = new ReverseLocationTask();
            return reverseLocationTask.execute(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude())).get();
        } catch (Exception exception) {
            Log.e("reverseLocation", exception.getMessage());
        }
        return null;
    }
}
