package com.example.practica11;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class PokeApiService {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    public String getPokemonData(String pokemonNameOrId) throws IOException {
        String urlString = BASE_URL + "pokemon/" + pokemonNameOrId;

        return makeHttpRequest(urlString);
    }

    public String getPokemonList(int limit, int offset) throws IOException {
        String urlString = BASE_URL + "pokemon?limit=" + limit + "&offset=" + offset;

        return makeHttpRequest(urlString);
    }

    public String getPokemonSpriteUrl(String pokemonNameOrId) throws IOException, JSONException {
        String jsonResult = getPokemonData(pokemonNameOrId);
        JSONObject jsonObject = new JSONObject(jsonResult);

        return jsonObject.getJSONObject("sprites").getString("front_default");
    }

    private String makeHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                throw new IOException("Error en la solicitud: Código de respuesta " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }
}
