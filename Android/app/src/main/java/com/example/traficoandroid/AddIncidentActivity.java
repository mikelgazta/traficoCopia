package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddIncidentActivity extends AppCompatActivity {

    private EditText tipoEditText;
    private EditText causaEditText;
    private EditText comienzoEditText;
    private EditText nvlIncidenciaEditText;
    private EditText carreteraEditText;
    private EditText direccionEditText;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_incidencia);

        // Obtener datos de latitud y longitud del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
        }

        // Inicializar vistas
        tipoEditText = findViewById(R.id.tipoEditText);
        causaEditText = findViewById(R.id.causaEditText);
        comienzoEditText = findViewById(R.id.comienzoEditText);
        nvlIncidenciaEditText = findViewById(R.id.nvlIncidenciaEditText);
        carreteraEditText = findViewById(R.id.carreteraEditText);
        direccionEditText = findViewById(R.id.direccionEditText);
        Button guardarButton = findViewById(R.id.guardarButton);

        // Configurar listener para el botón de enviar
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });
    }

    private void enviarDatos() {
        // Obtener valores de los campos de texto
        String tipo = tipoEditText.getText().toString();
        String causa = causaEditText.getText().toString();
        String comienzo = comienzoEditText.getText().toString();
        String nvlIncidencia = nvlIncidenciaEditText.getText().toString();
        String carretera = carreteraEditText.getText().toString();
        String direccion = direccionEditText.getText().toString();

        // Construir el objeto JSON con los datos
        Map<String, Object> incidenteMap = new HashMap<>();
        incidenteMap.put("TIPO", tipo);
        incidenteMap.put("CAUSA", causa);
        incidenteMap.put("COMIENZO", comienzo);
        incidenteMap.put("NVL_INCIDENCIA", nvlIncidencia);
        incidenteMap.put("CARRETERA", carretera);
        incidenteMap.put("DIRECCION", direccion);
        incidenteMap.put("LATITUD", latitude);
        incidenteMap.put("LONGITUD", longitude);
        incidenteMap.put("USUARIO", "mikelgazta@gmail.com");

        // Convertir el mapa a JSON
        String jsonBody = new Gson().toJson(incidenteMap);

        // Realizar la solicitud HTTP con OkHttp
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/api/crearIncidencia")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                // Manejar el fallo, por ejemplo, mostrar un mensaje al usuario
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(AddIncidentActivity.this, "Incidencia registrada correctamente", Toast.LENGTH_SHORT).show();
                        // Puedes finalizar esta actividad o realizar cualquier otra acción necesaria
                        Intent intent = new Intent(AddIncidentActivity.this, MapActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    // La solicitud no fue exitosa (código de respuesta HTTP diferente de 2xx)
                    // Por ejemplo, mostrar un mensaje de error al usuario indicando que ha ocurrido un problema al registrar la incidencia
                    runOnUiThread(() -> {
                        try {
                            String errorMessage = response.body().string();
                            Toast.makeText(AddIncidentActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

        });
    }
}
