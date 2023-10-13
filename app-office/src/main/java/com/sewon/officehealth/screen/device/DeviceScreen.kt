package com.sewon.officehealth.screen.device

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.device.components.BluetoothWrapper
import com.sewon.officehealth.screen.device.components.BleFindDevices


@SuppressLint("MissingPermission")
@Composable
fun DeviceList(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  viewModel: ViewModelUserSetting = hiltViewModel()
) {

  Column(
    modifier = modifier
      .systemBarsPadding()
      .statusBarsPadding()
      .padding(
        horizontal = 20.dp,
      ),
  ) {
    Spacer(modifier = Modifier.height(20.dp))
    Column(
      verticalArrangement = Arrangement.spacedBy(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(id = R.mipmap.ic_ohealp_foreground),
        contentDescription = "Logo",
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      )
      Image(
        painter = painterResource(id = R.drawable.ic_bluetooth_wing),
        contentDescription = "Logo",
      )
      BluetoothWrapper {
        BleFindDevices(navController)
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