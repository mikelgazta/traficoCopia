package com.example.traficoandroid;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtén el mapa asíncronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        dataManager = new IncidenciasDatamanager();

        // Obtener el token del Intent
        Bundle userDataBundle = getIntent().getBundleExtra("userData");
        if (userDataBundle != null) {
            jwtToken = userDataBundle.getString("token");
        }

        // Configurar el listener de clic en el mapa
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.setOnMapClickListener(latLng -> {
                // Abrir AddIncidentActivity con los datos de latitud, longitud y el token
                Intent intent = new Intent(MapActivity.this, AddIncidentActivity.class);
                intent.putExtra("latitud", latLng.latitude);
                intent.putExtra("longitud", latLng.longitude);
                intent.putExtra("token", jwtToken); // Pasar el token como extra
                startActivityForResult(intent, REQUEST_ADD_INCIDENT);
            });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Cargar datos y mostrar marcadores en el mapa
        loadDataAndDisplayMarkers();
    }

    private void loadDataAndDisplayMarkers() {
        // Verificar la disponibilidad del token
        if (jwtToken != null && !jwtToken.isEmpty()) {
            // Cargar los datos de tu API
            Log.d("loadDataAndDisplayMarkers", "jwtToken-loadDataAndDisplayMarkers(): " + jwtToken);

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

            // Cargar los datos de OpenDataEuskadi
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
        } else {
            Log.e("MapActivity", "Token is null or empty");
        }
    }

    private void displayDataOnMap(List<DataItem> items) {
        runOnUiThread(() -> {
            for (DataItem item : items) {
                // Agregar marcadores para todas las incidencias
                addMarkerForIncident(item);
            }
        });
    }

    private void addMarkerForIncident(DataItem incident) {
        // Implementa la lógica para agregar marcadores en el mapa
        try {
            // Obtener el origen de los datos de la incidencia
            String origin = incident.getOrigin();

            // Variables para almacenar latitud, longitud y descripción de la incidencia
            String latitud;
            String longitud;
            String descripcion;

            // Obtener los valores de latitud, longitud y descripción según el origen de los datos
            if ("myAPI".equals(origin)) {
                latitud = incident.getValueFromJson("latitud");
                longitud = incident.getValueFromJson("longitud");
                descripcion = incident.getValueFromJson("causa");
            } else if ("openDataEuskadi".equals(origin)) {
                latitud = incident.getValueFromJson("latitude");
                longitud = incident.getValueFromJson("longitude");
                descripcion = incident.getValueFromJson("cause");
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
                String markerTitle = (descripcion != null && !descripcion.isEmpty()) ? descripcion : "Descripción no disponible";

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

                // Crear un bitmap a partir del recurso de imagen
                Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), imageResId);

                // Escalar el bitmap al tamaño deseado (50x50 píxeles)
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(iconBitmap, 50, 50, false);

                // Crear un descriptor de icono a partir del bitmap escalado
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

                // Agregar el marcador al mapa con la posición, título y icono
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(markerTitle + " - " + origin)
                        .icon(icon));
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción en caso de que la conversión de String a double falle
            e.printStackTrace();
        }
    }



}





