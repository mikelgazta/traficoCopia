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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.loginTxtEmail);
        passwordEditText = findViewById(R.id.loginTxtPwd);
    }

    private void setupListeners() {
        Button loginButton = findViewById(R.id.loginBtnLogin);
        TextView registerTextView = findViewById(R.id.loginLinkLogin);

        registerTextView.setOnClickListener(view -> navigateToRegister());
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email y password son requeridos");
            return;
        }

        CredencialesLogin credenciales = new CredencialesLogin(email, password);
        String jsonCredenciales = gson.toJson(credenciales);
        RequestBody body = RequestBody.create(jsonCredenciales, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8000/api/login")
                .post(body)
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
                    handleLoginSuccess(responseBody);
                } else {
                    handleLoginFailure(responseBody);
                }
            }
        });
    }

    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show());
    }

    private void handleLoginSuccess(String responseBody) {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        intent.putExtra("usuario", responseBody);
        startActivity(intent);
    }

    private void handleLoginFailure(String responseBody) {
        showAlert("Error", responseBody);
    }

    // Clase para representar las credenciales
    private static class CredencialesLogin {
        private String email;
        private String contrasena;

        public CredencialesLogin(String email, String contrasena) {
            this.email = email;
            this.contrasena = contrasena;
        }
    }
}
