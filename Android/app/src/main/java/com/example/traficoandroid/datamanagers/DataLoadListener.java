package com.example.traficoandroid.datamanagers;

import com.example.traficoandroid.models.DataItem;

import java.util.List;

public interface DataLoadListener {

    // Método llamado cuando se han cargado correctamente los datos
    void onDataLoaded(List<DataItem> items);

    // Método llamado cuando ocurre un error durante la carga de datos
    void onError(Exception e);
}