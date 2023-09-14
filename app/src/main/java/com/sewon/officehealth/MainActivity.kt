package com.sewon.officehealth

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import com.sewon.officehealth.screen.device.FindBLEDevicesSample
import com.sewon.officehealth.screen.device.ble.DataListener
import com.sewon.officehealth.screen.device.ble.SerialService
import com.sewon.officehealth.screen.device.ble.SerialSocket
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


  lateinit var serialService: SerialService

  var dataListener: DataListener = DataListener()


  private var initialStart = true


  private enum class Connected {
    False, Pending, True
  }

  private var connected = Connected.False

  private val a = 10000L
  private val b = 1000L
  private val c = mutableLongStateOf(a)

  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

//    startService(Intent(applicationContext, SerialService::class.java))

    val appContext = applicationContext



    val timer = object : CountDownTimer(a, b) {
      override fun onTick(millisUntilFinished: Long) {
        Timber.tag("MYLOG").d("text updated programmatically")
        c.longValue = millisUntilFinished
      }

      override fun onFinish() {
        c.longValue = 0
      }
    }
    timer.start()
    setContent {
      CountDown()
    }
//    if (ActivityCompat.checkSelfPermission(
//        this,
//        Manifest.permission.BLUETOOTH_SCAN,
//      ) != PackageManager.PERMISSION_GRANTED
//    ) {
//      setContent {
////        FindBLEDevicesSample(appContext)
////        OfficeHealth {
////          finish()
////        }
//        FindBLEDevicesSample(onConnectBLE = {
//          connect(it)
//        }, appContext)
//      }
//    } else {
//      setContent {
////        OfficeHealth {
////          finish()
////        }
////        BLEScanIntentSample()
////        SoundUI(context = LocalContext.current)
//        CountDown()
////        FindBLEDevicesSample(onConnectBLE = {
////          connect(it)
////        }, appContext)
//      }
//    }
  }

  @Composable
  fun CountDown() {
    val milliseconds by c
    val text = (milliseconds / 1000).toString()
    Column {
      Text("asdasdasd")
      Text(text)
      Text(text)
      Text(text)
      Text("asdfa1231")
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

  private fun connect(address: String) {
    try {
      val adapter = getSystemService<BluetoothManager>()?.adapter
      val device = adapter?.getRemoteDevice(address)
      val socket = SerialSocket(applicationContext, device)
      serialService.connect(socket)

    } catch (exception: IllegalArgumentException) {
      Timber.tag("Timber").w(exception)
    }
    // connect to the GATT server on the device
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
      serialService = (service as SerialService.SerialBinder).getService()
      serialService.attach(dataListener)
      val myBinder = service
//      TODO mServiceBound = true

    }
  }
}