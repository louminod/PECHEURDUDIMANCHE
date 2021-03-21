package com.hexa.pecheur_du_dimanche.api.localisation;

import android.location.Location;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.api.localisation.tasks.ReverseLocationTask;
import com.hexa.pecheur_du_dimanche.models.Address;

public class APIAdresse {
    /**
     * Convert a location to an address
     * @param location
     * @return Adress
     */
    public static Address reverseLocation(Location location) {
        try {
            // Create and execute the task
            ReverseLocationTask reverseLocationTask = new ReverseLocationTask();
            return reverseLocationTask.execute(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude())).get();
        } catch (Exception exception) {
            Log.e("reverseLocation", exception.getMessage());
        }
        return null;
    }
}
