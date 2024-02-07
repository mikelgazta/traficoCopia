package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtén el mapa asíncronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Agrega un listener al mapa para obtener las coordenadas del punto seleccionado
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedLatLng = latLng;
                // Abre la actividad de añadir incidencia con las coordenadas predefinidas
                Intent intent = new Intent(MapActivity.this, AddIncidentActivity.class);
                intent.putExtra("selectedLatLng", selectedLatLng);
                startActivity(intent);
            }
        });
    }



    // Método para configurar los listeners de los botones del footer
    private void setupFooterButtons() {
        Button crearIncidenciaButton = findViewById(R.id.crearIncidencia);
        Button verFavoritosButton = findViewById(R.id.verFavoritos);
        Button verCamarasButton = findViewById(R.id.verCamaras);

        // Listener para el botón "Crear Incidencia"
        crearIncidenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de Crear Incidencia
                startActivity(new Intent(MapActivity.this, AddIncidentActivity.class));
            }
        });

        // Listener para el botón "Ver Favoritos"
        verFavoritosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de Ver Favoritos
                startActivity(new Intent(MapActivity.this, FavoritosActivity.class));
            }
        });

        // Listener para el botón "Ver Cámaras"
        verCamarasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de Ver Cámaras
                startActivity(new Intent(MapActivity.this, CamarasActivity.class));
            }
        });
    }
}

