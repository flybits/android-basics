package com.flybits.samples.android.basics;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BasicsApplication extends Application {

    public static final String NOTIFICATION_CHANNEL_FLYBITS = "com.flybits.push.channel.generic";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_FLYBITS, "Android Basics", importance);
            mChannel.setDescription("Notifications for Android Basics App");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }
    }
}
