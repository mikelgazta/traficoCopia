package com.example.traficoandroid;
/*
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traficoandroid.datamanagers.AppDatabase;


import com.example.traficoandroid.R;
import com.example.traficoandroid.adapters.FavoritosAdapter;
import com.example.traficoandroid.models.Favorito;
import com.example.traficoandroid.daos.FavoritoDao;

import java.util.List;

public class FavoritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;
    private AppDatabase db;
    private FavoritoDao favoritoDao;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_favoritos);

        recyclerView = findViewById(R.id.recyclerViewFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritosAdapter(this);
        recyclerView.setAdapter(adapter);

        addButton = findViewById(R.id.addFavoritoButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes abrir un diálogo o una nueva actividad para agregar un favorito
                // Por ejemplo: abrirAddFavoritoDialog();
            }
        });

        // Inicializar la base de datos y el DAO
        db = AppDatabase.getDatabase(this);
        favoritoDao = db.favoritoDao();

        cargarFavoritos();
    }

    private void cargarFavoritos() {
        // Asumiendo que tienes una instancia de AppDatabase llamada appDatabase
        FavoritoDao favoritoDao = appDatabase.favoritoDao();

        // Ejecutar la consulta en un hilo secundario
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Obtener la lista de favoritos de la base de datos
                final List<Favorito> favoritos = favoritoDao.getAllFavoritos();

                // Actualizar el adaptador en el hilo principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Asumiendo que tu adaptador tiene un método para establecer los datos
                        favoritosAdapter.setFavoritos(favoritos);
                        favoritosAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    // Método para agregar un nuevo favorito a la base de datos
    private void agregarFavorito(Favorito favorito) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoritoDao.insert(favorito);
                cargarFavoritos();
            }
        }).start();
    }

    // Método para actualizar un favorito existente
    private void actualizarFavorito(Favorito favorito) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoritoDao.update(favorito);
                cargarFavoritos();
            }
        }).start();
    }

    // Método para eliminar un favorito
    private void eliminarFavorito(Favorito favorito) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoritoDao.delete(favorito);
                cargarFavoritos();
            }
        }).start();
    }

    // Suponiendo que tienes un método para abrir un diálogo y agregar un favorito
    // private void abrirAddFavoritoDialog() { ... }
}*/

