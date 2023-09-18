package com.sewon.officehealth.screen.device

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.R
import com.sewon.officehealth.common.theme.checkedBorderColor
import com.sewon.officehealth.common.theme.checkedThumbColor
import com.sewon.officehealth.common.theme.checkedTrackColor
import com.sewon.officehealth.common.theme.uncheckedBorderColor
import com.sewon.officehealth.common.theme.uncheckedThumbColor
import com.sewon.officehealth.common.theme.uncheckedTrackColor
import com.sewon.officehealth.screen.device.components.BluetoothSampleBox
import com.sewon.officehealth.screen.device.components.DeviceItem
import com.sewon.officehealth.screen.device.components.FindDevicesScreen


@SuppressLint("MissingPermission")
@Composable
fun DeviceList(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  viewModel: ViewModelUserSetting = hiltViewModel()
) {

  Column(
    modifier = modifier.padding(
      start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp
    ),
  ) {
    Spacer(modifier = Modifier.height(20.dp))
    Column(
      verticalArrangement = Arrangement.spacedBy(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(id = R.mipmap.ic_image_5_foreground),
        contentDescription = "Logo",
        modifier = Modifier
          .size(width = 400.dp, height = 150.dp)
      )
      Image(
        painter = painterResource(id = R.drawable.ic_bluetooth_wing),
        contentDescription = "Logo",
      )
      BluetoothSampleBox {
        FindDevicesScreen(navController)
      }
    }

//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

  }

}

@Preview
@Composable
fun PreviewUserSetting() {
  DeviceList()
}