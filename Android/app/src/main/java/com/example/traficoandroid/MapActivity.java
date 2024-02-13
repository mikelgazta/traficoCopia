package com.example.traficoandroid;

import android.os.Bundle;
import android.util.Log;

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

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

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
                try {
                    String origin = item.getOrigin();
                    String type = item.getType();
                    String latitud;
                    String longitud;
                    String descripcion;

                    if ("myAPI".equals(origin)) {
                        latitud = item.getValueFromJson("latitud");
                        longitud = item.getValueFromJson("longitud");
                        descripcion = item.getValueFromJson("descripcion");
                    } else if ("openDataEuskadi".equals(origin)) {
                        latitud = item.getValueFromJson("latitude");
                        longitud = item.getValueFromJson("longitude");
                        descripcion = item.getValueFromJson("descripcion");
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
        });
    }
}