package com.example.practica11;
import android.content.Context;
import android.content.SharedPreferences;

public class Almacenamiento {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "mx.example.android.pokeProyecto";

    public Almacenamiento(Context context) {
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
}
