package com.sewon.officehealth.screen.device.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sewon.officehealth.service.ble.SerialService
import com.sewon.officehealth.service.ble.SerialSocket
import timber.log.Timber
import androidx.core.content.getSystemService


@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(
  color: Color,
               bluetoothDevice: BluetoothDevice,
               isSampleServer: Boolean = false,
               ) {

  val context = LocalContext.current

  fun connect(address: String) {
    try {
      val adapter = context.getSystemService<BluetoothManager>()?.adapter
      val device = adapter?.getRemoteDevice(address)
      val socket = SerialSocket(context, device)
      SerialService().SerialBinder().service.connect(socket)

    } catch (exception: IllegalArgumentException) {
      Timber.tag("Timber").w(exception)
    }
    // connect to the GATT server on the device
  }

  Row(
    modifier = Modifier
      .padding(vertical = 8.dp)
      .fillMaxWidth()
      .clickable { connect(bluetoothDevice.address) },
    horizontalArrangement = Arrangement.SpaceBetween,
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
    Text(bluetoothDevice.address)
    val state = when (bluetoothDevice.bondState) {
      BluetoothDevice.BOND_BONDED -> "Paired"
      BluetoothDevice.BOND_BONDING -> "Pairing"
      else -> "None"
    }
    Text(text = state)

  }

  Row(
    modifier = Modifier
      .shadow(elevation = 10.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
      .width(320.dp)
      .height(80.dp)
      .background(color = color, shape = RoundedCornerShape(size = 10.dp)),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
    Text("Device name")
    Button(onClick = { connect("C4:4D:84:69:DA:CE") }) {

    }
    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
  }
}