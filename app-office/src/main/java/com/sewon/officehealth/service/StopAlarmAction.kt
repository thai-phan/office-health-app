package com.sewon.officehealth.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sewon.officehealth.MainActivity
import timber.log.Timber


class StopAlarmAction : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Timber.tag("StopAlarmAction").d("stop")
    MainActivity.bleDataListener.stretchStop()
  }
}