package com.example.traficoandroid.datamanagers;

import com.example.traficoandroid.models.DataItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomJsonParser {
    public static Map<String, String> transformIncidentData(JsonObject incidentData) {
        Map<String, String> transformedData = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : incidentData.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value != null && !value.isJsonNull()) { // Asegúrate de que el valor no sea null

                    // Convierte el valor a String, pero ignora los valores explícitamente nulos
                    String valueStr = value.toString();
                    // Evita añadir el valor si es "null" como cadena
                    if (!"null".equals(valueStr)) {
                        transformedData.put(key, valueStr.replace("\"", "")); // Remueve comillas para valores que no son objetos
                    }
                }
            // Si el valor es null o es un JsonNull, no añadirá el par clave-valor
        }
        return transformedData;
    }

    private static List<DataItem> parseArray(JsonArray jsonArray) {
        List<DataItem> items = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                DataItem dataItem = parseObject(jsonObject);
                items.add(dataItem);
            }
        }
        return items;
    }

    private static DataItem parseObject(JsonObject jsonObject) {
        String origin = jsonObject.get("origin").getAsString();
        String type = jsonObject.get("type").getAsString();
        String json = jsonObject.toString();
        return new DataItem(origin, type, json);
    }
}
