package com.sewon.officehealth.service.ble

import android.text.SpannableStringBuilder
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.realtime.RealtimeDataObject
import timber.log.Timber
import java.util.ArrayDeque

class ListenerBleData : ISerialListener {
  val TAG = "ListenerBleData"

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
    MainActivity.serviceBleHandler.createTimerNotification()
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