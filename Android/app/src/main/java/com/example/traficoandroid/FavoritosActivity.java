package com.example.traficoandroid;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traficoandroid.Beans.Incidencia;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private List<Incidencia> listaIncidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_favoritos);

        // Obtener referencia al TextView donde se mostrarán las incidencias
        TextView textView = findViewById(R.id.textViewFavorites);

        // Suponiendo que tienes un método para obtener las incidencias desde tu API
        obtenerIncidenciasDesdeAPI(textView);
    }

    // Método para obtener las incidencias desde tu API
    private void obtenerIncidenciasDesdeAPI(TextView textView) {
        // Definir el cliente OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Definir el tipo de media
        MediaType mediaType = MediaType.parse("application/json");

        // Crear el cuerpo de la solicitud
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(json, mediaType);

        // Construir la solicitud
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/login")
                .post(body)
                .build();

        // Realizar la llamada de manera asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejar fallo en la llamada
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Manejar respuesta no exitosa
                    return;
                }

                // Obtener la respuesta como String
                String jsonResponse = response.body().string();

                // Procesar la respuesta para obtener el token
                String token = obtenerTokenDesdeJsonResponse(jsonResponse);

                // Realizar otra solicitud para obtener las incidencias utilizando el token
                obtenerIncidenciasConToken(token, textView);
            }
        });
    }

    // Método para procesar el JSON response y obtener el token
    private String obtenerTokenDesdeJsonResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String token = jsonObject.getString("token");
            return token;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Método para obtener las incidencias con el token
    private void obtenerIncidenciasConToken(String token, TextView textView) {
        // Aquí debes implementar la lógica para realizar otra solicitud con el token
        // para obtener las incidencias desde tu API
        // Puedes utilizar OkHttpClient para realizar la segunda llamada, similar a como se hizo en este método
        // y luego procesar las incidencias recibidas
    }
}
