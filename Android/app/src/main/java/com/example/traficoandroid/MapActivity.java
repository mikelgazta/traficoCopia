package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traficoandroid.datamanagers.DataLoadListener;
import com.example.traficoandroid.datamanagers.IncidenciasDatamanager;
import com.example.traficoandroid.models.DataItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_ADD_INCIDENT = 1;

    private GoogleMap mMap;
    private IncidenciasDatamanager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtén el mapa asíncronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        dataManager = new IncidenciasDatamanager();

        // Configurar el listener de clic en el mapa
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.setOnMapClickListener(latLng -> {
                // Obtener el token del Intent
                Bundle userDataBundle = getIntent().getBundleExtra("userData");
                String token = null;
                if (userDataBundle != null) {
                    token = userDataBundle.getString("token");
                }

                // Abrir AddIncidentActivity con los datos de latitud, longitud y el token
                Intent intent = new Intent(MapActivity.this, AddIncidentActivity.class);
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longitude", latLng.longitude);
                intent.putExtra("token", token); // Pasar el token como extra
                startActivityForResult(intent, REQUEST_ADD_INCIDENT);
            });

            // Cargar datos y mostrar marcadores en el mapa
            loadDataAndDisplayMarkers();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_INCIDENT && resultCode == RESULT_OK && data != null) {
            // Obtener la nueva incidencia creada desde AddIncidentActivity
            String jsonNuevaIncidencia = data.getStringExtra("nuevaIncidencia");
            Gson gson = new Gson();
            DataItem nuevaIncidencia = gson.fromJson(jsonNuevaIncidencia, DataItem.class);

            // Agregar un marcador para la nueva incidencia en el mapa
            addMarkerForIncident(nuevaIncidencia);
        }
    }

    private void addMarkerForIncident(DataItem incident) {
        try {
            String origin = incident.getOrigin();
            String latitud;
            String longitud;
            String descripcion;

            if ("myAPI".equals(origin)) {
                latitud = incident.getValueFromJson("latitud");
                longitud = incident.getValueFromJson("longitud");
                descripcion = incident.getValueFromJson("descripcion");
            } else if ("openDataEuskadi".equals(origin)) {
                latitud = incident.getValueFromJson("latitude");
                longitud = incident.getValueFromJson("longitude");
                descripcion = incident.getValueFromJson("descripcion");
            } else {
                // Manejar cualquier otro origen de datos
                latitud = "";
                longitud = "";
                descripcion = "";
            }

            // Convertir a double solo si las cadenas no están vacías y no son nulas
            if (latitud != null && !latitud.isEmpty() && longitud != null && !longitud.isEmpty()) {
                double lat = Double.parseDouble(latitud);
                double lng = Double.parseDouble(longitud);

                // Verificar si la descripción es nula o está vacía, y proporcionar un valor predeterminado en ese caso
                String markerTitle = descripcion != null && !descripcion.isEmpty() ? descripcion : "Descripción no disponible";

                // Definir el recurso de imagen según el origen de los datos
                int imageResId;
                if ("myAPI".equals(origin)) {
                    imageResId = R.drawable.ping_api1;
                } else if ("openDataEuskadi".equals(origin)) {
                    imageResId = R.drawable.ping_api2;
                } else {
                    // Definir un recurso de imagen predeterminado si el origen no coincide con ninguno conocido
                    imageResId = R.drawable.googlemaps;
                }

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(imageResId);

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(markerTitle + " - " + origin).icon(icon));
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción en caso de que la conversión de String a double falle
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Cargar datos y mostrar marcadores en el mapa
        loadDataAndDisplayMarkers();
    }

    private void loadDataAndDisplayMarkers() {
        // Lógica para cargar datos de la API y mostrar marcadores en el mapa
        Bundle token = getIntent().getExtras();
        String jwtToken = token != null ? token.getString("usuario") : "";

        // Carga los datos de tu API
        dataManager.cargarListaIncidencias(jwtToken, new DataLoadListener() {
            @Override
            public void onDataLoaded(List<DataItem> items) {
                Log.d("MapActivity", "Items cargados: " + items.toString());
                displayDataOnMap(items);
            }

            @Override
            public void onError(Exception e) {
                Log.e("MapActivity", "Error loading data from my API", e);
            }
        });

        // Carga los datos de OpenDataEuskadi
        dataManager.cargarListaIncidenciasOpenDataEuskadi(new DataLoadListener() {
            @Override
            public void onDataLoaded(List<DataItem> items) {
                Log.d("MapActivity", "Items cargados: " + items.toString());
                displayDataOnMap(items);
            }

            @Override
            public void onError(Exception e) {
                Log.e("MapActivity", "Error loading data from OpenDataEuskadi", e);
            }
        });
    }

    private void displayDataOnMap(List<DataItem> items) {
        runOnUiThread(() -> {
            for (DataItem item : items) {
                // Agregar marcadores para todas las incidencias
                addMarkerForIncident(item);
            }
        });
    }
}
