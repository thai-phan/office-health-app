package com.sewon.officehealth.service.ble

import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.stress.StressDetection
import timber.log.Timber
import java.util.ArrayDeque

class BleDataListener : SerialListener {

  private enum class Connected {
    False, Pending, True
  }

  private var connected = Connected.False
  private val hexEnabled = false
  val receiveText: SpannableStringBuilder = SpannableStringBuilder()

  val log = mutableStateOf("")
  val isStretch = mutableStateOf(false)
  val isStress = mutableStateOf(false)
  val isWrongDeviceType = mutableStateOf(false)


  private val totalDuration = 50 * 60 * 1000L
//  private val totalDuration = 10 * 1000L

  private val b = 1000L
  val timeRemaining = mutableLongStateOf(0)


  val countDownTimer = object : CountDownTimer(totalDuration, b) {
    override fun onTick(millisUntilFinished: Long) {
      timeRemaining.longValue = totalDuration - millisUntilFinished
    }

    override fun onFinish() {
      stretchDetected()

      timeRemaining.longValue = 0
    }
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
    Timber.tag("Timber").d("onSerialRead")
  }

  fun resetTimer() {
    countDownTimer.cancel()
    countDownTimer.start()
  }


  fun stretchDetected() {
    MainActivity.bleHandleService.createTimerNotification()
    isStretch.value = true
  }

  fun stretchStop() {
    isStretch.value = false
  }

  fun stressDetected() {
    MainActivity.bleHandleService.createTimerNotification()
    isStress.value = true
  }

  fun stressStop() {
    isStress.value = false
  }

//  var prevValue = Constants.STABLE_MOVING;


  private var topperCount = 0

  private fun receive(datas: ArrayDeque<ByteArray>) {
    val spn = SpannableStringBuilder()
    for (data in datas) {
      if (hexEnabled) {
        spn.append(TextUtil.toHexString(data)).append('\n')
      } else {
        val dataStr = String(data)
        StressDetection.validateDataFormat(dataStr)
        val text = TextUtil.toCaretString(dataStr, true)
        spn.append(text)
      }
    }

    if (topperCount < 5) {
      topperCount += 1
    } else {
      topperCount = 0
    }

    log.value = "$spn $topperCount"
  }
}