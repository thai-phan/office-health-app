package com.sewon.officehealth

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.sewon.officehealth.common.OfficeHealth
import com.sewon.officehealth.service.ble.DataListener
import com.sewon.officehealth.service.ble.SerialService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  companion object {
    lateinit var serialService: SerialService

  }

  var dataListener: DataListener = DataListener()



  private enum class Connected {
    False, Pending, True
  }

  private var connected = Connected.False


  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

//    startService(Intent(applicationContext, SerialService::class.java))


    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH_SCAN,
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      setContent {
//        FindBLEDevicesSample(appContext)
        OfficeHealth {
          finish()
        }

      }
    } else {
      setContent {
        OfficeHealth {
          finish()
        }
//        BLEScanIntentSample()
//        SoundUI(context = LocalContext.current)
//        FindBLEDevicesSample(onConnectBLE = {
//          connect(it)
//        }, appContext)
      }
    }
  }

  override fun onStart() {
    super.onStart()
    val intent = Intent(this, SerialService::class.java)
    startService(intent)
    bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
  }

  override fun onStop() {
    super.onStop()
//    if (mServiceBound) {
    unbindService(mServiceConnection)
//      mServiceBound = false
  }

  private fun disconnect() {
    connected = Connected.False
    serialService.disconnect()
  }


  private val mServiceConnection: ServiceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName) {
//      TODO mServiceBound = false

    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      Timber.tag("Timber").d("onServiceConnected")
      serialService = (service as SerialService.SerialBinder).service
      serialService.attach(dataListener)
      val myBinder = service
//      TODO mServiceBound = true

    }
  }
}