package com.robertkiszelirk.googlebooksearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private EditText searchField;
    private RadioGroup searchIn;
    private LinearLayout mainScreen;
    private LinearLayout progressBar;
    private TextView no_connection;

    private boolean firstStart = true;

    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set views
        mainScreen = (LinearLayout) findViewById(R.id.main_screen);
        progressBar = (LinearLayout) findViewById(R.id.progress_bar);
        no_connection = (TextView) findViewById(R.id.no_internet_connection);
        // Set edit text
        searchField = (EditText) findViewById(R.id.search_field);
        // Set base radio button selection
        RadioButton searchAll = (RadioButton) findViewById(R.id.search_all);
        searchAll.setChecked(true);
        searchIn = (RadioGroup) findViewById(R.id.radio_group_search_in);
        // Check internet connection
        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        // If it has internet connection
        if(isConnected) {
            // Set search button
            Button bookSearchButton = (Button) findViewById(R.id.book_search_button);
            bookSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check internet connection
                    cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    activeNetwork = cm.getActiveNetworkInfo();
                    isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if(isConnected) {
                        // Main url for query
                        String urlString = getString(R.string.main_url);
                        // Handle radio button selection
                        if (searchField.getText().toString().trim().length() > 0) {
                            switch (searchIn.getCheckedRadioButtonId()) {
                                case R.id.search_all:
                                    urlString = urlString + searchField.getText().toString().trim().replace(" ", "+");
                                    break;
                                case R.id.search_title:
                                    urlString = urlString + getString(R.string.in_title) + searchField.getText().toString().trim().replace(" ", "+");
                                    break;
                                case R.id.search_author:
                                    urlString = urlString + getString(R.string.in_author) + searchField.getText().toString().trim().replace(" ", "+");
                                    break;
                            }
                            // Set load view
                            mainScreen.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            // Get json file
                            URL url = createUrl(urlString);
                            GetJson getJson = new GetJson(MainActivity.this, url);
                            getJson.execute();
                        } else {
                            // Handle empty edit text
                            Toast.makeText(MainActivity.this, R.string.empty_search_field, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // Handle missed internet connection
                        Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else{
            // Warning for missing internet connection
            mainScreen.setVisibility(View.GONE);
            no_connection.setVisibility(View.VISIBLE);
        }
    }
    // Create URL
    private static URL createUrl(String urlString) {
        // Base URL
        URL url = null;
        // Try create URL check format
        try{
            url = new URL(urlString);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG,"Problem building URL", e);
        }
        // Return URL
        return url;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!firstStart) {
            mainScreen.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            no_connection.setVisibility(View.GONE);
        }
        firstStart = false;
    }
}
