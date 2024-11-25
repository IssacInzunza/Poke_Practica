package com.example.practica11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Alarmas alarmas = new Alarmas();
        alarmas.scheduleMidnightMoneyAlarm(this);
    }

    public void abrirTienda(View view){
        Intent intent = new Intent(this, TiendaActivity.class);
        startActivity(intent);
    }

    public void abrirChamba(View view){
        Intent intent = new Intent(this, TrabajoActivity.class);
        startActivity(intent);
    }

    public void abrirInventario(View view){
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
    }
}