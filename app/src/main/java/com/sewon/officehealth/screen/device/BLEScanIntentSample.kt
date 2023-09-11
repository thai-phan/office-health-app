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
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.sewon.officehealth.screen.permission.PermissionScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.UUID


@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
fun BLEScanIntentSample() {
  val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
    permissions.add(Manifest.permission.BLUETOOTH)
    permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
    permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
  }
  PermissionScreen(permissions = permissions) {
    Text("asdasdf")
    Text("asdasdf")
    BLEScanIntentScreen()
  }
}

@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
private fun BLEScanIntentScreen() {
  val context = LocalContext.current
  val scanner = context.getSystemService<BluetoothManager>()?.adapter?.bluetoothLeScanner
  if (scanner != null) {
    Column(
      Modifier
        .fillMaxSize()
        .padding(16.dp),
    ) {
      ScanPendingIntentItem(scanner)
    }
  } else {
    Text(text = "Bluetooth Scanner not found")
  }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
private fun ScanPendingIntentItem(scanner: BluetoothLeScanner) {
  val context = LocalContext.current
  var pendingIntent by remember {
    mutableStateOf(
      PendingIntent.getBroadcast(
        context,
        1,
        Intent(context, BLEScanReceiver::class.java),
        // Using the FLAG_NO_CREATE so it returns null if the PI is not there
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE,
      ),
    )
  }
  // If the PendingIntent is null, it means it's not registered
  val isScheduled = pendingIntent != null

  val devices by BLEScanReceiver.devices.collectAsState()
  LazyColumn {
    stickyHeader {
      Row(
        Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = "Scan even if app is not alive")
        Button(
          onClick = {
            pendingIntent = if (isScheduled) {
              pendingIntent.cancel()
              null
            } else {
              startScan(context, scanner)
            }
          },
        ) {
          if (isScheduled) {
            Text(text = "Stop scanning")
          } else {
            Text(text = "Schedule Scan")
          }
        }
      }
    }
    items(devices) {
      BluetoothDeviceItem(bluetoothDevice = it.device, isSampleServer = true, onConnect = {})
    }
  }
}

@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
private fun startScan(
  context: Context,
  scanner: BluetoothLeScanner,
): PendingIntent? {
  Timber.d("asdfadf")
  val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
  val bluetoothAdapter: BluetoothAdapter = bluetoothManager.getAdapter()

  val leDeviceListAdapter = LeDeviceListAdapter()
// Device scan callback.
  val leScanCallback: ScanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      super.onScanResult(callbackType, result)
      Timber.d(result.device.address)
      leDeviceListAdapter.addDevice(result.device)
      leDeviceListAdapter.notifyDataSetChanged()
    }
  }


  val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
  var scanning = false
  val handler = Handler()
  val scanSettings: ScanSettings = ScanSettings.Builder()
    // There are other modes that might work better depending on the use case
    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
    // If not set the results will be batch while the screen is off till the screen is turned one again
    .setReportDelay(3000)
    // Use balanced, when in background it will be switched to low power
    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
    .build()
  // Stops scanning after 10 seconds.
  val SCAN_PERIOD: Long = 10000

  fun scanLeDevice() {
    if (!scanning) { // Stops scanning after a pre-defined scan period.
      handler.postDelayed({
        bluetoothLeScanner.stopScan(leScanCallback)
      }, SCAN_PERIOD)
      scanning = true
      bluetoothLeScanner.startScan(leScanCallback)
    } else {
      scanning = false
      bluetoothLeScanner.stopScan(leScanCallback)
    }
  }

  scanLeDevice()
//    val scanSettings: ScanSettings = ScanSettings.Builder()
//        // There are other modes that might work better depending on the use case
//        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//        // If not set the results will be batch while the screen is off till the screen is turned one again
//        .setReportDelay(3000)
//        // Use balanced, when in background it will be switched to low power
//        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
//        .build()

  // Create the pending intent that will be invoked when the scan happens and the filters matches
  val resultIntent = PendingIntent.getBroadcast(
    context,
    1,
    Intent(context, BLEScanReceiver::class.java),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
  )
  val SERVICE_UUID: UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb")
  // We only want the devices running our GATTServerSample
  val scanFilters = listOf(
    ScanFilter.Builder()
      .setServiceUuid(ParcelUuid(SERVICE_UUID))
      .build(),
  )
  scanner.startScan(scanFilters, scanSettings, resultIntent)
  return resultIntent
}

class BLEScanReceiver : BroadcastReceiver() {

  companion object {
    // Static StateFlow that caches the list of scanned devices used by our sample
    // This is an **anti-pattern** used for demo purpose and simplicity
    val devices = MutableStateFlow(emptyList<ScanResult>())
  }

  override fun onReceive(context: Context, intent: Intent) {
    val results = intent.getScanResults()
    Timber.tag("MPB").d("Devices found: %s", results.size)

    // Update our results cached list
    if (results.isNotEmpty()) {
      devices.update { scanResults ->
        (scanResults + results).distinctBy { it.device.address }
      }
    }
  }

  /**
   * Extract the list of scan result from the intent if available
   */
  private fun Intent.getScanResults(): List<ScanResult> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getParcelableArrayListExtra(
        BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT,
        ScanResult::class.java,
      )
    } else {
      @Suppress("DEPRECATION")
      getParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)
    } ?: emptyList()
}


@SuppressLint("MissingPermission")
@Composable
internal fun BluetoothDeviceItem(
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

