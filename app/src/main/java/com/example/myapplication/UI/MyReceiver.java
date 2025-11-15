package com.example.myapplication.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

public class MyReceiver extends BroadcastReceiver {
    String channel_id = "test";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context,channel_id);
        }
        Notification n=new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("NotificationTest").build();
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++,n);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context, String CHANNEL_ID){
        CharSequence name="mychannelname";
        String description="mychanneldescription";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel= new NotificationChannel(CHANNEL_ID,name,importance);
        channel.setDescription(description);
        NotificationManager notificationManager=context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}