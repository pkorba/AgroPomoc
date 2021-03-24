package com.piotrkorba.agropomoc;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
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
    private static final String VERSION_FILE = "version";

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

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static int checkRegistryVersion() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        int registryVersion = -1;

        try {
            URL requestURL = new URL(PROTOCOL, HOST, PORT, VERSION_FILE);
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get input stream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            registryVersion = Integer.parseInt(line);

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
        return registryVersion;
    }

    public static void downloadLabel(Context context, String labelUrl, String name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(labelUrl));
        request.setDescription(name);
        request.setTitle(context.getString(R.string.app_name));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".pdf");
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
