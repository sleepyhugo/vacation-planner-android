package com.example.myapplication.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_KIND  = "extra_kind"; // "STARTING" or "ENDING"
    public static final String CHANNEL_ID  = "vacation_alerts";

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(EXTRA_TITLE);
        String kind  = intent.getStringExtra(EXTRA_KIND);

        // Handle excursion alerts
        if (title == null) {
            title = intent.getStringExtra("key"); // passed from scheduleExcursionAlert()
            kind  = "starting";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID,  "Vacation & Excursion Alerts", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(ch);
        }

        String msg = "Vacation \"" + title + "\" is " +
                (kind != null ? kind.toLowerCase() : "due") + " today";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Vacation Alert")
                .setContentText(msg)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            int granted = androidx.core.content.ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.POST_NOTIFICATIONS);
            if (granted != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                android.util.Log.w("AlertReceiver", "Notification permission not granted; skipping notify()");
                return;
            }
        }

        if (!androidx.core.app.NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            android.util.Log.w("AlertReceiver", "Notifications disabled by user/OS; skipping notify()");
            return;
        }

        try {
            androidx.core.app.NotificationManagerCompat.from(context)
                    .notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException se) {
            android.util.Log.e("AlertReceiver", "SecurityException posting notification", se);
        }
    }
}