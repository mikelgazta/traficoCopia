package com.example.traficoandroid.datamanagers;

import android.util.Log;

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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IncidenciasDatamanager {
    //private OkHttpClient client = new OkHttpClient();
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // Ajusta el tiempo de espera de conexión
            .build();


    public void cargarListaIncidencias(String jwtToken, DataLoadListener listener) {
        Log.d("cargarListaIncidencias", "jwtToken-cargarListaIncidencias(): " + jwtToken);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/listaIncidencias")
                .addHeader("Authorization", jwtToken) // Asegúrate de incluir "Bearer " antes del token
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
                    List<DataItem> items = parseItemsFromApi(responseBody);
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

    private List<DataItem> parseItemsFromApi(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);

        if (jsonElement.isJsonArray()) {
            JsonArray incidencesArray = jsonElement.getAsJsonArray();

            for (JsonElement incidenceElement : incidencesArray) {
                JsonObject incidenceObject = incidenceElement.getAsJsonObject();

                // Obtener los valores de cada campo de la incidencia
                String tipo = getStringValueFromJson(incidenceObject, "TIPO");
                String causa = getStringValueFromJson(incidenceObject, "CAUSA");
                String comienzo = getStringValueFromJson(incidenceObject, "COMIENZO");
                String nivelIncidencia = getStringValueFromJson(incidenceObject, "NVL_INCIDENCIA");
                String carretera = getStringValueFromJson(incidenceObject, "CARRETERA");
                String direccion = getStringValueFromJson(incidenceObject, "DIRECCION");
                double latitud = getDoubleValueFromJson(incidenceObject, "LATITUD");
                double longitud = getDoubleValueFromJson(incidenceObject, "LONGITUD");
                String usuario = getStringValueFromJson(incidenceObject, "USUARIO");

                // Crear el objeto DataItem con los valores obtenidos
                DataItem dataItem = new DataItem("myApi", "incidencia", createJsonString(tipo, causa, comienzo, nivelIncidencia, carretera, direccion, latitud, longitud, usuario));
                items.add(dataItem);
            }
        }
        return items;
    }

    // Método para obtener un valor de cadena de un objeto JSON
    private String getStringValueFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : "";
    }

    // Método para obtener un valor de doble de un objeto JSON
    private double getDoubleValueFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsDouble() : 0.0;
    }

    // Método para crear una cadena JSON a partir de los valores dados
    private String createJsonString(String tipo, String causa, String comienzo, String nivelIncidencia, String carretera, String direccion, double latitud, double longitud, String usuario) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TIPO", tipo);
        jsonObject.addProperty("CAUSA", causa);
        jsonObject.addProperty("COMIENZO", comienzo);
        jsonObject.addProperty("NVL_INCIDENCIA", nivelIncidencia);
        jsonObject.addProperty("CARRETERA", carretera);
        jsonObject.addProperty("DIRECCION", direccion);
        jsonObject.addProperty("LATITUD", latitud);
        jsonObject.addProperty("LONGITUD", longitud);
        jsonObject.addProperty("USUARIO", usuario);
        return jsonObject.toString();
    }


    private List<DataItem> parseItemsFromOpenData(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonObject()) {
            Log.d("DataItem", "JSONOPENDATA: " + jsonElement);

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
