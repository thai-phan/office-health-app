package com.sewon.officehealth.screen.device

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import com.sewon.officehealth.common.theme.checkedBorderColor
import com.sewon.officehealth.common.theme.checkedThumbColor
import com.sewon.officehealth.common.theme.checkedTrackColor
import com.sewon.officehealth.common.theme.uncheckedBorderColor
import com.sewon.officehealth.common.theme.uncheckedThumbColor
import com.sewon.officehealth.common.theme.uncheckedTrackColor


@Composable
fun DeviceList(
    modifier: Modifier = Modifier,
    viewModel: ViewModelUserSetting = hiltViewModel()
) {

//    val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        permissions.add(Manifest.permission.BLUETOOTH_SCAN)
//    }
//    PermissionBox(permissions = permissions) {
//        BLEScanIntentScreen()
//    }
//
//    val leDeviceListAdapter = LeDeviceListAdapter()
//// Device scan callback.
//    val leScanCallback: ScanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            super.onScanResult(callbackType, result)
//            leDeviceListAdapter.addDevice(result.device)
//            leDeviceListAdapter.notifyDataSetChanged()
//        }
//    }
//
//
//


//    setListAdapter(ArrayAdapter<String>(this, R.layout.list, s))


    Column(
        modifier = modifier.padding(
                start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp
            ),
    ) {
        Text("R.string.setting_title", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

        }

//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

    }

}

@Preview
@Composable
fun PreviewUserSetting() {
    DeviceList()
}