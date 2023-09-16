package com.sewon.officehealth

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import com.sewon.officehealth.common.OfficeHealth
import com.sewon.officehealth.screen.device.FindBLEDevicesSample
import com.sewon.officehealth.service.ble.DataListener
import com.sewon.officehealth.service.ble.SerialService
import com.sewon.officehealth.service.ble.SerialSocket
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  companion object {
    public var test = "123123"

  }


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

  }

  override fun onStop() {
    super.onStop()
//    if (mServiceBound) {
//      mServiceBound = false
  }

  private fun disconnect() {
    connected = Connected.False
  }



}