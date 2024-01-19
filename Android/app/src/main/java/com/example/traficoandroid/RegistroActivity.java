package com.example.traficoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegistroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        EditText usuario = findViewById(R.id.regTxtEmail);
        EditText contrasena = findViewById(R.id.regTxtPwd);
        DataManager dataManager = new DataManager(this);

        //Boton de deshacer
        Button deshacerRegistro = findViewById(R.id.deshacerButton);
        deshacerRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamada al metodo para iniciar el boton.
                volverLogin();
            }
        });

        //Boton de guardar
        Button guardarRegistro = findViewById(R.id.btnRegistro);
        guardarRegistro.setOnClickListener(view -> {
            if (dataManager.countById(String.valueOf(usuario.getText()))==0) {
                if (validateLogin(String.valueOf(usuario.getText()))) {
                    if (validatePassword(String.valueOf(contrasena.getText()))) {
                        dataManager.insertUsuario(new Usuario(String.valueOf(usuario.getText()), String.valueOf(contrasena.getText())));
                        //guardarNuevoUsuario();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(this, "La contraseña tiene que tener entre 5 y 20 caracteres", Toast.LENGTH_SHORT).show();

                } else Toast.makeText(this, "Correo mal introducido", Toast.LENGTH_SHORT).show();

            }else Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();

        });
    }
    private void guardarNuevoUsuario() {
        DataManager dataManager = new DataManager(this);
        String usuario = ((android.widget.EditText) findViewById(R.id.usuarioText)).getText().toString();
        String contrasena = ((android.widget.EditText) findViewById(R.id.contrasenaText)).getText().toString();

        dataManager.insertUsuario(new Usuario(usuario, contrasena));
        Log.e("Usuario insertado correctamente.", "Usuario: "+ usuario +", Contrasena: "+ contrasena);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    private void volverLogin() {
        //Intent para empezar el juego
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public boolean validateLogin(String login) {
        if (login.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")) return true;
        else return false;

    }

    public boolean validatePassword(String password) {
        if (password.length()>=5 && password.length()<=20) return true;
        else return false;
    }
}
