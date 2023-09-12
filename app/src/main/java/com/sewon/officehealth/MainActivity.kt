package com.sewon.officehealth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.sewon.officehealth.screen.device.ble.BLEScanIntentSample
import com.sewon.officehealth.screen.device.ble.FindBLEDevicesSample
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


  private val BLE_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
  )

  @RequiresApi(api = Build.VERSION_CODES.S)
  private val ANDROID_12_BLE_PERMISSIONS = arrayOf(
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT
  )

  @RequiresApi(Build.VERSION_CODES.S)
  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            requestMultiplePermissions.launch(arrayOf(
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT))
//        }
//        else{
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            requestBluetooth.launch(enableBtIntent)
//        }

//        val requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // feature requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            }

//        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
//        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
//        if (bluetoothAdapter == null) {
//            // Device doesn't support Bluetooth
//
//        }
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            val pairedDevices: Set<BluetoothDevice>? =
//                bluetoothAdapter?.bondedDevices
//            pairedDevices?.forEach { device ->
//                val deviceName = device.name
//                val deviceHardwareAddress = device.address // MAC address
//            }
//
//            return
//        }


//        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                Log.e("Activity result", "OK")
//                // There are no request codes
//                val data = result.data
//            }
//        }
//
//        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        activityResultLauncher.launch(intent)


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//    val PERMISSION_ALL = 1
//    val PERMISSIONS = arrayOf(
//      Manifest.permission.BLUETOOTH,
//      Manifest.permission.BLUETOOTH_ADMIN,
//      Manifest.permission.BLUETOOTH_ADVERTISE,
//      Manifest.permission.BLUETOOTH_CONNECT,
//      Manifest.permission.BLUETOOTH_PRIVILEGED,
//      Manifest.permission.BLUETOOTH_SCAN
//    )
//
//    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
//      ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    if (!hasPermissions(this, *PERMISSIONS)) {
//      ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
//    }

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH_SCAN,
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      setContent {
        FindBLEDevicesSample()
      }

//            OfficeHealth {
//                finish()
//            }

    } else {
      setContent {
//        BLEScanIntentSample()
        FindBLEDevicesSample()
      }
    }
  }
}