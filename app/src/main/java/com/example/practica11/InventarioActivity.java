package com.example.practica11;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventarioActivity extends AppCompatActivity {
    Almacenamiento almacenamiento;
    TextView txtDinero;
    ArrayList<String> inventario;

    PokeApiService pokeApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtDinero = findViewById(R.id.txt_Dinero);
        almacenamiento = new Almacenamiento(this);
        almacenamiento.mostrarDinero(txtDinero);
        pokeApiService = new PokeApiService();
        inventario = almacenamiento.mostrarInventario();
        mostrarInventario(inventario);
    }

    private void mostrarInventario(ArrayList<String> inventario) {
        // Crear el ExecutorService para ejecutar tareas en segundo plano
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Ejecutar el código en un hilo de fondo
        executor.execute(() -> {
            // Esta parte se ejecutará en un hilo secundario para evitar bloquear la UI
            LinearLayout layout = findViewById(R.id.inventario_layout);

            // Limpiar el layout antes de agregar nuevos elementos
            layout.removeAllViews();

            // Iterar sobre la lista de inventario
            for (String pokemonName : inventario) {
                try {
                    // Obtener la URL del sprite
                    String spriteUrl = pokeApiService.getPokemonSpriteUrl(pokemonName);

                    // Crear el contenedor de la carta
                    LinearLayout cardLayout = new LinearLayout(this);
                    cardLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            300 // Altura fija para las cartas
                    );
                    cardParams.setMargins(16, 16, 16, 16); // Margen entre cartas
                    cardLayout.setLayoutParams(cardParams);
                    cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                    // Crear y agregar el ImageView para el sprite
                    ImageView spriteImageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0, 1 // Ocupa 1/3 del espacio
                    );
                    spriteImageView.setLayoutParams(imageParams);

                    // Cargar el sprite en el ImageView utilizando runOnUiThread para asegurarse que se ejecute en el hilo principal
                    runOnUiThread(() -> {
                        Glide.with(this).load(spriteUrl).into(spriteImageView);
                    });

                    cardLayout.addView(spriteImageView);

                    // Crear y agregar el TextView para el nombre
                    TextView nameTextView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0, 2 // Ocupa 2/3 del espacio
                    );
                    nameTextView.setLayoutParams(textParams);
                    nameTextView.setText(pokemonName.toUpperCase());
                    nameTextView.setGravity(Gravity.CENTER);
                    nameTextView.setTextSize(18);
                    cardLayout.addView(nameTextView);

                    // Esta parte de agregar la carta al layout debe hacerse en el hilo principal
                    runOnUiThread(() -> layout.addView(cardLayout));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}