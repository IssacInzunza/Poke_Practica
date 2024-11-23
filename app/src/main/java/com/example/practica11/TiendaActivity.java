package com.example.practica11;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TiendaActivity extends AppCompatActivity {
    //Componentes graficos
    TextView txtDinero;
    TextView txtPagina;
    Almacenamiento almacenamiento;
    PokeApiService pokeApiService;
    private static final String TAG = "TiendaActivity";
    //Variables
    private int numeroPagina = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);
        //DUDA
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtDinero = findViewById(R.id.txt_Dinero);
        txtPagina = findViewById(R.id.txt_Pagina);
        almacenamiento = new Almacenamiento(this);
        pokeApiService = new PokeApiService();
        almacenamiento.mostrarDinero(txtDinero);
        pokemonList(numeroPagina);
    }

    public void enfrente(View view){
        numeroPagina++;
        pokemonList(numeroPagina);
        txtPagina.setText("Pagina : " + (numeroPagina+1));
    }

    public void atras(View view){
        if(numeroPagina == 0){
            return;
        }
        numeroPagina--;
        pokemonList(numeroPagina);
        txtPagina.setText("Pagina : " + (numeroPagina+1));
    }

    public void pokemonList(int numeroPagina) {
        txtPagina.setText("Pagina : " + (numeroPagina+1));

        // Limpiar el LinearLayout antes de agregar los nuevos Pokémon
        LinearLayout layout = findViewById(R.id.scroll_layout);
        layout.removeAllViews();  // Elimina todos los elementos previos

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Obtener la lista de Pokémon con paginación
                String jsonResult = pokeApiService.getPokemonList(10, numeroPagina); // Cambia el límite y offset según necesites
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                // Crear y añadir un botón para cada Pokémon
                runOnUiThread(() -> {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        try {
                            JSONObject pokemon = resultsArray.getJSONObject(i);
                            String name = pokemon.getString("name");
                            String url = pokemon.getString("url");

                            // Crear un contenedor horizontal para el botón
                            LinearLayout buttonContainer = new LinearLayout(this);
                            buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
                            buttonContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    120 // Altura del contenedor
                            ));

                            // Imagen del sprite usando Glide
                            ImageView spriteImageView = new ImageView(this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                                    0,
                                    120 // La misma altura que el contenedor
                            );
                            imageParams.weight = 1; // 1/3 del ancho del botón
                            spriteImageView.setLayoutParams(imageParams);
                            Glide.with(this).load(pokeApiService.getPokemonSpriteUrl(name)).into(spriteImageView);

                            // Crear el TextView para el nombre del Pokémon (2/3 del botón)
                            TextView textView = new TextView(this);
                            textView.setText(name);
                            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                    0,
                                    120
                            );
                            textParams.weight = 2;
                            textView.setLayoutParams(textParams);
                            textView.setGravity(Gravity.CENTER);

                            // Agregar el ImageView y el TextView al LinearLayout
                            buttonContainer.addView(spriteImageView);
                            buttonContainer.addView(textView);

                            // Crear el botón con el contenedor
                            Button button = new Button(this);
                            button.setText(" ");
                            button.setGravity(Gravity.CENTER);
                            button.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            // Agregar el contenedor al layout principal
                            layout.addView(buttonContainer);
                            buttonContainer.setOnClickListener(v -> {
                                almacenamiento.gastarDinero(100);
                                almacenamiento.mostrarDinero(txtDinero);
                            });

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error al obtener la lista de Pokémon", e);
            }
        });
    }
}