package com.sewon.officehealth.service.algorithm.realtime

import com.sewon.officehealth.MainActivity
import timber.log.Timber

class RealtimeDataObject {

  var detectionStretch: DetectionStretch = DetectionStretch()
  var detectionStress: DetectionStress = DetectionStress()

//  init {
//    stretchDetection =
//    stressDetection =
//  }


  private val regex = Regex("[01234]")


  fun validateDataFormat(dataStr: String) {
    val messageList = dataStr.split(" ").filter { it != "" }
    if (messageList.size == 6 && messageList[0].matches(regex)) {
      detectionStretch.processTopperDataStretch(messageList)
      detectionStress.processTopperDataStress(messageList)
    } else {
      Timber.tag("DataProcess").d("Wrong Device Type")
      MainActivity.listenerBleData.isWrongDeviceType.value = true
      MainActivity.serviceBleHandle.disconnect()
    }
  }


}