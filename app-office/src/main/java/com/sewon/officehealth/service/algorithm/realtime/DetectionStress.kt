package com.sewon.officehealth.service.algorithm.realtime

import android.icu.text.DecimalFormat
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.service.algorithm.AlgorithmConstants
import timber.log.Timber

class DetectionStress {

  private val TAG = this.javaClass.name

  //  var oneMinuteRefCount = 20
  private var meanDuration = AlgorithmConstants.MEAN_DURATION

  //  var referenceCount = 100
  private var referenceCount = AlgorithmConstants.STRESS_REF_DURATION

  var sumOneMinHR = 0.0
  var sumOneMinBR = 0.0
  var oneMinHRCount = 0
  var oneMinBRCount = 0

  fun processTopperDataStress(messageList: List<String>) {
    val topperData = TopperData(messageList)
    if (topperData.stable == 0 || topperData.stable == 1 || topperData.stable == 2) {
      return
    }

    calculateReference(topperData)

    if (isHRRefCalculated) {
      sumOneMinHR += topperData.HR
      oneMinHRCount += 1
//        Timber.tag(TAG).d("HR count $oneMinHRCount")
      if (oneMinHRCount == meanDuration) {
        val meanOneMinHR = sumOneMinHR / meanDuration
        statusDetectHR(meanOneMinHR)
        oneMinHRCount = 0
        sumOneMinHR = 0.0
      }
    }
    if (isBRRefCalculated) {
      sumOneMinBR += topperData.BR
      oneMinBRCount += 1
//        Timber.tag(TAG).d("BR count $oneMinBRCount")
      if (oneMinBRCount == meanDuration) {
        val meanOneMinBR = sumOneMinBR / meanDuration
        statusDetectBR(meanOneMinBR)
        oneMinBRCount = 0
        sumOneMinBR = 0.0
      }
    }
  }

  var sumRefHRV = 0.0
  var sumRefHR = 0.0
  var sumRefBR = 0.0

  var refHRV = 0.0
  var refHR = mutableDoubleStateOf(0.0)
  var refBR = mutableDoubleStateOf(0.0)
  var decimalFormat: DecimalFormat = DecimalFormat("#.##")


  var countReferenceHR = mutableIntStateOf(0)
  var countReferenceBR = mutableIntStateOf(0)

  var isHRRefCalculated = false
  var isBRRefCalculated = false

  fun calculateReference(topperData: TopperData) {
    if (!isHRRefCalculated) {
      sumRefHR += topperData.HR
      countReferenceHR.intValue += 1
      if (countReferenceHR.intValue == referenceCount) {
        refHR.doubleValue = decimalFormat.format(sumRefHR / referenceCount).toDouble()
        isHRRefCalculated = true
      }
    }

    if (!isBRRefCalculated) {
      sumRefBR += topperData.BR
      countReferenceBR.intValue += 1
      if (countReferenceBR.intValue == referenceCount) {
        refBR.doubleValue = decimalFormat.format(sumRefBR / referenceCount).toDouble()
        isBRRefCalculated = true
      }
    }
  }


  private fun statusDetectHR(meanOneMinHR: Double) {
    if (meanOneMinHR > refHR.doubleValue * AlgorithmConstants.NORMAL_HR_THRESHOLD) {
      if (meanOneMinHR > refHR.doubleValue * AlgorithmConstants.ALERT_HR_THRESHOLD) {
        stress(meanOneMinHR, "HR", refHR.doubleValue)
      } else {
        alert(meanOneMinHR, "HR", refHR.doubleValue)
      }
    } else {
      normal()
    }
  }

  private fun statusDetectBR(meanOneMinBR: Double) {
    if (meanOneMinBR > refBR.doubleValue * AlgorithmConstants.NORMAL_BR_THRESHOLD) {
      if (meanOneMinBR > refBR.doubleValue * AlgorithmConstants.ALERT_BR_THRESHOLD) {
        stress(meanOneMinBR, "BR", refBR.doubleValue)
      } else {
        alert(meanOneMinBR, "BR", refBR.doubleValue)
      }
    } else {
      normal()
    }
  }

  val stressMessage = mutableStateOf("")

  private fun stress(meanOneMin: Double, message: String, ref: Double) {
    val stressMess = "Detect Stress $message $meanOneMin ref $ref \n"
    Timber.tag(TAG).d(stressMess)
    stressMessage.value += stressMess
    MainActivity.lowEnergyClient.stressDetected()
  }

  private var alertCount = 0

  private fun alert(meanOneMin: Double, message: String, ref: Double) {
    alertCount += 1
    if (alertCount == 2) {
      stress(meanOneMin, message, ref)
      alertCount = 0
    }
  }

  private fun normal() {

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