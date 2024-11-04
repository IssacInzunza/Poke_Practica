package com.example.practica11;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TiendaActivity extends AppCompatActivity {
    Almacenamiento almacenamiento = new Almacenamiento(this);
    PokeApiService pokeApiService = new PokeApiService();
    private static final String TAG = "TiendaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mostrarDinero();
    }

    public void mostrarDinero(){
        int dinero = almacenamiento.obtenerDinero();
        TextView txtDinero = findViewById(R.id.txt_Dinero);
        txtDinero.setText("Dinero Acumulado : " + dinero);
    }

    public void pokemonList(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Obtener la lista de Pokémon con paginación
                String jsonResult = pokeApiService.getPokemonList(10, 0); // Cambia el límite y offset según necesites
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                // Obtener el LinearLayout del ScrollView donde se añadirán los botones
                LinearLayout layout = findViewById(R.id.scroll_layout);

                // Crear y añadir un botón para cada Pokémon
                runOnUiThread(() -> {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        try {
                            JSONObject pokemon = resultsArray.getJSONObject(i);
                            String name = pokemon.getString("name");
                            String url = pokemon.getString("url");

                            // Crear un botón con el nombre del Pokémon
                            Button button = new Button(this);
                            button.setText(name);
                            button.setGravity(Gravity.CENTER);
                            button.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            // Imagen del sprite usando Glide
                            ImageView spriteImageView = new ImageView(this);
                            Glide.with(this).load(getPokemonSpriteUrl(url)).into(spriteImageView);

                            // Añadir botón e imagen al layout
                            layout.addView(spriteImageView);
                            layout.addView(button);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error al parsear JSON", e);
                        }
                    }
                });
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error al obtener la lista de Pokémon", e);
            }
        });
    }


}