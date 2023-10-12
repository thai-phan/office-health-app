package com.sewon.officehealth.service.algorithm

object AlgorithmConstants {
  const val REPORT_STAGE_WAKE = 4
  const val REPORT_STAGE_REM = 3
  const val REPORT_STAGE_N1 = 2
  const val REPORT_STAGE_N2 = 1
  const val REPORT_STAGE_N3 = 0

  const val REF_HR_THRESHOLD = 40
  const val REF_BR_THRESHOLD = 10

  const val NORMAL_HR_THRESHOLD = 1.1
  const val ALERT_HR_THRESHOLD = 1.2

  const val NORMAL_BR_THRESHOLD = 1.2
  const val ALERT_BR_THRESHOLD = 1.4
}