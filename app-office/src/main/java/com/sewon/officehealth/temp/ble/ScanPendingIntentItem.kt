package com.sewon.officehealth.temp.ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.le.BluetoothLeScanner
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
fun ScanPendingIntentItem(scanner: BluetoothLeScanner) {
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
        Text(isScheduled.toString())
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
      BLEDeviceItem(bluetoothDevice = it.device, isSampleServer = true, onConnect = {})
    }
  }
}