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
import com.example.traficoandroid.datamanagers.CamarasDatamanager;
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

    public static final int REQUEST_ADD_INCIDENT = 1;

    private GoogleMap mMap;
    private IncidenciasDatamanager dataManager;

    private CamarasDatamanager camarasDataManager;

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
        camarasDataManager = new CamarasDatamanager();


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
                intent.putExtra("LATITUD", latLng.latitude);
                intent.putExtra("LONGITUD", latLng.longitude);
                intent.putExtra("token", jwtToken); // Pasar el token como extra
                startActivityForResult(intent, REQUEST_ADD_INCIDENT);
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_INCIDENT && resultCode == RESULT_OK && data != null) {
            String jsonNuevaIncidencia = data.getStringExtra("nuevaIncidencia");
            Gson gson = new Gson();
            DataItem nuevaIncidencia = gson.fromJson(jsonNuevaIncidencia, DataItem.class);

            // Agregar un marcador para la nueva incidencia en el mapa
            addMarkerForIncident(nuevaIncidencia);
            loadDataAndDisplayMarkers();
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
            //Log.d("loadDataAndDisplayMarkers", "jwtToken-loadDataAndDisplayMarkers(): " + jwtToken);

            dataManager.cargarListaIncidencias(jwtToken, new DataLoadListener() {
                @Override
                public void onDataLoaded(List<DataItem> items) {
                    Log.d("MapActivity", "Items cargados de mi api: " + items.toString());
                    displayDataOnMap(items);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("MapActivity", "Error loading data from my API", e);
                }
            });
            camarasDataManager.cargarListaCamaras(jwtToken, new DataLoadListener() {
                @Override
                public void onDataLoaded(List<DataItem> camaras) {
                    Log.d("MapActivity", "Cámaras cargadas: " + camaras.toString());
                    displayDataOnMap(camaras); // Puede reutilizar el método para mostrar las cámaras en el mapa
                }

                @Override
                public void onError(Exception e) {
                    Log.e("MapActivity", "Error loading cameras", e);
                }
            });

            // Cargar los datos de las cámaras de OpenData Euskadi y mostrarlos en el mapa
            camarasDataManager.loadOpenDataEuskadiCameras()
                    .thenAcceptAsync(camaras -> {
                        Log.d("MapActivity", "Cámaras de OpenData Euskadi cargadas: " + camaras.toString());
                        displayDataOnMap(camaras); // Puede reutilizar el método para mostrar las cámaras en el mapa
                    }, this::runOnUiThread)
                    .exceptionally(e -> {
                        Log.e("MapActivity", "Error loading OpenData Euskadi cameras", e);
                        return null;
                    });
            // Cargar los datos de OpenDataEuskadi
            dataManager.cargarListaIncidenciasOpenDataEuskadi(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<DataItem> items) {
                    Log.d("MapActivity", "Items cargados de dataeuskadi: " + items.toString());
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
        try {
            String origin = incident.getOrigin();
            String tipo = incident.getType(); // Asegúrate de que tienes un método getType() en DataItem o ajusta según tu implementación
            String latitud;
            String longitud;
            String descripcion;

            if ("myApi".equals(origin)) {
                latitud = incident.getValueFromJson("LATITUD");
                longitud = incident.getValueFromJson("LONGITUD");
                descripcion = tipo.equals("camara") ? incident.getValueFromJson("NOMBRE") : incident.getValueFromJson("CAUSA");
            } else if ("openDataEuskadi".equals(origin)) {
                latitud = incident.getValueFromJson("latitude");
                longitud = incident.getValueFromJson("longitude");
                descripcion = tipo.equals("camara") ? incident.getValueFromJson("cameraName") : incident.getValueFromJson("cause");
            } else {
                latitud = "";
                longitud = "";
                descripcion = "Información no disponible";
            }

            if (latitud != null && !latitud.isEmpty() && longitud != null && !longitud.isEmpty()) {
                double lat = Double.parseDouble(latitud);
                double lng = Double.parseDouble(longitud);
                String markerTitle = !descripcion.isEmpty() ? descripcion : "Descripción no disponible";

                int imageResId = getImageResource(origin, tipo); // Utiliza una función para determinar el recurso de imagen basado en el origen y tipo

                Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(iconBitmap, 50, 50, false);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(markerTitle + " - " + origin)
                        .icon(icon));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int getImageResource(String origin, String tipo) {
        // Primero, maneja el caso para "incidencias"
        if ("incidencia".equals(tipo)) {
            if ("myApi".equals(origin)) {
                return R.drawable.ping_api1; // Ícono específico para incidencias de myApi
            } else if ("openDataEuskadi".equals(origin)) {
                return R.drawable.ping_api2; // Ícono diferente para incidencias de OpenDataEuskadi
            }
        }

        // Luego, maneja el caso para "cámaras"
        if ("camara".equals(tipo)) {
            // Asume que quieres usar el mismo ícono para cámaras, independientemente del origen
            return R.drawable.ic_camera; // Ícono para cámaras
        }

        // Aquí puedes añadir más casos para otros tipos si es necesario

        // Finalmente, devuelve un ícono por defecto si no se cumple ninguna de las condiciones anteriores
        return R.drawable.googlemaps; // Ícono por defecto
    }
}





