package com.sewon.officehealth.service.algorithm.stress

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.AlgorithmConstants
import com.sewon.officehealth.service.ble.Constants

class StressDetection {

  companion object {
    //    1 * 60 * 20
    var oneMinuteRefCount = 200
    var referenceTime = 5

    //    referenceTime * 60 * 20
    var referenceCount = 1000

    var sumRefHRV = 0.0
    var sumRefHR = 0.0
    var sumRefBR = 0.0

    var refHRV = 0.0


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

    var refHR = mutableDoubleStateOf(0.0)
    var refBR = mutableDoubleStateOf(0.0)

    var countReferenceHR = mutableIntStateOf(0)
    var countReferenceBR = mutableIntStateOf(0)

    var isHRRefCalculated = false
    var isBRRefCalculated = false

    fun calculateReference(topperData: TopperData) {
      if (!isHRRefCalculated) {
        if (topperData.HR > AlgorithmConstants.REF_HR_THRESHOLD) {
          sumRefHR += topperData.HR
          countReferenceHR.intValue += 1
        }
        if (countReferenceHR.intValue == referenceCount) {
          refHR.doubleValue = sumRefHR / referenceCount
          // refHR calculated
          isHRRefCalculated = true
        }
      }

      if (!isBRRefCalculated) {
        if (topperData.BR > AlgorithmConstants.REF_BR_THRESHOLD) {
          sumRefBR += topperData.BR
          countReferenceBR.intValue += 1
        }
        if (countReferenceBR.intValue == referenceCount) {
          refBR.doubleValue = sumRefBR / referenceCount
          // refBR calculated
          isBRRefCalculated = true
        }
      }
    }

    var sumOneMinHR = 0.0
    var sumOneMinBR = 0.0
    var meanOneMinHR = 0.0
    var meanOneMinBR = 0.0

    var oneMinHRCount = 0
    var oneMinBRCount = 0


    private fun processTopperDataStress(messageList: List<String>) {
      val topperData = TopperData(messageList)
      calculateReference(topperData)

      if (isHRRefCalculated) {
        sumOneMinHR += topperData.HR
        oneMinHRCount += 1
        if (oneMinHRCount == oneMinuteRefCount) {
          meanOneMinHR = sumRefHR / oneMinuteRefCount
          statusDetect(topperData)
          oneMinHRCount = 0
        }
      }

      if (isBRRefCalculated) {
        sumOneMinBR += topperData.BR
        oneMinBRCount += 1
        if (oneMinBRCount == oneMinuteRefCount) {
          meanOneMinBR = sumRefBR / oneMinuteRefCount
          statusDetect(topperData)
          oneMinBRCount = 0
        }
      }
    }

    fun statusDetect(topperData: TopperData) {
      if (topperData.HR > refHR.doubleValue * AlgorithmConstants.NORMAL_HR_THRESHOLD) {
        if (topperData.HR > refHR.doubleValue * AlgorithmConstants.ALERT_HR_THRESHOLD) {
          stress()
        } else {
          alert()
        }
      } else {
        normal()
      }

      if (topperData.BR > refBR.doubleValue * AlgorithmConstants.NORMAL_BR_THRESHOLD) {
        if (topperData.BR > refBR.doubleValue * AlgorithmConstants.ALERT_BR_THRESHOLD) {
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