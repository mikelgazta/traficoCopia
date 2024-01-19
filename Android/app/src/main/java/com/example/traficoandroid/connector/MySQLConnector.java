package com.example.traficoandroid.connector;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MySQLConnector extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://127.0.0.1/tu_ruta_api");
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            Log.e("MySQLConnector", "Error en la conexión a la base de datos", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Maneja el resultado (parsing JSON, etc.)
        Log.d("MySQLConnector", "Resultado: " + result);
    }
}

