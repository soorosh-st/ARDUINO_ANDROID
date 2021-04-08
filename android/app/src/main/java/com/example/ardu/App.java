package com.example.ardu;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String Channel_ID="exampleID";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationchannel();
    }

    private void createNotificationchannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel servicechannel = new NotificationChannel(
                    Channel_ID, "example service channel", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(servicechannel);
        }
    }
}
