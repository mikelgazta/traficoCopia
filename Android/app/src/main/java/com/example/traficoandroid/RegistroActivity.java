package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traficoandroid.Beans.Usuario;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroActivity extends AppCompatActivity {

    // Declaración de variables de vistas y objetos necesarios
    private EditText nombreEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmarPasswordEditText;
    private final OkHttpClient client = new OkHttpClient(); // Cliente HTTP para realizar llamadas a la API
    private final Gson gson = new Gson(); // Objeto Gson para convertir objetos a JSON y viceversa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialización de vistas y configuración de listeners
        initializeViews();
        setupListeners();
    }

    // Método para inicializar las vistas
    private void initializeViews() {
        nombreEditText = findViewById(R.id.regTxtName);
        emailEditText = findViewById(R.id.regTxtEmail);
        passwordEditText = findViewById(R.id.regTxtPwd);
        confirmarPasswordEditText = findViewById(R.id.regTxtConfirmPwd);
    }

    // Método para configurar los listeners de los botones
    private void setupListeners() {
        Button registerButton = findViewById(R.id.btnRegistro);
        ImageView backImageView = findViewById(R.id.regImgLogo);

        // Listener para el botón de registro
        registerButton.setOnClickListener(v -> registerUser());

        // Listener para el botón de retroceso (back)
        backImageView.setOnClickListener(view -> navigateToLogin());
    }

    // Método para navegar a la actividad de inicio de sesión
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Método para registrar al usuario
    private void registerUser() {
        // Obtener los datos ingresados por el usuario
        final String nombre = nombreEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String confirmarPassword = confirmarPasswordEditText.getText().toString();

        // Validar que todos los campos estén completos
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            showAlert("Error", "Por favor, complete todos los campos.");
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmarPassword)) {
            showAlert("Error", "Las contraseñas no coinciden.");
            return;
        }

        // Crear objeto Usuario con los datos ingresados
        Usuario usuario = new Usuario(email, password);

        // Convertir usuario a JSON usando Gson
        String jsonUsuario = gson.toJson(usuario);

        // Crear el cuerpo de la solicitud HTTP
        RequestBody body = RequestBody.create(jsonUsuario, MediaType.parse("application/json; charset=utf-8"));

        // Crear la solicitud HTTP POST
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/api/register") // Reemplaza con la URL de tu API de registro
                .post(body)
                .build();

        // Realizar la llamada a la API de registro
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Mostrar mensaje de error en caso de fallo
                showAlert("Error", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    // Mostrar mensaje de éxito en caso de registro exitoso
                    showAlert("Registro Exitoso", "Usuario registrado correctamente.");
                    // Navegar a la actividad de inicio de sesión
                    navigateToLogin();
                } else {
                    // Mostrar mensaje de error en caso de fallo en el registro
                    showAlert("Error al registrar", responseBody);
                }
            }
        });
    }

    // Método para mostrar un cuadro de diálogo de alerta con un título y un mensaje
    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(RegistroActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show());
    }
}
