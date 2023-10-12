package com.sewon.officehealth.service.algorithm.realtime

import android.os.CountDownTimer
import androidx.compose.runtime.mutableLongStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.AlgorithmConstants
import com.sewon.officehealth.service.ble.Constants
import timber.log.Timber

class DetectionStretch {

  private val TAG = "DetectionStretch"

  private var countNoVitalSign = 0
  private var countNoTarget = 0

  val totalDuration = AlgorithmConstants.STRETCH_COUNTDOWN_DURATION

  val timeRemaining = mutableLongStateOf(totalDuration)

  val countDownTimer = object : CountDownTimer(totalDuration, 1000L) {
    override fun onTick(millisUntilFinished: Long) {
      timeRemaining.longValue = totalDuration - millisUntilFinished
    }

    override fun onFinish() {
      MainActivity.listenerBleData.stretchDetected()
      timeRemaining.longValue = 0
    }
  }

  fun processTopperDataStretch(messageList: List<String>) {
    //  STABLE_NO_VITAL_SIGN = "1"
    if (messageList[0] == Constants.STABLE_NO_VITAL_SIGN) {
      countNoVitalSign += 1
      if (countNoVitalSign == Constants.NO_VITAL_SIGN_THRESHOLD) {
        Timber.tag(TAG).d("STABLE_NO_VITAL_SIGN")
        MainActivity.listenerBleData.resetTimer()
        countNoVitalSign = 0
      }
    } else {
      countNoVitalSign = 0
    }

    //  STABLE_NO_TARGET = "0"
    if (messageList[0] == Constants.STABLE_NO_TARGET) {
      countNoTarget += 1
      if (countNoTarget == Constants.NO_TARGET_THRESHOLD) {
        Timber.tag(TAG).d("STABLE_NO_TARGET")
        MainActivity.listenerBleData.resetTimer()
        countNoTarget = 0
      }
    } else {
      countNoTarget = 0
    }
  }
}