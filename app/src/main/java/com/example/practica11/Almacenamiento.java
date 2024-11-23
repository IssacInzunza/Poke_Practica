package com.example.practica11;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

public class Almacenamiento {

    private final SharedPreferences mPreferences;
    private String sharedPrefFile = "mx.example.android.pokeProyecto";

    public Almacenamiento(Context context) {
        // Inicializa SharedPreferences en el constructor
        mPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
    }
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
}
