package com.example.traficoandroid.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataItem {
    private String origin; // Origen del dato (por ejemplo, "myAPI" o "openDataEuskadi")
    private String type; // Tipo del dato (por ejemplo, "incidencia", "camara" o "flujo")
    private String json; // Representación en formato JSON del dato

    // Constructor
    public DataItem(String origin, String type, String json) {
        this.origin = origin;
        this.type = type;
        this.json = json;
    }

    // Getters y Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    /**
     * Método para obtener un valor específico del JSON.
     * @param key La clave para buscar en el objeto JSON.
     * @return El valor asociado con la clave si existe, o una cadena vacía si no se encuentra.
     */
    public String getValueFromJson(String key) {
        if (json == null || json.isEmpty()) {
            return ""; // Devuelve una cadena vacía si el JSON es nulo o vacío
        }

        // Parsear el JSON y buscar el valor asociado con la clave
        JsonElement jsonElement = JsonParser.parseString(json);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement valueElement = jsonObject.get(key);
            if (valueElement != null) {
                // Comprobar si el valor es un objeto o un array anidado y devolver su representación en forma de cadena
                if (valueElement.isJsonObject() || valueElement.isJsonArray()) {
                    return valueElement.toString();
                } else {
                    // Para valores primitivos, devolver el valor como una cadena
                    return valueElement.getAsString();
                }
            }
        }
        return ""; // Devuelve una cadena vacía si la clave no existe o si el elemento no es un objeto
    }

    @Override
    public String toString() {
        // Representación de cadena del objeto DataItem
        return "DataItem{origin='" + origin + "', type='" + type + "', json='" + json + "'}";
    }
}
