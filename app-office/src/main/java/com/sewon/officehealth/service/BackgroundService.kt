package com.sewon.officehealth.service

import android.app.Service
import android.content.Intent
import android.os.IBinder


class BackgroundSoundService : Service() {
  override fun onBind(arg0: Intent?): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return START_STICKY
  }


  fun onUnBind(arg0: Intent?): IBinder? {
    // TO DO Auto-generated method
    return null
  }

  fun onStop() {}
  fun onPause() {}
  override fun onDestroy() {
  }

  override fun onLowMemory() {}

  companion object {
    private val TAG: String? = null
  }
}