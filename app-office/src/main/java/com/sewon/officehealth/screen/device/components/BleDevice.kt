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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.screen.AppDestinations
import com.sewon.officehealth.service.ble.LowEnergyGatt
import timber.log.Timber


@SuppressLint("MissingPermission")
@Composable
fun BleDevice(
  color: Color,
  navController: NavHostController = rememberNavController(),
  bluetoothDevice: BluetoothDevice,
) {
  val context = LocalContext.current

  fun connect(address: String) {
    try {
      val adapter = context.getSystemService<BluetoothManager>()?.adapter
      val device = adapter?.getRemoteDevice(address)
      val socket = device?.let {
        LowEnergyGatt(context, it)
      }
      if (socket != null) {
        MainActivity.lowEnergyService.connect(socket)
      }
      navController.navigate(AppDestinations.ACTIVITY_ROUTE)
    } catch (exception: IllegalArgumentException) {
      Timber.tag("Timber").w(exception)
    }
    // connect to the GATT server on the device
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
        .fillMaxWidth()
        .shadow(elevation = 10.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .height(80.dp)
        .background(color = color, shape = RoundedCornerShape(size = 10.dp))
        .padding(10.dp)
        .clickable {
          connect(bluetoothDevice.address)
        },
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(Icons.Filled.Bluetooth, contentDescription = "Localized description", tint = Color.Black)
      Text(
        bluetoothDevice.name ?: "N/A",
        style = TextStyle(fontWeight = FontWeight.Normal),
        color = Color.Black
      )
      Text(text = bluetoothDevice.address, color = Color.Black)
    }
  }
}

