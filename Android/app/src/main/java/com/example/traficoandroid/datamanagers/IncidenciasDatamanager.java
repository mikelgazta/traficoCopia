package com.example.traficoandroid.datamanagers;

import com.example.traficoandroid.models.DataItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IncidenciasDatamanager {
    private OkHttpClient client = new OkHttpClient();

    public void cargarListaIncidencias(String jwtToken, DataLoadListener listener) {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/listaIncidencias")
                .addHeader("Authorization", jwtToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<DataItem> items = parseItems(responseBody);
                    listener.onDataLoaded(items);
                } else {
                    listener.onError(new IOException("¡Error HTTP! Estado: " + response.code()));
                }
            }
        });
    }
    public void cargarListaIncidenciasOpenDataEuskadi(DataLoadListener listener) {
        String fechaHoy = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        String url = "https://api.euskadi.eus/traffic/v1.0/incidences/byDate/" + fechaHoy;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<DataItem> items = parseItemsFromOpenData(responseBody);
                    listener.onDataLoaded(items);
                } else {
                    listener.onError(new IOException("HTTP error! status: " + response.code()));
                }
            }
        });
    }

    private List<DataItem> parseItems(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            // Convertir el JsonObject en un Map<String, String>
            Map<String, String> transformedData = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                transformedData.put(entry.getKey(), entry.getValue().getAsString());
            }

            // Crear un nuevo DataItem con los datos transformados
            DataItem dataItem = new DataItem("myAPI", "incidencia", transformedData.toString());
            items.add(dataItem);
        }
        return items;
    }

    private List<DataItem> parseItemsFromOpenData(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray incidencesArray = jsonObject.getAsJsonArray("incidences");

            for (JsonElement incidenceElement : incidencesArray) {
                JsonObject incidenceObject = incidenceElement.getAsJsonObject();
                // Aquí convertimos el objeto JSON de la incidencia en una cadena JSON
                String incidenceJsonString = incidenceObject.toString();
                // Asumiendo que DataItem puede ser inicializado con un origen, tipo e información de la incidencia en formato JSON
                DataItem dataItem = new DataItem("openDataEuskadi", "incidencia", incidenceJsonString);
                items.add(dataItem);
            }
        }
        return items;
    }
}
