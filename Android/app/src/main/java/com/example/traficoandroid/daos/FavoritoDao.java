package com.example.traficoandroid.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.traficoandroid.models.Favorito;

import java.util.List;

@Dao
public interface FavoritoDao {
    @Query("SELECT * FROM favoritos")
    List<Favorito> getAll();

    @Insert
    void insert(Favorito favorito);

    @Update
    void update(Favorito favorito);

    @Delete
    void delete(Favorito favorito);
}

