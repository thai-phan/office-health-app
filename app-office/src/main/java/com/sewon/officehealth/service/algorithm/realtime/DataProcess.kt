package com.sewon.officehealth.service.algorithm.realtime

import com.sewon.officehealth.MainActivity

class DataProcess {

  companion object {
    private val regex = Regex("[01234]")

    fun validateDataFormat(dataStr: String) {
      val messageList = dataStr.split(" ").filter { it != "" }
      if (messageList.size == 6 && messageList[0].matches(regex)) {
        StretchDetection.processTopperDataStretch(messageList)
        StressDetection.processTopperDataStress(messageList)
      } else {
        MainActivity.bleDataListener.isWrongDeviceType.value = true
        MainActivity.bleHandleService.disconnect()
      }
    }
  }
}