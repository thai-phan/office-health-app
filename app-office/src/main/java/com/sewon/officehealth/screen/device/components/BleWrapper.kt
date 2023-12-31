package com.sewon.officehealth.screen.device.components

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import timber.log.Timber


/**
 * This composable wraps the permission logic and checks if bluetooth it's available and enabled
 */
@Composable
fun BluetoothWrapper(
  modifier: Modifier = Modifier,
  extraPermissions: Set<String> = emptySet(),
  content: @Composable BoxScope.(BluetoothAdapter) -> Unit,
) {
  val context = LocalContext.current
  val packageManager = context.packageManager
  val bluetoothAdapter = context.getSystemService<BluetoothManager>()?.adapter

  // If we derive physical location from BT devices or if the device runs on Android 11 or below
  // we need location permissions otherwise we don't need to request them (see AndroidManifest).
  val locationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    setOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION,
    )
  } else {
    setOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
    )
  }
  val buildVersion = Build.VERSION.SDK_INT
  Timber.tag("buildVersion").d(buildVersion.toString())
  val bluetoothPermissionSet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    setOf(
      Manifest.permission.BLUETOOTH_CONNECT,
      Manifest.permission.BLUETOOTH_SCAN,
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN,
      Manifest.permission.POST_NOTIFICATIONS,
    )
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    setOf(
      Manifest.permission.BLUETOOTH_CONNECT,
      Manifest.permission.BLUETOOTH_SCAN,
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN,
    )
  } else {
    setOf(
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN,
    )
  }


  val permissionFinal = (bluetoothPermissionSet + locationPermission + extraPermissions).toList()

  PermissionScreen(
    modifier = modifier,
    permissions = permissionFinal,
    contentAlignment = Alignment.Center,
  ) {
    // Check to see if the Bluetooth classic feature is available.
    val hasBT = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    // Check to see if the BLE feature is available.
    val hasBLE = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    // Check if the adapter is enabled
    var isBTEnabled by remember {
      mutableStateOf(bluetoothAdapter?.isEnabled == true)
    }

    when {
      bluetoothAdapter == null || !hasBT -> MissingFeatureText(text = "No bluetooth available")
      !hasBLE -> MissingFeatureText(text = "No bluetooth low energy available")
      !isBTEnabled -> BluetoothDisabledScreen { isBTEnabled = true }
      else -> content(bluetoothAdapter)
    }
  }
}

@Composable
fun BluetoothDisabledScreen(onEnabled: () -> Unit) {
  val result =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == Activity.RESULT_OK) {
        onEnabled()
      }
    }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(text = "Bluetooth is disabled")
    Button(
      onClick = {
        result.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
      },
    ) {
      Text(text = "Enable")
    }
  }
}

@Composable
private fun MissingFeatureText(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.headlineSmall,
    color = MaterialTheme.colorScheme.error,
  )
}
