package com.example.traficoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usuario = findViewById(R.id.loginTxtEmail);

        DataManager dataManager = new DataManager(this);

        //Boton del login
        Button loginButton = findViewById(R.id.loginBtnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataManager.countById(String.valueOf(usuario.getText())) > 0){
                    loginCorrecto(String.valueOf(usuario.getText()));
                }else{
                    Toast.makeText(LoginActivity.this, "Login Invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Boton de registro
        Button registroButton = findViewById(R.id.registroButton);
        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUsuario();
            }
        });

    }

    private void createUsuario() {
        //Intent para crear el usuario
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    private void loginCorrecto(String usuario) {
        //Intent para pasar al perfil
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
}