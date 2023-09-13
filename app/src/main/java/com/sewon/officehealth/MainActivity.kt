package com.sewon.officehealth

import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.sewon.officehealth.screen.device.FindBLEDevicesSample
import com.sewon.officehealth.screen.device.ble.SerialListener
import com.sewon.officehealth.screen.device.ble.SerialService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.ArrayDeque


@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection, SerialListener {
  private var serialService: SerialService? = null
  private var initialStart = true

  private enum class Connected {
    False, Pending, True
  }

  private var connected = Connected.False

  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)



//    startService(Intent(applicationContext, SoundService::class.java))

    val appContext = applicationContext

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH_SCAN,
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      setContent {
        FindBLEDevicesSample(appContext)
      }

//            OfficeHealth {
//                finish()
//            }

    } else {
      setContent {
//        BLEScanIntentSample()
//        SoundUI(context = LocalContext.current)
        FindBLEDevicesSample(appContext)
      }
    }
  }

  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    Timber.tag("Timber").d("onServiceConnected")

    serialService = (service as SerialService.SerialBinder).getService()
    serialService?.attach(this)
//    if (initialStart) {
//      initialStart = false
//      getActivity().runOnUiThread(Runnable { this.connect() })
//    }
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    TODO("Not yet implemented")
  }

  override fun onSerialConnect() {
    connected = Connected.True
  }

  override fun onSerialConnectError(e: Exception?) {
    Timber.tag("Timber").d("onSerialConnectError")
    TODO("Not yet implemented")
  }

  override fun onSerialRead(data: ByteArray?) {
    Timber.tag("Timber").d("onSerialRead")
  }

  override fun onSerialRead(datas: ArrayDeque<ByteArray>?) {
    Timber.tag("Timber").d("onSerialRead")
  }

  override fun onSerialIoError(e: Exception?) {
    Timber.tag("Timber").d("onSerialRead")
  }
}