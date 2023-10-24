package com.sewon.officehealth.service.ble

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.text.SpannableStringBuilder
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.realtime.RealtimeDataObject
import timber.log.Timber
import java.util.ArrayDeque

class BleServiceListener : ServiceConnection, ISerialListener {
  val TAG = this.javaClass.name

  private enum class Connected {
    False, Pending, True
  }

  private var connected = Connected.False
  private val hexEnabled = false

  var realtimeDataObject: RealtimeDataObject = RealtimeDataObject()


  fun resetRealtimeDataObject() {
    Timber.tag(TAG).d("resetRealtimeDataObject")
    realtimeDataObject = RealtimeDataObject()
  }

  val log = mutableStateOf("")
  val isStretch = mutableStateOf(false)
  val isStress = mutableStateOf(false)
  val isWrongDeviceType = mutableStateOf(false)

  private var service: BleServiceHandler? = null

  override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
    service = (binder as BleServiceHandler.SerialBinder).service
//    if (initialStart && isResumed()) {
//      initialStart = false
//      getActivity().runOnUiThread(Runnable { this.connect() })
//    }
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    service = null
  }

  override fun onSerialConnect() {
    connected = Connected.True
  }

  override fun onSerialConnectError(e: Exception) {
    connected = Connected.False
  }

  override fun onSerialRead(data: ByteArray) {
    val datas = ArrayDeque<ByteArray>()
    datas.add(data)
    receive(datas)
  }

  override fun onSerialRead(datas: ArrayDeque<ByteArray>) {
    receive(datas)
  }

  override fun onSerialIoError(e: Exception) {
    Timber.tag(TAG).d("onSerialRead")
  }

  fun resetTimer() {
    realtimeDataObject.stretchObj.countDownTimer.cancel()
    realtimeDataObject.stretchObj.countDownTimer.start()
  }

  fun stretchDetected() {
    MainActivity.bleServiceHandler.createTimerNotification()
    resetTimer()
//    MainActivity.serviceBleHandle.playSoundStretch()
//    isStretch.value = true
  }

  fun stretchStop() {
    isStretch.value = false
  }

  fun stressDetected() {
//    MainActivity.serviceBleHandle.createTimerNotification()
    //    Popup show
    isStress.value = true
  }

  fun stressStop() {
    isStress.value = false
  }


  private var topperCount = 0
  private val topperCountLoop = 20

  private val newline = TextUtil.newline_crlf

  fun send(str: String) {
//    if (connected != Connected.True) {
//      Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show()
//      return
//    }
    try {
      val msg: String
      val data: ByteArray
      if (hexEnabled) {
        val sb = StringBuilder()
        TextUtil.toHexString(sb, TextUtil.fromHexString(str))
        TextUtil.toHexString(sb, newline.toByteArray())
        msg = sb.toString()
        data = TextUtil.fromHexString(msg)
      } else {
        data = (str + newline).toByteArray()
      }
      service?.write(data)
    } catch (e: java.lang.Exception) {
      onSerialIoError(e)
    }
  }

  private fun receive(datas: ArrayDeque<ByteArray>) {
    val spn = SpannableStringBuilder()
    for (data in datas) {
      if (hexEnabled) {
        spn.append(TextUtil.toHexString(data)).append('\n')
      } else {
        val dataStr = String(data)
        realtimeDataObject.validateDataFormat(dataStr)
        val text = TextUtil.toCaretString(dataStr, true)
        spn.append(text)
      }
    }

    if (topperCount < topperCountLoop) {
      topperCount += 1
    } else {
      topperCount = 0
    }

    log.value = "$spn $topperCount"
  }


}