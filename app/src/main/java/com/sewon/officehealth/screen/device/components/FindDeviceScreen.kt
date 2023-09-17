package com.sewon.officehealth.screen.device.components

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.common.AppDestinations
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.UUID


@SuppressLint("InlinedApi", "MissingPermission")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
internal fun FindDevicesScreen(navController: NavHostController = rememberNavController()) {
  val context = LocalContext.current
  val adapter = checkNotNull(context.getSystemService<BluetoothManager>()?.adapter)
  var scanning by remember {
    mutableStateOf(true)
  }
  val devices = remember {
    mutableStateListOf<BluetoothDevice>()
  }
  val pairedDevices = remember {
    // Get a list of previously paired devices
    mutableStateListOf<BluetoothDevice>(*adapter.bondedDevices.toTypedArray())
  }
  val sampleServerDevices = remember {
    mutableStateListOf<BluetoothDevice>()
  }
  val scanSettings: ScanSettings = ScanSettings.Builder()
    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
    .build()

  // This effect will start scanning for devices when the screen is visible
  // If scanning is stop removing the effect will stop the scanning.
  if (scanning) {
    BluetoothScanEffect(
      scanSettings = scanSettings,
      onScanFailed = {
        scanning = false
        Timber.tag("Timber").w("Scan failed with error: %s", it)
      },
      onDeviceFound = { scanResult ->
        if (!devices.contains(scanResult.device)) {
          if (scanResult.device.name != null) {
            devices.add(scanResult.device)
          }
        }
        val SERVICE_UUID: UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb")

        // If we find our GATT server sample let's highlight it
        val serviceUuids = scanResult.scanRecord?.serviceUuids.orEmpty()
        if (serviceUuids.contains(ParcelUuid(SERVICE_UUID))) {
          if (!sampleServerDevices.contains(scanResult.device)) {
            sampleServerDevices.add(scanResult.device)
          }
        }
      },
    )
    // Stop scanning after a while
    LaunchedEffect(true) {
      delay(15000)
      scanning = false
    }
  }

  Column(Modifier.fillMaxSize()) {
    Row(
      Modifier
        .fillMaxWidth()
        .height(54.dp)
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(text = "Available devices", style = MaterialTheme.typography.titleSmall)
      if (scanning) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
      } else {
        IconButton(
          onClick = {
            devices.clear()
            scanning = true
          },
        ) {
          Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
        }
      }
    }

    LazyColumn(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      if (devices.isEmpty()) {
        item {
          Text(text = "No devices found")
        }
      }
      items(devices) { item ->
        DeviceItem(
          Color(0xFFE3ECA6),
          bluetoothDevice = item,
          isSampleServer = sampleServerDevices.contains(item),
        )
      }

      item {
        Button(onClick = { navController.navigate(AppDestinations.ACTIVITY_ROUTE) }) {
          Text("Move")
        }
      }

      if (pairedDevices.isNotEmpty()) {
        item {
          Text(text = "Saved devices", style = MaterialTheme.typography.titleSmall)
        }
        items(pairedDevices) {
          DeviceItem(
            Color(0xFFE3ECA6),
            bluetoothDevice = it,
          )
        }
      }
    }
  }

}