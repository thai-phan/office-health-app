/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sewon.officehealth.screen.device

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.sewon.officehealth.screen.demo.SoundService
import com.sewon.officehealth.screen.device.ble.BLEDeviceItem
import com.sewon.officehealth.screen.device.ble.BluetoothSampleBox
import com.sewon.officehealth.screen.device.ble.SerialService
import com.sewon.officehealth.screen.device.ble.SerialSocket
import com.sewon.officehealth.service.MyService
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.UUID

@SuppressLint("MissingPermission")
@Composable
fun FindBLEDevicesSample(appContext: Context) {



  val service = SerialService()

  val context = LocalContext.current
  val adapter = checkNotNull(context.getSystemService<BluetoothManager>()?.adapter)
  val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_START) {
//        context.startService(Intent(context, SoundService::class.java))
        context.startService(Intent(context, SerialService::class.java))
      } else if (event == Lifecycle.Event.ON_STOP) {
        context.stopService(Intent(context, SerialService::class.java))
      }
    }

    // Add the observer to the lifecycle
    lifecycleOwner.lifecycle.addObserver(observer)

    // When the effect leaves the Composition, remove the observer
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }

  }

  fun onConnect(bluetoothDevice: BluetoothDevice) {
    adapter.let { adapter ->
      try {
        val device = adapter.getRemoteDevice(bluetoothDevice.address)

        val socket = SerialSocket(context, device)
        service.connect(socket)

      } catch (exception: IllegalArgumentException) {
        Timber.tag("Timber").w(exception)
      }
      // connect to the GATT server on the device
    }
  }


  BluetoothSampleBox {
    FindDevicesScreen(onConnect = { onConnect(it) })
  }
}

@SuppressLint("InlinedApi", "MissingPermission")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
internal fun FindDevicesScreen(onConnect: (BluetoothDevice) -> Unit) {
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
        BLEDeviceItem(
          bluetoothDevice = item,
          isSampleServer = sampleServerDevices.contains(item),
          onConnect = onConnect,
        )
      }

      if (pairedDevices.isNotEmpty()) {
        item {
          Text(text = "Saved devices", style = MaterialTheme.typography.titleSmall)
        }
        items(pairedDevices) {
          BLEDeviceItem(
            bluetoothDevice = it,
            onConnect = onConnect,
          )
        }
      }
    }
  }

}

@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
fun BluetoothScanEffect(
  scanSettings: ScanSettings,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  onScanFailed: (Int) -> Unit,
  onDeviceFound: (device: ScanResult) -> Unit,
) {
  val context = LocalContext.current
  val adapter = context.getSystemService<BluetoothManager>()?.adapter

  if (adapter == null) {
    onScanFailed(ScanCallback.SCAN_FAILED_INTERNAL_ERROR)
    return
  }

  val currentOnDeviceFound by rememberUpdatedState(onDeviceFound)

  DisposableEffect(lifecycleOwner, scanSettings) {
    val leScanCallback: ScanCallback = object : ScanCallback() {
      override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        currentOnDeviceFound(result)
      }

      override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        onScanFailed(errorCode)
      }
    }

    val observer = LifecycleEventObserver { _, event ->
      // Start scanning once the app is in foreground and stop when in background
      if (event == Lifecycle.Event.ON_START) {
        adapter.bluetoothLeScanner.startScan(null, scanSettings, leScanCallback)
      } else if (event == Lifecycle.Event.ON_STOP) {
        adapter.bluetoothLeScanner.stopScan(leScanCallback)
      }
    }

    // Add the observer to the lifecycle
    lifecycleOwner.lifecycle.addObserver(observer)

    // When the effect leaves the Composition, remove the observer and stop scanning
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
      adapter.bluetoothLeScanner.stopScan(leScanCallback)
    }
  }
}
