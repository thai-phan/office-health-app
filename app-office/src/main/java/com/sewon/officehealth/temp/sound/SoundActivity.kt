package com.sewon.officehealth.temp.sound

import android.R
import android.R.attr.button
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SoundActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_main)
//    button = findViewById<Int>(R.id.notify)
//    val channel = NotificationChannel(
//      "MyCuS Notification",
//      "My Notification",
//      NotificationManager.IMPORTANCE_HIGH
//    )
//    val manager = getSystemService(NotificationManager::class.java)
//    val audioAttributes = AudioAttributes.Builder()
//      .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
//    channel.setSound(
//      Uri.parse("android.resources://" + packageName + "/" + R.raw.bg_reminder_alarm),
//      audioAttributes.build()
//    )
//    manager.createNotificationChannel(channel)
//    button.setOnClickListener(View.OnClickListener {
//      val builder = NotificationCompat.Builder(this@MainActivity, "MyCuS Notification")
//      builder.setContentTitle("MyTitle")
//      builder.setContentText("TESTING")
//      builder.setSmallIcon(R.drawable.ic_launcher_background)
//      builder.setAutoCancel(true)
//      builder.setSound(Uri.parse("android.resources://" + packageName + "/" + R.raw.bg_reminder_alarm))
//      val managerCompat = NotificationManagerCompat.from(this@MainActivity)
//      managerCompat.notify(1, builder.build())
//    })
  }


}