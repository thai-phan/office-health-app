package com.sewon.officehealth.service.ble

import com.sewon.officehealth.BuildConfig


internal object Constants {
  // values have to be globally unique
  const val INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect"
  const val NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel"
  const val INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity"

  // values have to be unique within each app
  const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001


  const val STABLE_NO_TARGET = "0"
  const val STABLE_NO_VITAL_SIGN = "1"
  const val STABLE_MOVING = "2"
  const val STABLE_BR_LOWER_50 = "3"
  const val STABLE_BR_UPPER_50 = "4"

  const val NO_VITAL_SIGN_THRESHOLD = 1 * 60 * 20
  const val NO_TARGET_THRESHOLD = 1 * 60 * 20
}