package com.sewon.officehealth.screen.device.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@SuppressLint("MissingPermission")
@Composable
fun BLEDeviceItem(
  bluetoothDevice: BluetoothDevice,
  isSampleServer: Boolean = false,
  onConnect: (BluetoothDevice) -> Unit,
) {
  Row(
    modifier = Modifier
      .padding(vertical = 8.dp)
      .fillMaxWidth()
      .clickable { onConnect(bluetoothDevice) },
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
}