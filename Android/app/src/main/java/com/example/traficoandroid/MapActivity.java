package com.example.traficoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonMap = findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMapFragment();
            }
        });
    }

    private void showMapFragment() {
        EditText latText = findViewById(R.id.latText);
        EditText longText = findViewById(R.id.longText);

        String lat = latText.getText().toString();
        String lon = longText.getText().toString();

        // Create a new instance of MapFragment and pass lat/lon as arguments
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("LATITUDE", lat);
        args.putString("LONGITUDE", lon);
        mapFragment.setArguments(args);

        // Show the MapFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mapContainer, mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Make the map container visible
        findViewById(R.id.mapContainer).setVisibility(View.VISIBLE);
    }
}
