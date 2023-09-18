package com.sewon.officehealth.service.ble

import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.algorithm.ECG_ANALYSIS_PROC
import timber.log.Timber
import java.util.ArrayDeque

class DataListener : SerialListener {

  private enum class Connected {
    False, Pending, True
  }

  private val deviceAddress: String? = null
  private val service: SerialService? = null
  private val hexWatcher: TextUtil.HexWatcher? = null
  private val initialStart = true


  private var connected = Connected.False
  private val hexEnabled = false
  private var pendingNewline = false
  private val newline: String = TextUtil.newline_crlf
  val receiveText: SpannableStringBuilder = SpannableStringBuilder()


  val log = mutableStateOf("")
  val isStretch = mutableStateOf(false)
  val isStress = mutableStateOf(false)


  val dataArrayList = ArrayList<Double>()

//  private val totalDuration = 50  * 60 * 1000L
  private val totalDuration = 10 * 1000L
  private val b = 1000L
  val timeRemaining = mutableLongStateOf(totalDuration)


  val countDownTimer = object : CountDownTimer(totalDuration, b) {
    override fun onTick(millisUntilFinished: Long) {
      Timber.tag("MYLOG").d("text updated programmatically")
      timeRemaining.longValue = millisUntilFinished
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

  var prevValue = "0";

  fun stretchDetected()  {
    MainActivity.serialService.playSoundAlarm()
    isStretch.value = true
  }

  fun stretchStop()  {
    MainActivity.serialService.stopSoundAlarm()
    isStretch.value = false
  }

  fun stressDetected() {
    MainActivity.serialService.playSoundStress()
    isStress.value = true
  }

  fun stressStop() {
    MainActivity.serialService.stopSoundStress()
    isStress.value = false
  }


  private fun receive(datas: ArrayDeque<ByteArray>) {
    val spn = SpannableStringBuilder()
    for (data in datas) {
      if (hexEnabled) {
        spn.append(TextUtil.toHexString(data)).append('\n')
      } else {
        var msg = String(data)
        val dataArray = msg.split(" ").filter { it != "" }
        if (prevValue == "0" && dataArray.get(0) != "0") {
          countDownTimer.cancel()
          countDownTimer.start()
        }

        prevValue = dataArray.get(0)

        if (dataArrayList.size == 9) {
            val result = ECG_ANALYSIS_PROC.ECG_AnalysisData(dataArrayList)
            if (result.get(0).stress_State == "stress") {
              stressDetected()
            }
            dataArrayList.clear()
        } else  {
          dataArrayList.add(dataArray.get(4).toDouble())
        }

        if (newline == TextUtil.newline_crlf && msg.length > 0) {
          // don't show CR as ^M if directly before LF
          msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf)
          // special handling if CR and LF come in separate fragments
//          if (pendingNewline && msg[0] == '\n') {
//            if (spn.length >= 2) {
//              spn.delete(spn.length - 2, spn.length)
//            } else {
//              val edt: Editable = receiveText.getEditableText()
//              if (edt != null && edt.length >= 2) edt.delete(edt.length - 2, edt.length)
//            }
//          }
          pendingNewline = msg[msg.length - 1] == '\r'
        }
        spn.append(TextUtil.toCaretString(msg, newline.length != 0))
      }
    }
    log.value = spn.toString()
    receiveText.append(spn)
  }



}