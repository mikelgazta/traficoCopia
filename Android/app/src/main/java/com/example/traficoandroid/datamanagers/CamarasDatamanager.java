package com.example.traficoandroid.datamanagers;

import com.example.traficoandroid.models.DataItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CamarasDatamanager {
    private OkHttpClient client = new OkHttpClient();

    public void loadMyApiData(String jwtToken, DataLoadListener listener) {
        Request request = new Request.Builder().url("http://10.0.2.2:8000/api/seleccionar").addHeader("Authorization", "Bearer " + jwtToken).build();

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
                    listener.onError(new IOException("HTTP error! status: " + response.code()));
                }
            }
        });
    }

    public void loadOpenDataEuskadiData(DataLoadListener listener) {
        String latlog = "43.3202955/-1.7893800/100";
        String url = "https://api.euskadi.eus/traffic/v1.0/cameras/byLocation/" + latlog;

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
        // Aquí deberías implementar la lógica para parsear el responseBody a una lista de DataItem
        // usando por ejemplo, Gson o cualquier otra librería de parsing de JSON.
        // El siguiente código es solo un ejemplo basado en tu descripción:
        List<DataItem> items = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

        // Iterar sobre el array JSON y transformar cada elemento
        for (JsonElement element : jsonArray) {
            // Transformar el elemento JSON en un Map<String, String>
            JsonObject jsonObject = element.getAsJsonObject();

            Map<String, String> transformedData = CustomJsonParser.transformIncidentData(jsonObject);

            // Crear un nuevo DataItem con los datos transformados y añadirlo a la lista
            DataItem dataItem = new DataItem("myAPI", "camara", transformedData.toString());
            items.add(dataItem);
        }
        return items;
    }

    private List<DataItem> parseItemsFromOpenData(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray incidencesArray = jsonObject.getAsJsonArray("cameras");

            for (JsonElement incidenceElement : incidencesArray) {
                JsonObject incidenceObject = incidenceElement.getAsJsonObject();
                // Aquí convertimos el objeto JSON de la incidencia en una cadena JSON
                String incidenceJsonString = incidenceObject.toString();
                // Asumiendo que DataItem puede ser inicializado con un origen, tipo e información de la incidencia en formato JSON
                DataItem dataItem = new DataItem("openDataEuskadi", "camara", incidenceJsonString);
                items.add(dataItem);
            }
        }
        return items;
    }

}

