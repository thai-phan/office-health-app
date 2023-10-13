package com.sewon.officehealth.screen.device.components

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner



@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
fun BluetoothScanning(
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