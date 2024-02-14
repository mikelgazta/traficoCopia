package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmarPasswordEditText;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.regTxtEmail);
        passwordEditText = findViewById(R.id.regTxtPwd);
        confirmarPasswordEditText = findViewById(R.id.regTxtConfirmPwd);
    }

    private void setupListeners() {
        Button registerButton = findViewById(R.id.btnRegistro);
        ImageView backImageView = findViewById(R.id.regImgLogo);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmarPasswordEditText.getText().toString();
            registerUser(email, password, confirmPassword);
        });

        backImageView.setOnClickListener(view -> navigateToLogin());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void registerUser(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden.");
            return;
        }

        MediaType mediaType = MediaType.parse("application/json");
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/register")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showAlert("Error", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    showAlert("Registro Exitoso", "Usuario registrado correctamente.");

                    // Enviar correo de bienvenida
                    CorreoRequest correoRequest = new CorreoRequest();
                    correoRequest.setDestinatario(email);
                    correoRequest.setAsunto("¡Bienvenido a nuestra aplicación!");
                    correoRequest.setCuerpo("¡Gracias por registrarte en nuestra aplicación!");

                    // Crear la solicitud para enviar el correo
                    RequestBody correoBody = RequestBody.create(correoRequest.toString(), MediaType.parse("application/json"));

                    Request requestM = new Request.Builder()
                            .url("http://10.0.2.2:8000/api/enviarCorreo")
                            .post(correoBody)
                            .build();

                    client.newCall(requestM).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            showAlert("Error al enviar el correo de bienvenida", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                showAlert("Correo enviado", "Se ha enviado un correo de bienvenida al usuario.");
                            } else {
                                showAlert("Error al enviar el correo de bienvenida", "No se pudo enviar el correo de bienvenida.");
                            }
                        }
                    });

                    navigateToLogin();
                } else {
                    showAlert("Error al registrar", responseBody);
                }
            }
        });
    }


    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(RegistroActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show());
    }
}
