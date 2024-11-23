package com.example.practica11;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class MoneyReceiver extends BroadcastReceiver {
    Context context;

    Notificaciones notificaciones = new Notificaciones();
    @Override
    public void onReceive(Context context, Intent intent) {
        Almacenamiento almacenamiento = new Almacenamiento(context);
        int dineroActual = almacenamiento.obtenerDinero();
        almacenamiento.guardarDinero(dineroActual + 1000);

        notificaciones.mostrarNotificacion(context, "Dinero agregado", "Se han agregado 1000 de dinero a tu cuenta");
    }

}
