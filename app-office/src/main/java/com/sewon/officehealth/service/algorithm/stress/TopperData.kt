package com.sewon.officehealth.service.algorithm.stress

import java.time.LocalTime

class TopperData(stringList: List<String>) {
  var stable: Int
  var HR: Int
  var BR: Int
  var HRV: Double
  var HRWfm: Double
  var BRWfm: Double
  var isSleep: Boolean
  var sessionId: Int = 0
  var createdAt: LocalTime

  init {
    stable = stringList[0].toInt()
    HR = stringList[1].toInt()
    BR = stringList[2].toInt()
    HRV = stringList[3].toDouble()
    HRWfm = try {
      stringList[4].toDouble()
    } catch (error: NumberFormatException) {
      0.0
    }
    BRWfm = try {
      stringList[5].toDouble()
    } catch (error: NumberFormatException) {
      0.0
    }
    isSleep = true
//    sessionId = currentSessionId
    createdAt = LocalTime.now()
  }

//  fun toTopperModel(): Topper {
//    return Topper(
//      stable = stable,
//      hr = HR,
//      br = BR,
//      hrv = HRV,
//      hrWfm = HRWfm,
//      brWfm = BRWfm,
//      isSleep = isSleep,
//      sessionId = sessionId,
//      createdAt = createdAt
//    )
//  }
}


