package com.sewon.officehealth

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.sewon.officehealth.screen.RootCompose
import com.sewon.officehealth.service.ble.ListenerBleData
import com.sewon.officehealth.service.ble.ServiceBleHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  companion object {
    lateinit var serviceBleHandler: ServiceBleHandler
    var listenerBleData = ListenerBleData()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide()
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

//    startService(Intent(applicationContext, SerialService::class.java))
    setContent {
      RootCompose {
        finish()
      }
    }
  }

  override fun onStart() {
    super.onStart()
    val intent = Intent(this, ServiceBleHandler::class.java)
    startService(intent)
    bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
  }

  override fun onStop() {
    super.onStop()
    unbindService(mServiceConnection)
  }


  private val mServiceConnection: ServiceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName) {
//      TODO: serviceBleHandler = null
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      serviceBleHandler = (service as ServiceBleHandler.SerialBinder).service
      serviceBleHandler.attach(listenerBleData)
    }
  }
}