package com.example.practica11;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BienvenidaActivity extends AppCompatActivity {

    Button siguiente;
    private static final String CHANNEL_ID = "bienvenida_channel"; // ID del canal de notificación
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bienvenida);

        // Obtener el nombre de usuario pasado por el Intent
        String username = getIntent().getStringExtra("username");

        // Configurar el texto de bienvenida
        TextView welcomeTextView = findViewById(R.id.textView3);
        if (username != null) {
            welcomeTextView.setText("¡Bienvenido, " + username + "!");
        } else {
            welcomeTextView.setText("¡Bienvenido!");
        }

        // Configurar el botón para redirigir a MainActivity
        siguiente = findViewById(R.id.button4);
        siguiente.setOnClickListener(view -> {
            // Crear un Intent para ir a MainActivity
            Intent intent = new Intent(BienvenidaActivity.this, MainActivity.class);

            // Iniciar MainActivity
            startActivity(intent);

            // Destruir esta actividad
            finish();
        });
        showNotification(username);


        // Ajustes para el sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showNotification(String username) {
        // Verificar si la aplicación tiene permiso para enviar notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Si no tiene permiso, solicitamos el permiso
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
                return; // Salimos del método sin mostrar la notificación hasta que se otorgue el permiso
            }
        }

        // Crear el canal de notificación (necesario a partir de Android 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bienvenida Channel";
            String description = "Canal para mostrar notificación de bienvenida";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Crear un Intent para abrir una página web en el navegador
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pokemon.com/el"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Generar la notificación con el PendingIntent
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.lock) // Asegúrate de tener este ícono en los recursos
                .setContentTitle("¡Te estábamos esperando!")
                .setContentText("Ya póngase a poke chambear, " + (username != null ? username : "usuario") + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Asocia el PendingIntent a la notificación
                .setAutoCancel(true) // Esto hará que la notificación desaparezca al hacer clic
                .build();

        // Mostrar la notificación
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // Si el permiso fue concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Llamar nuevamente al método para mostrar la notificación
                String username = getIntent().getStringExtra("username");
                showNotification(username);
            } else {
                // Si el permiso fue denegado, puedes mostrar un mensaje
                Toast.makeText(this, "No se concedió permiso para mostrar notificaciones", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
