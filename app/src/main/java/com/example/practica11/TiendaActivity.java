package com.example.practica11;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText edtBusqueda;
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
        //Componentes graficos
        txtDinero = findViewById(R.id.txt_Dinero);
        txtPagina = findViewById(R.id.txt_Pagina);
        edtBusqueda = findViewById(R.id.edt_Busqueda);

        //Llamada de clases
        almacenamiento = new Almacenamiento(this);
        pokeApiService = new PokeApiService();
        almacenamiento.mostrarDinero(txtDinero);

        //Llamada de funciones
        pokemonList(numeroPagina);
    }

    public void buscarPokemon(View view) {

        // Obtener el texto ingresado por el usuario
        String pokemonNameOrId = edtBusqueda.getText().toString().trim();
        edtBusqueda.setText("");

        // Obtener el LinearLayout para la lista de Pokémon
        LinearLayout layout = findViewById(R.id.scroll_layout);

        // Limpiar el contenido previo
        layout.removeAllViews();

        // Si el usuario ingresó algo, buscar el Pokémon específico
        if (!pokemonNameOrId.isEmpty()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    // Obtener los datos del Pokémon por nombre o ID
                    String pokemonData = pokeApiService.getPokemonData(pokemonNameOrId);
                    JSONObject jsonObject = new JSONObject(pokemonData);

                    // Obtener información del Pokémon
                    String name = jsonObject.getString("name");
                    String spriteUrl = jsonObject.getJSONObject("sprites").getString("front_default");

                    // Actualizar la UI en el hilo principal
                    runOnUiThread(() -> {
                        // Mostrar el sprite del Pokémon
                        ImageView spriteImageView = new ImageView(this);
                        Glide.with(this).load(spriteUrl).into(spriteImageView);
                        layout.addView(spriteImageView);

                        // Crear un botón para el nombre del Pokémon
                        Button nameButton = new Button(this);
                        nameButton.setText(name);
                        nameButton.setGravity(Gravity.CENTER);
                        nameButton.setTextSize(18);

                        // Configurar el evento de clic para el botón
                        nameButton.setOnClickListener(v -> {
                            if (almacenamiento.obtenerDinero() >= 100) {
                                almacenamiento.gastarDinero(100);
                                almacenamiento.mostrarDinero(txtDinero);

                                // Mostrar un mensaje de compra exitosa (opcional)
                                Toast.makeText(this, "¡Has comprado a " + name + "!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Mostrar un Toast si no hay suficiente dinero
                                Toast.makeText(this, "No tienes suficiente dinero para comprar este Pokémon.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Agregar el botón al layout
                        layout.addView(nameButton);
                    });

                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Error al buscar el Pokémon", e);

                    // Mostrar mensaje de error en la UI
                    runOnUiThread(() -> {
                        TextView errorTextView = new TextView(this);
                        errorTextView.setText("Pokémon no encontrado.");
                        errorTextView.setGravity(Gravity.CENTER);
                        layout.addView(errorTextView);
                    });
                }
            });
        } else {
            // Si no se ingresó texto, mostrar la lista de Pokémon
            pokemonList(numeroPagina);
        }
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

    public void lista(View view){
        pokemonList(numeroPagina);
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
                String jsonResult = pokeApiService.getPokemonList(10, numeroPagina);
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
                                if (almacenamiento.obtenerDinero() >= 100) {
                                    almacenamiento.gastarDinero(100);
                                    almacenamiento.mostrarDinero(txtDinero);

                                    // Mostrar un mensaje de compra exitosa (opcional)
                                    Toast.makeText(this, "¡Has comprado a " + name + "!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Mostrar un Toast si no hay suficiente dinero
                                    Toast.makeText(this, "No tienes suficiente dinero para comprar este Pokémon.", Toast.LENGTH_SHORT).show();
                                }
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