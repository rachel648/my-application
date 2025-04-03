// NotificationReceiver.java
package com.example.yogademoapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    private void showNotification(Context context) {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_add_alert_24)
                .setContentTitle("iMental Reminder")
                .setContentText("It's time for your scheduled activity!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}