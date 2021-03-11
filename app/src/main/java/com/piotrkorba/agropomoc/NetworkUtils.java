package com.piotrkorba.agropomoc;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String PROTOCOL = "http";
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 8000;
    private static final String FILE = "rejestr.json";

    static String getRegistryContent() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String registryJSONString = null;

        try {
            URL requestURL = new URL(PROTOCOL, HOST, PORT, FILE);
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get input stream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Hold the response in StringBuilder
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            // No reason to parse empty json
            if (builder.length() == 0)
                return null;
            registryJSONString = builder.toString();
            Log.d(LOG_TAG, registryJSONString);

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return registryJSONString;
    }

    static JSONArray convertStringJSON(String str) {
        // Convert String to JSON array
        JSONArray ja = null;
        try {
            ja = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }
}
