package com.sewon.officehealth.service.algorithm.stress

import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.AlgorithmConstants
import com.sewon.officehealth.service.algorithm.ecg.ECGAnalysisProc
import com.sewon.officehealth.service.ble.Constants

class StressDetection {

  companion object {
    var oneMinuteRefCount = 1 * 60 * 20
    var referenceTime = 5
    var referenceCount = referenceTime * 60 * 20

    var sumRefHRV = 0.0
    var sumRefHR = 0.0
    var sumRefBR = 0.0

    var refHRV = 0.0
    var refHR = 0.0
    var refBR = 0.0

    var countNoVitalSign = 0
    var countNoTarget = 0


    private val regex = Regex("[01234]")

    fun validateDataFormat(dataStr: String) {
      val messageList = dataStr.split(" ").filter { it != "" }
      if (messageList.size == 6 && messageList[0].matches(regex)) {
        processTopperDataStretch(messageList)
        processTopperDataStress(messageList)
      } else {
        MainActivity.bleDataListener.isWrongDeviceType.value = true
        MainActivity.bleHandleService.disconnect()
      }
    }

    var countReferenceHR = 0
    var countReferenceBR = 0


    val ignoreArray = intArrayOf(1, 2, 3)

    var isHRRefCalculated = false
    var isBRRefCalculated = false

    fun calculateReference(topperData: TopperData) {
      if (!isHRRefCalculated) {
        if (!ignoreArray.contains(topperData.HR)) {
          sumRefHR += topperData.HR
          countReferenceHR += 1
        }
        if (countReferenceHR == referenceCount) {
          refHR = sumRefHR / referenceCount
          // refHR calculated
          isHRRefCalculated = true
        }
      }

      if (!isBRRefCalculated) {
        if (!ignoreArray.contains(topperData.BR)) {
          sumRefBR += topperData.BR
          countReferenceBR += 1
        }
        if (countReferenceBR == referenceCount) {
          refBR = sumRefBR / referenceCount
          // refBR calculated
          isBRRefCalculated = true
        }
      }
    }

    var sumOneMinHR = 0.0
    var sumOneMinBR = 0.0
    var meanOneMinHR = 0.0
    var meanOneMinBR = 0.0
    var oneMinCount = 0

    private fun processTopperDataStress(messageList: List<String>) {
      val topperData = TopperData(messageList)
      calculateReference(topperData)

      // if refHR and refBR not calculated
      if (!isHRRefCalculated || !isBRRefCalculated) {
        return
      }

      sumOneMinHR += topperData.HR
      sumOneMinBR += topperData.BR
      oneMinCount += 1

      if (oneMinCount == oneMinuteRefCount) {
        meanOneMinHR = sumRefBR / oneMinuteRefCount
        meanOneMinBR = sumRefHR / oneMinuteRefCount
        oneMinCount = 0

        statusDetect(topperData)
      }
    }

    fun statusDetect(topperData: TopperData) {
      if (topperData.HR > refHR * AlgorithmConstants.NORMAL_HR_THRESHOLD) {
        if (topperData.HR > refHR * AlgorithmConstants.ALERT_HR_THRESHOLD) {
          stress()
        } else {
          alert()
        }
      } else {
        normal()
      }

      if (topperData.BR > refBR * AlgorithmConstants.NORMAL_BR_THRESHOLD) {
        if (topperData.BR > refBR * AlgorithmConstants.ALERT_BR_THRESHOLD) {
          stress()
        } else {
          alert()
        }
      } else {
        normal()
      }
    }

    private fun stress() {
      MainActivity.bleDataListener.stressDetected()
    }

    var alertCount = 0

    private fun alert() {
      alertCount += 1
      if (alertCount == 2) {
        stress()
        alertCount = 0
      }
    }

    private fun normal() {

    }

    private val dataArrayList = ArrayList<Double>()

    private fun processTopperDataStretch(messageList: List<String>) {

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

//      if (dataArrayList.size == 9) {
//        val result = ECGAnalysisProc.ECG_AnalysisData(dataArrayList)
//        if (result[0].stress_State == "stress") {
//          MainActivity.bleDataListener.stressDetected()
//        }
//        dataArrayList.clear()
//      }
//      dataArrayList.add(messageList[3].toDouble())
    }
  }
}