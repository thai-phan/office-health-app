package com.sewon.officehealth

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// TODO: connect server

// TODO: notification

// TODO: background service

@HiltAndroidApp
class Application : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}