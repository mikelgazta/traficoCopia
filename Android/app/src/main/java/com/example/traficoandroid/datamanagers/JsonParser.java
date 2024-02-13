package com.example.traficoandroid.datamanagers;

import com.example.traficoandroid.models.DataItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<DataItem> parseItems(String responseBody) {
        // Utiliza la clase JsonParser de Gson para parsear la cadena JSON
        JsonElement jsonElement = com.google.gson.JsonParser.parseString(responseBody);

        // Verifica si el elemento JSON es un array
        if (jsonElement.isJsonArray()) {
            return parseArray(jsonElement.getAsJsonArray());
        } else {
            // Maneja el caso en que el JSON no sea un array
            // Puedes implementar una lógica adicional según tus necesidades
            return new ArrayList<>();
        }
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
