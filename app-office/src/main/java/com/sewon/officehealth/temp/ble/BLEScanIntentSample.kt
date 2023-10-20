package com.sewon.officehealth.temp.ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import timber.log.Timber


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
//  PermissionScreen(permissions = permissions) {
//    BLEScanIntentScreen()
//  }
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

@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
fun startScan1(
  context: Context,
  scanner: BluetoothLeScanner,
): PendingIntent? {
  Timber.tag("Timber").d("start scan")
  val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
  val bluetoothAdapter: BluetoothAdapter = bluetoothManager.getAdapter()

  val bleDeviceListAdapter = BLEDeviceListAdapter()
// Device scan callback.
  val leScanCallback: ScanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      super.onScanResult(callbackType, result)
      Timber.d(result.device.address)
      bleDeviceListAdapter.addDevice(result.device)
      bleDeviceListAdapter.notifyDataSetChanged()
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
//  val SERVICE_UUID: UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb")
  // We only want the devices running our GATTServerSample
  val scanFilters = listOf(
    ScanFilter.Builder()
//      .setServiceUuid(ParcelUuid(SERVICE_UUID))
      .build(),
  )
  scanner.startScan(scanFilters, scanSettings, resultIntent)
  return resultIntent
}


@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
fun startScan(
  context: Context,
  scanner: BluetoothLeScanner,
): PendingIntent? {


  val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
  val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
  val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner


  val scanSettings: ScanSettings = ScanSettings.Builder()
    // There are other modes that might work better depending on the use case
    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
    // If not set the results will be batch while the screen is off till the screen is turned one again
    .setReportDelay(3000)
    // Use balanced, when in background it will be switched to low power
    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
    .build()

  // Create the pending intent that will be invoked when the scan happens and the filters matches
  val resultIntent = PendingIntent.getBroadcast(
    context,
    1,
    Intent(context, BLEScanReceiver::class.java),
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
  )
//  val SERVICE_UUID: UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb")
  Timber.tag("MPB").d("Scanning aasdfadsf!")

  // We only want the devices running our GATTServerSample
  val scanFilters = listOf(
    ScanFilter.Builder()
//      .setServiceUuid(ParcelUuid(SERVICE_UUID))
      .build(),
  )
  bluetoothLeScanner.startScan(null, null, resultIntent)
  return resultIntent
}

