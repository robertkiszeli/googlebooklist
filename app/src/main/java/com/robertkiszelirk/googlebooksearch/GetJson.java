package com.robertkiszelirk.googlebooksearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

class GetJson extends AsyncTask<URL, Void, String> {

    private static final String LOG_TAG = GetJson.class.getSimpleName();
    private static Context context;
    private URL url;

    GetJson(Context context, URL url) {
        this.context = context;
        this.url = url;
    }

    @Override
    protected String doInBackground(URL... urls) {
        // Start getting json
        return makeHttpRequest(url);
    }

    @Override
    protected void onPostExecute(String s) {
        // Start SearchResult.class when json is done
        if (s != null) {
            Intent intent = new Intent(context, SearchResult.class);
            intent.putExtra("json", s);
            context.startActivity(intent);
        }
    }

    // Make HTTP request
    private static String makeHttpRequest(URL url) {
        // Response JSON
        String jsonResponse = "";
        // Check if URL valid
        if (url == null) {
            return jsonResponse;
        }
        // Set base data
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        // Try to connect to HTTP
        try {
            // Create connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            // Max read time
            urlConnection.setReadTimeout(10000);
            // Max connect time
            urlConnection.setConnectTimeout(15000);
            // Request type
            urlConnection.setRequestMethod("GET");
            // Start connection
            urlConnection.connect();
            // Check if connection OK
            if (urlConnection.getResponseCode() == 200) {
                // Read JSON
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, context.getString(R.string.error_response) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_retrieving_json), e);
        } finally {
            // At end disconnect
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // At end close stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Log.e(LOG_TAG, context.getString(R.string.i_o_exception));
                }
            }
        }
        // Return JSON file
        return jsonResponse;
    }

    // Read from stream
    private static String readFromInputStream(InputStream inputStream) throws IOException {
        // Create string builder
        StringBuilder readString = new StringBuilder();
        // Check input stream valid
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            // Read stream line by line
            while (line != null) {
                readString.append(line);
                line = bufferedReader.readLine();
            }
        }
        // Return string
        return readString.toString();
    }

}
