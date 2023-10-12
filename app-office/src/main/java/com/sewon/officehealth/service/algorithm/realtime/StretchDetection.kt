package com.sewon.officehealth.service.algorithm.realtime

import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.ble.Constants

class StretchDetection {
  companion object {

    private val dataArrayList = ArrayList<Double>()

    var countNoVitalSign = 0
    var countNoTarget = 0

    fun processTopperDataStretch(messageList: List<String>) {
      //  STABLE_NO_VITAL_SIGN = "1"
      if (messageList[0] == Constants.STABLE_NO_VITAL_SIGN) {
        countNoVitalSign += 1
        if (countNoVitalSign == Constants.NO_VITAL_SIGN_THRESHOLD) {
          MainActivity.bleDataListener.resetTimer()
          countNoVitalSign = 0
        }
      } else {
        countNoVitalSign = 0
      }

      //  STABLE_NO_TARGET = "0"
      if (messageList[0] == Constants.STABLE_NO_TARGET) {
        countNoTarget += 1
        if (countNoTarget == Constants.NO_TARGET_THRESHOLD) {
          MainActivity.bleDataListener.resetTimer()
          countNoTarget = 0
        }
      } else {
        countNoTarget = 0
      }
    }
  }
}