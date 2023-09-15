package com.sewon.officehealth

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class TimerActivity : ComponentActivity() {
  private enum class Connected {
    False, Pending, True
  }


  private val a = 10000L
  private val b = 1000L
  private val c = mutableLongStateOf(a)

  override fun onCreate(savedInstanceState: Bundle?) {
    actionBar?.hide();
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

//    startService(Intent(applicationContext, SerialService::class.java))

    val appContext = applicationContext


    val timer = object : CountDownTimer(a, b) {
      override fun onTick(millisUntilFinished: Long) {
        Timber.tag("MYLOG").d("text updated programmatically")
        c.longValue = millisUntilFinished
      }

      override fun onFinish() {
        c.longValue = 0
      }
    }
    timer.start()
    setContent {
      CountDown()
    }
  }

  @Composable
  fun CountDown() {
    val milliseconds by c
    val text = (milliseconds / 1000).toString()
    Column {
      Text("asdasdasd")
      Text(text)
      Text(text)
      Text(text)
      Text("asdfa1231")
    }

  }
}