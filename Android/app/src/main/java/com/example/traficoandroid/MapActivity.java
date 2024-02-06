package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad a partir del archivo XML activity_map
        setContentView(R.layout.activity_map);

        // Configura el listener para el layout footer
        setupClickListener();
    }

    // Método para configurar el listener del layout footer
    private void setupClickListener() {
        LinearLayout layout = findViewById(R.id.footer); // Obtiene una referencia al layout footer
        layout.setOnClickListener(v -> navigateToMapFragment()); // Asigna un listener al layout para la navegación al fragmento de mapa
    }

    // Método para navegar al fragmento de mapa (MapFragment)
    private void navigateToMapFragment() {
        Intent intent = new Intent(this, MapFragment.class); // Crea un intent para iniciar la actividad MapFragment
        startActivity(intent); // Inicia la actividad MapFragment
    }
}
