package com.sewon.officehealth.service.ble

import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.ecg.ECGAnalysisProc
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

  private val dataArrayList = ArrayList<Double>()

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

  var countNoVitalSign = 0
  var countNoTarget = 0

  private fun processData(messageList: List<String>) {

    //  STABLE_NO_VITAL_SIGN = "1"
    if (messageList[0] == Constants.STABLE_NO_VITAL_SIGN) {
      countNoVitalSign += 1
      if (countNoVitalSign == Constants.NO_VITAL_SIGN_THRESHOLD) {
        resetTimer()
        countNoVitalSign = 0
      }
    } else {
      countNoVitalSign = 0
    }

    //  STABLE_NO_TARGET = "0"
    if (messageList[0] == Constants.STABLE_NO_TARGET) {
      countNoTarget += 1
      if (countNoTarget == Constants.NO_TARGET_THRESHOLD) {
        resetTimer()
        countNoTarget = 0
      }
    } else {
      countNoTarget = 0
    }

    if (dataArrayList.size == 9) {
      val result = ECGAnalysisProc.ECG_AnalysisData(dataArrayList)
      if (result[0].stress_State == "stress") {
        stressDetected()
      }
      dataArrayList.clear()
    }
    dataArrayList.add(messageList[3].toDouble())
  }

  private var tempCount = 0

  private val regex = Regex("[01234]")

  private fun validateDataFormatAndProcess(dataStr: String) {
    val messageList = dataStr.split(" ").filter { it != "" }
    if (messageList.size == 6 && messageList[0].matches(regex)) {
      processData(messageList)
    } else {
      isWrongDeviceType.value = true
      MainActivity.bleHandleService.disconnect()
    }
  }

  private fun receive(datas: ArrayDeque<ByteArray>) {
    val spn = SpannableStringBuilder()
    for (data in datas) {
      if (hexEnabled) {
        spn.append(TextUtil.toHexString(data)).append('\n')
      } else {
        val dataStr = String(data)
        validateDataFormatAndProcess(dataStr)
        val text = TextUtil.toCaretString(dataStr, true)
        spn.append(text)
      }
    }

    if (tempCount < 2) {
      tempCount += 1
    } else {
      tempCount = 0
    }

    log.value = "$spn $tempCount"
  }
}