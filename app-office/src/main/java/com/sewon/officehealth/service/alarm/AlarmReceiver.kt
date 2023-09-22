package com.sewon.officehealth.service.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Vibrator
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {
  companion object {
    lateinit var ringtone: Ringtone
  }

  override fun onReceive(context: Context, intent: Intent) {
    val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(4000)
    Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()
    var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    if (alarmUri == null) {
      alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
    // setting default ringtone
    ringtone = RingtoneManager.getRingtone(context, alarmUri)
    // play ringtone
    ringtone.play()
  }
}