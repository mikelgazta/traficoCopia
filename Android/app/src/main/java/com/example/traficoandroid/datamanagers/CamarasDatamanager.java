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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CamarasDatamanager {
    //private OkHttpClient client = new OkHttpClient();
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // Ajusta el tiempo de espera de conexión
            .build();


    public void cargarListaCamaras(String jwtToken, DataLoadListener listener) {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/verCamaras")
                .addHeader("Authorization", jwtToken) // Asegúrate de incluir "Bearer " antes del token si es necesario
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
                    List<DataItem> items = parseItemsFromCamaras(responseBody);
                    listener.onDataLoaded(items);
                } else {
                    listener.onError(new IOException("¡Error HTTP! Estado: " + response.code()));
                }
            }
        });
    }

    public CompletableFuture<List<DataItem>> loadOpenDataEuskadiCameras() {
        CompletableFuture<List<DataItem>> future = new CompletableFuture<>();
        // Puedes ajustar las coordenadas y el radio según necesites
        String latlog = "43.3202955/-1.7893800/100";
        String url = "https://api.euskadi.eus/traffic/v1.0/cameras/byLocation/" + latlog;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<DataItem> cameras = parseCamerasFromOpenData(responseBody);
                    future.complete(cameras);
                } else {
                    future.completeExceptionally(new IOException("HTTP error! status: " + response.code()));
                }
            }
        });

        return future;
    }

    // Debes implementar un método para parsear los datos de las cámaras de la respuesta JSON.
    private List<DataItem> parseCamerasFromOpenData(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray camerasArray = jsonObject.getAsJsonArray("cameras");
            for (JsonElement cameraElement : camerasArray) {
                JsonObject cameraObject = cameraElement.getAsJsonObject();
                String cameraJsonString = cameraObject.toString();
                DataItem dataItem = new DataItem("openDataEuskadi", "camara", cameraJsonString);
                items.add(dataItem);
            }
        }
        return items;
    }


    private List<DataItem> parseItemsFromCamaras(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);

        if (jsonElement.isJsonArray()) {
            JsonArray camarasArray = jsonElement.getAsJsonArray();

            for (JsonElement camaraElement : camarasArray) {
                JsonObject camaraObject = camaraElement.getAsJsonObject();

                // Obtener los valores de cada campo de la cámara
                String nombre = getStringValueFromJson(camaraObject, "NOMBRE");
                String url = getStringValueFromJson(camaraObject, "URL");
                double latitud = getDoubleValueFromJson(camaraObject, "LATITUD");
                double longitud = getDoubleValueFromJson(camaraObject, "LONGITUD");
                String carretera = getStringValueFromJson(camaraObject, "CARRETERA");
                int kilometro = getIntValueFromJson(camaraObject, "KILOMETRO");
                String imagen = getStringValueFromJson(camaraObject, "IMAGEN");
                String usuario = getStringValueFromJson(camaraObject, "USUARIO");

                // Aquí podrías definir cómo quieres almacenar y usar la información de la cámara
                // Por ejemplo, podrías crear un nuevo DataItem o una nueva clase CamaraItem
                DataItem camaraItem = new DataItem("myApi", "camara", createJsonStringCamara(nombre, url, latitud, longitud, carretera, kilometro, imagen, usuario));
                items.add(camaraItem);
            }
        }
        return items;
    }

    // Método para crear una cadena JSON para la cámara a partir de los valores dados
    private String createJsonStringCamara(String nombre, String url, double latitud, double longitud, String carretera, int kilometro, String imagen, String usuario) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("NOMBRE", nombre);
        jsonObject.addProperty("URL", url);
        jsonObject.addProperty("LATITUD", latitud);
        jsonObject.addProperty("LONGITUD", longitud);
        jsonObject.addProperty("CARRETERA", carretera);
        jsonObject.addProperty("KILOMETRO", kilometro);
        jsonObject.addProperty("IMAGEN", imagen);
        jsonObject.addProperty("USUARIO", usuario);
        return jsonObject.toString();
    }

    // Método para obtener un valor entero de un objeto JSON
    private String getStringValueFromJson(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null && !element.isJsonNull() ? element.getAsString() : "";
    }

    private double getDoubleValueFromJson(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null && !element.isJsonNull() ? element.getAsDouble() : 0.0;
    }

    private int getIntValueFromJson(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null && !element.isJsonNull() ? element.getAsInt() : 0;
    }

}
