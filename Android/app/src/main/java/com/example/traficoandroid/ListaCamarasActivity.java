package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traficoandroid.adapters.CamarasAdapter;
import com.example.traficoandroid.datamanagers.CamarasDatamanager;
import com.example.traficoandroid.datamanagers.DataLoadListener;
import com.example.traficoandroid.models.DataItem;

import java.util.List;

public class ListaCamarasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CamarasAdapter camarasAdapter;
    private CamarasDatamanager camarasDatamanager;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_camaras);

        jwtToken = getIntent().getStringExtra("jwtToken");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        camarasAdapter = new CamarasAdapter(); // Inicializar el adaptador una vez
        recyclerView.setAdapter(camarasAdapter);

        // Recibir la lista de cámaras del Intent
        List<DataItem> camaras = (List<DataItem>) getIntent().getSerializableExtra("camaras");
        if (camaras != null) {
            // Configurar el adaptador con los datos recibidos
            camarasAdapter.setData(camaras);
        }

        Button volverButton = findViewById(R.id.volverButton);

        // Establece un OnClickListener para el botón
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para volver a MapActivity
                Intent intent = new Intent(ListaCamarasActivity.this, MapActivity.class);
                // Aquí puedes agregar cualquier extra que necesites pasarle a MapActivity
                // Por ejemplo, si necesitas pasarle el jwtToken de vuelta
                intent.putExtra("jwtToken", jwtToken);
                // Inicia MapActivity
                startActivity(intent);
                // Opcional: Si no necesitas volver a ListaCamarasActivity desde MapActivity, puedes llamar a finish() para eliminar ListaCamarasActivity del stack de actividades
                finish();
            }
        });
    }


    private void cargarListaCamaras() {
        camarasDatamanager.cargarListaCamaras(jwtToken, new DataLoadListener() {
            @Override
            public void onDataLoaded(List<DataItem> camaras) {
                // Configura el RecyclerView con el adaptador después de cargar los datos
                camarasAdapter = new CamarasAdapter();
                camarasAdapter.setData(camaras);
                recyclerView.setAdapter(camarasAdapter);
            }

            @Override
            public void onError(Exception e) {
                Log.e("MapActivity", "Error cargando cámaras", e);
            }
        });
    }

}

