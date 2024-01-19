package com.example.traficoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Obtener los argumentos
        Bundle args = getArguments();
        if (args != null) {
            String lat = args.getString("LATITUDE", "0");
            String lon = args.getString("LONGITUDE", "0");

            // Convertir las coordenadas a double
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

            // Crear LatLng y posicionar la cámara
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location).title("Ubicación"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        } else {
            // Manejar el caso en el que no se proporcionaron coordenadas
            Toast.makeText(getContext(), "No se proporcionaron coordenadas.", Toast.LENGTH_SHORT).show();
        }
    }
}
