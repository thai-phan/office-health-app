package com.sewon.officehealth

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.sewon.officehealth.common.RootCompose
import com.sewon.officehealth.service.ble.ListenerBleData
import com.sewon.officehealth.service.ble.ServiceBleHandle
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  companion object {
    lateinit var serviceBleHandle: ServiceBleHandle
    var listenerBleData = ListenerBleData()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
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
    val intent = Intent(this, ServiceBleHandle::class.java)
    startService(intent)
    bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
  }

  override fun onStop() {
    super.onStop()
    unbindService(mServiceConnection)
  }


  private val mServiceConnection: ServiceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName) {
//      TODO: mServiceBound = false
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      Timber.tag("Timber").d("onServiceConnected")
      serviceBleHandle = (service as ServiceBleHandle.SerialBinder).service
      serviceBleHandle.attach(listenerBleData)
    }
  }
}