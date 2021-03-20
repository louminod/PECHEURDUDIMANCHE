package com.hexa.pecheur_du_dimanche.services;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.hexa.pecheur_du_dimanche.models.Station;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes/reads an object to/from a private local file
 */
public class LocalPersistence {

    private static final String STATIONS_PATH = "stations";


    public static void saveStation(Context context, Station station) {
        ObjectOutputStream objectOut = null;
        try {
            List<Station> stations = getStations(context);
            stations.add(station);
            FileOutputStream fos = context.openFileOutput(STATIONS_PATH, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(stations);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }

    public static void unsaveStation(Context context, Station station) {
        ObjectOutputStream objectOut = null;
        try {
            List<Station> stations = getStations(context);
            List<Station> newStations = new ArrayList<>();
            for (Station s : stations) {
                if (!station.getCodeStation().equals(s.getCodeStation())) {
                    newStations.add(s);
                }
            }
            FileOutputStream fos = context.openFileOutput(STATIONS_PATH, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(newStations);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
        Log.i("LocalPersistence", "Object saved !");
    }


    public static List<Station> getStations(Context context) {

        ObjectInputStream objectIn = null;
        List<Station> stations = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(LocalPersistence.STATIONS_PATH);
            ObjectInputStream is = new ObjectInputStream(fis);
            stations = (List<Station>) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            // Do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }

        return stations;
    }

}