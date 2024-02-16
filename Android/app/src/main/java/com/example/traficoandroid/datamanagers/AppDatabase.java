package com.example.traficoandroid.datamanagers;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.traficoandroid.daos.FavoritoDao;
import com.example.traficoandroid.models.Favorito;

// Anotación de la base de datos con todas las entidades y la versión de la base de datos
@Database(entities = {Favorito.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Definición de los DAOs que trabajan con la base de datos
    public abstract FavoritoDao favoritoDao();

    // Instancia singleton de la base de datos
    private static AppDatabase INSTANCE;

    // Método para obtener la instancia de la base de datos
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Crear base de datos aquí
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "favoritos")
                            .fallbackToDestructiveMigration() // Estrategia de migración
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

