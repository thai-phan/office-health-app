package com.sewon.officehealth.temp.foreground;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import androidx.core.app.NotificationCompat;

import com.sewon.officehealth.R;

public class ForegroundService extends Service {
  public static final String CHANNEL_ID = "ForegroundServiceChannel";

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    String input = intent.getStringExtra("inputExtra");
    createNotificationChannel();
    Intent notificationIntent = new Intent(this, ForegroundActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this,
        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Foreground Service")
        .setContentText(input)
        .setSmallIcon(R.drawable.ic_intelli)
        .setContentIntent(pendingIntent)
        .build();
    startForeground(1, notification);
    //do heavy work on a background thread
    //stopSelf();
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void createNotificationChannel() {
    NotificationChannel serviceChannel = new NotificationChannel(
        CHANNEL_ID,
        "Foreground Service Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    );
    NotificationManager manager = getSystemService(NotificationManager.class);
    manager.createNotificationChannel(serviceChannel);
  }
}