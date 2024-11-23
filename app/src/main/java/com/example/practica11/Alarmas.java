package com.example.practica11;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class Alarmas {
    public void scheduleMidnightMoneyAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MoneyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Configura el calendario para medianoche
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Si ya pasó la medianoche, avanza al día siguiente
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Usar setInexactRepeating para alarmas repetitivas
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,               // Despierta el dispositivo si está en reposo
                calendar.getTimeInMillis(),            // Hora de inicio
                AlarmManager.INTERVAL_DAY,             // Intervalo diario
                pendingIntent                          // PendingIntent asociado
        );
    }
}
