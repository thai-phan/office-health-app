package com.sewon.officehealth.screen.device

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.sewon.officehealth.screen.device.components.FindDevicesScreen
import com.sewon.officehealth.screen.device.components.BluetoothSampleBox

@SuppressLint("MissingPermission")
@Composable
fun FindBLEDevicesSample() {
  BluetoothSampleBox {
    FindDevicesScreen()
  }
}


