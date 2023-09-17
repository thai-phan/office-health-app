package com.sewon.officehealth.screen.device

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.screen.device.components.FindDevicesScreen
import com.sewon.officehealth.screen.device.components.BluetoothSampleBox

@SuppressLint("MissingPermission")
@Composable
fun FindBLEDevicesSample(
  navController: NavHostController = rememberNavController()
) {
  BluetoothSampleBox {
    FindDevicesScreen(navController)
  }
}


