package com.sewon.officehealth.temp.bound;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Chronometer;

import timber.log.Timber;

public class BoundService extends Service {

  private final String TAG = this.getClass().getName();
  private final IBinder mBinder = new MyBinder();
  private Chronometer mChronometer;

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.tag(TAG).v("in onCreate");
    mChronometer = new Chronometer(this);
    mChronometer.setBase(SystemClock.elapsedRealtime());
    mChronometer.start();
  }

  @Override
  public IBinder onBind(Intent intent) {
    Timber.tag(TAG).v("in onBind");
    return mBinder;
  }

  @Override
  public void onRebind(Intent intent) {
    Timber.tag(TAG).v("in onRebind");
    super.onRebind(intent);
  }

  @Override
  public boolean onUnbind(Intent intent) {
    Timber.tag(TAG).v("in onUnbind");
    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Timber.tag(TAG).v("in onDestroy");
    mChronometer.stop();
  }

  public String getTimestamp() {
    long elapsedMillis = SystemClock.elapsedRealtime()
        - mChronometer.getBase();
    int hours = (int) (elapsedMillis / 3600000);
    int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
    int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
    int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
    return hours + ":" + minutes + ":" + seconds + ":" + millis;
  }

  public class MyBinder extends Binder {
    BoundService getService() {
      return BoundService.this;
    }
  }
}