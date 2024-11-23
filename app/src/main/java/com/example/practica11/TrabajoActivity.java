package com.example.practica11;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrabajoActivity extends AppCompatActivity {
    Almacenamiento almacenamiento;
    TextView txtDinero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trabajo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtDinero = findViewById(R.id.txt_Dinero);
        almacenamiento = new Almacenamiento(this);
        almacenamiento.mostrarDinero(txtDinero);
    }

    public void aplastar(View view) {
        int dinero = almacenamiento.obtenerDinero();
        dinero += 1;
        almacenamiento.guardarDinero(dinero);
        almacenamiento.mostrarDinero(txtDinero);
    }

    protected void onPause() {
        super.onPause();
        int dinero = almacenamiento.obtenerDinero();
        almacenamiento.guardarDinero(dinero);
    }
    }