package com.example.traficoandroid;

import static com.example.traficoandroid.MapActivity.REQUEST_ADD_INCIDENT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traficoandroid.models.DataItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddIncidentActivity extends AppCompatActivity {

    private Spinner tipoSpinner;
    private EditText causaEditText;
    private EditText comienzoEditText;
    private EditText nvlIncidenciaEditText;
    private EditText carreteraEditText;
    private EditText direccionEditText;
    private double latitude;
    private double longitude;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_incidencia);

        // Obtener datos de latitud y longitud del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("LATITUD");
            longitude = extras.getDouble("LONGITUD");
            token = extras.getString("token");
        }

        // Inicializar vistas
        tipoSpinner = findViewById(R.id.tipoSpinner);
        causaEditText = findViewById(R.id.causaEditText);
        comienzoEditText = findViewById(R.id.comienzoEditText);
        nvlIncidenciaEditText = findViewById(R.id.nvlIncidenciaEditText);
        carreteraEditText = findViewById(R.id.carreteraEditText);
        direccionEditText = findViewById(R.id.direccionEditText);
        Button guardarButton = findViewById(R.id.guardarButton);
        Button cancelarButton = findViewById(R.id.cancelarButton);

        // Configurar listener para el botón de enviar
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });

        // Configurar listener para el botón de cancelar
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente regresa a la actividad anterior (MapActivity) sin guardar nada
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    private void enviarDatos() {
        // Obtener valores de los campos de texto y spinner
        String tipo = tipoSpinner.getSelectedItem().toString();
        String causa = causaEditText.getText().toString();
        String comienzo = comienzoEditText.getText().toString();
        String nvlIncidencia = nvlIncidenciaEditText.getText().toString();
        String carretera = carreteraEditText.getText().toString();
        String direccion = direccionEditText.getText().toString();

        // Construir el objeto JSON
        JsonObject incidenteJson = new JsonObject();
        incidenteJson.addProperty("TIPO", tipo);
        incidenteJson.addProperty("CAUSA", causa);
        incidenteJson.addProperty("COMIENZO", comienzo);
        incidenteJson.addProperty("NVL_INCIDENCIA", nvlIncidencia);
        incidenteJson.addProperty("CARRETERA", carretera);
        incidenteJson.addProperty("DIRECCION", direccion);
        incidenteJson.addProperty("LATITUD", latitude);
        incidenteJson.addProperty("LONGITUD", longitude);
        incidenteJson.addProperty("USUARIO", "mikelgazta@gmail.com");

        String jsonBody = incidenteJson.toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/crearIncidencia")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token) // Utilizar el token obtenido previamente
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(AddIncidentActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Intent returnIntent = new Intent();
                        // Si DataItem implementa Serializable, de lo contrario conviértelo a JSON
                        // returnIntent.putExtra("dataItem", item); // Directamente si es serializable
                        returnIntent.putExtra("nuevaIncidencia", responseBody); // Si usas JSON
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(AddIncidentActivity.this, "Error al registrar la incidencia: " + responseBody, Toast.LENGTH_SHORT).show());
                }
            }

        });
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
}

