package com.sewon.officehealth.screen.device.ble

import timber.log.Timber
import java.lang.Exception
import java.util.ArrayDeque

class DataListener : SerialListener {
  override fun onSerialConnect() {
    TODO("Not yet implemented")
  }

  override fun onSerialConnectError(e: Exception?) {
    Timber.tag("Timber").d("onSerialConnectError")
    TODO("Not yet implemented")
  }

  override fun onSerialRead(data: ByteArray?) {
    Timber.tag("Timber").d("onSerialRead")
  }

  override fun onSerialRead(datas: ArrayDeque<ByteArray>?) {
    Timber.tag("Timber").d("onSerialRead")
  }

  override fun onSerialIoError(e: Exception?) {
    Timber.tag("Timber").d("onSerialRead")
  }
}