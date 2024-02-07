package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Método onCreate() llamado cuando se crea la actividad.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupListeners();
    }

    // Método para inicializar las vistas (encontrar las vistas en el diseño y asignarlas a variables).
    private void initializeViews() {
        emailEditText = findViewById(R.id.loginTxtEmail);
        passwordEditText = findViewById(R.id.loginTxtPwd);
    }

    // Método para configurar los listeners para los elementos de la interfaz de usuario.
    private void setupListeners() {
        Button loginButton = findViewById(R.id.loginBtnLogin);
        TextView registerTextView = findViewById(R.id.loginLinkLogin);

        // Listener para el enlace de registro que abre la actividad de registro cuando se hace clic.
        registerTextView.setOnClickListener(view -> navigateToRegister());

        // Listener para el botón de inicio de sesión que llama al método loginUser() cuando se hace clic.
        loginButton.setOnClickListener(v -> loginUser());
    }

    // Método para navegar a la actividad de registro.
    private void navigateToRegister() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    // Método para realizar el inicio de sesión del usuario.
    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Verificar si los campos de correo electrónico y contraseña están vacíos.
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email y password son requeridos"); // Mostrar un mensaje de error si están vacíos.
            return;
        }

        // Crear un objeto CredencialesLogin con el correo electrónico y la contraseña.
        CredencialesLogin credenciales = new CredencialesLogin(email, password);
        // Convertir el objeto CredencialesLogin a JSON.
        String jsonCredenciales = gson.toJson(credenciales);
        // Crear el cuerpo de la solicitud HTTP con el JSON.
        RequestBody body = RequestBody.create(jsonCredenciales, MediaType.parse("application/json; charset=utf-8"));

        // Construir la solicitud HTTP POST con la URL de inicio de sesión y el cuerpo de la solicitud.
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/api/login")
                .post(body)
                .build();

        // Enviar la solicitud HTTP asíncronamente y manejar las respuestas en los métodos de Callback.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showAlert("Error", e.getMessage()); // Mostrar un mensaje de error en caso de fallo.
            }
            //Metodos para manejar las respuestas del servidor
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    handleLoginSuccess(responseBody);
                } else {
                    handleLoginFailure(responseBody);
                }
            }
        });
    }

    // Método para mostrar un cuadro de diálogo con un título y un mensaje.
    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show());
    }

    // Método para manejar el inicio de sesión exitoso.
    private void handleLoginSuccess(String responseBody) {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        intent.putExtra("usuario", responseBody);
        startActivity(intent);
    }

    // Método para manejar el inicio de sesión fallido.
    private void handleLoginFailure(String responseBody) {
        showAlert("Error", responseBody);
    }

    // Clase interna para representar las credenciales de inicio de sesión.
    private static class CredencialesLogin {
        private String email;
        private String contrasena;

        // Constructor de la clase CredencialesLogin.
        public CredencialesLogin(String email, String contrasena) {
            this.email = email;
            this.contrasena = contrasena;
        }
    }
}
