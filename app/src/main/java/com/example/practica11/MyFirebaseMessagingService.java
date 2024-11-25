package com.example.practica11;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";
    private Notificaciones notificaciones = new Notificaciones();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!remoteMessage.getData().isEmpty()) {
            // Si el mensaje contiene datos
            String message = remoteMessage.getData().get("message");
            Log.d(TAG, "Mensaje de datos: " + message);
        }

        if (remoteMessage.getNotification() != null) {
            // Si el mensaje contiene una notificación
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Mostrar la notificación con el contexto de FirebaseMessagingService
            Log.d(TAG, "Notificación: " + title + " - " + body);
            notificaciones.mostrarNotificacion(this, title, body);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Token de registro: " + token);
    }
}
