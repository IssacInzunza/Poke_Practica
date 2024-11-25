package com.example.practica11;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Almacenamiento {

    private final SharedPreferences mPreferences;
    private String sharedPrefFile = "mx.example.android.pokeProyecto";
    private ArrayList<String> inventarioList = new ArrayList<>();

    public Almacenamiento(Context context) {
        // Inicializa SharedPreferences en el constructor
        mPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
    }

    //Zona de dinero
    public void guardarDinero(int dinero) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("dinero", dinero);
        editor.apply();
    }

    public int obtenerDinero() {
        return mPreferences.getInt("dinero", 0);
    }

    public void gastarDinero(int costo) {
        int dineroActual = obtenerDinero();
        if (dineroActual >= costo) {
            dineroActual -= costo;
            guardarDinero(dineroActual);
        }
    }

    public void mostrarDinero(TextView txtDinero) {
        int dinero = obtenerDinero();
        txtDinero.setText("Dinero Acumulado: " + dinero);
    }

    //Zona de inventario
    public void guardarInventario(String inventario) {
        SharedPreferences.Editor editor = mPreferences.edit();
        inventarioList = mostrarInventario();
        inventarioList.add(inventario);

        Gson gson = new Gson();
        String json = gson.toJson(inventarioList);

        editor.putString("pokemones", json);
        editor.apply();
    }

    public ArrayList<String> mostrarInventario() {
        String json = mPreferences.getString("pokemones", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            inventarioList = gson.fromJson(json, type);
            return inventarioList;
        } else {
            return new ArrayList<>();
        }
    }

}
