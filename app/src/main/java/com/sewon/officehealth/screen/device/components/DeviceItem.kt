package com.sewon.officehealth.screen.device.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.ble.DataListener
import com.sewon.officehealth.service.ble.SerialService
import com.sewon.officehealth.service.ble.SerialSocket
import timber.log.Timber


@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(
  color: Color,
  bluetoothDevice: BluetoothDevice,
  isSampleServer: Boolean = false,
) {
//  lateinit var serialService: SerialService
//
//  val dataListener = DataListener()

  val context = LocalContext.current


//  val mServiceConnection: ServiceConnection = object : ServiceConnection {
//    override fun onServiceDisconnected(name: ComponentName) {
////      TODO mServiceBound = false
//
//    }
//
//    override fun onServiceConnected(name: ComponentName, service: IBinder) {
//      Timber.tag("Timber").d("onServiceConnected")
//      serialService = (service as SerialService.SerialBinder).getService()
//      serialService.attach(dataListener)
//      val myBinder = service
////      TODO mServiceBound = true
//
//    }
//  }

//  val intent = Intent(context, SerialService::class.java)
//  context.startService(intent)
//  context.bindService(intent, mServiceConnection, ComponentActivity.BIND_AUTO_CREATE)

  fun sendNoti() {
    MainActivity.serialService.createNotificationHealth()
  }


  fun connect(address: String) {
    try {
      val adapter = context.getSystemService<BluetoothManager>()?.adapter
      val device = adapter?.getRemoteDevice(address)
      val socket = SerialSocket(context, device)
      MainActivity.serialService.connect(socket)

    } catch (exception: IllegalArgumentException) {
      Timber.tag("Timber").w(exception)
    }
    // connect to the GATT server on the device
  }

  fun disconnect() {
    MainActivity.serialService.disconnect()
  }

  val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

  DisposableEffect(lifecycleOwner) {
        // When the effect leaves the Composition, remove the observer and stop scanning
    onDispose {

    }
  }

  Row(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .shadow(elevation = 10.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .height(80.dp)
        .background(color = color, shape = RoundedCornerShape(size = 10.dp))
        .clickable { connect(bluetoothDevice.address) },
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        if (isSampleServer) {
          "GATT Sample server"
        } else {
          bluetoothDevice.name ?: "N/A"
        },
        style = if (isSampleServer) {
          TextStyle(fontWeight = FontWeight.Bold)
        } else {
          TextStyle(fontWeight = FontWeight.Normal)
        },
      )
      val state = when (bluetoothDevice.bondState) {
        BluetoothDevice.BOND_BONDED -> "Paired"
        BluetoothDevice.BOND_BONDING -> "Pairing"
        else -> "None"
      }
      Text(text = state)


    }
    Button(onClick = { disconnect() }) {
      Text("Disconnect")
    }
    Button(onClick = { sendNoti() }) {
      Text("Send Noti")
    }
  }


}

