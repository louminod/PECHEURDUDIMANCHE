package com.hexa.pecheur_du_dimanche.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Converters {
    /**
     * Used to convert an InputStream to a String
     *
     * @param is
     * @return A String
     * @throws IOException
     */
    public static String readStream(InputStream is) throws IOException {
        // Create the String
        StringBuilder sb = new StringBuilder();

        // Read and convert each line
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }

        // Close the buffer and return the String
        is.close();
        return sb.toString();
    }
}
