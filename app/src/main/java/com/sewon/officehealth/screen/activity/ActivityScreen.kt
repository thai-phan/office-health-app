package com.sewon.officehealth.screen.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.R
import com.sewon.officehealth.common.AppDestinations
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

@SuppressLint("RememberReturnType")
@Composable
fun ScreenActivity(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  viewModel: ViewModelSleepActivity = hiltViewModel()
) {

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val milliseconds by MainActivity.dataListener.timeRemaining

  val second = milliseconds / 1000
  val minuteStr = ((second % 3600) / 60).toString();
  val secondStr = (second % 60).toString();


  fun disconnect() {
    MainActivity.serialService.disconnect()
  }

  fun stopSoundAlarm() {
    MainActivity.serialService.stopSoundStretch()
  }

  fun stopSoundStress() {
    MainActivity.serialService.stopSoundStress()
  }


  val calendar = Calendar.getInstance()
  val year = calendar.get(YEAR).toString()
  val month = (calendar.get(MONTH) + 1).toString()
  val date = calendar.get(DAY_OF_MONTH).toString()

  val logUI by MainActivity.dataListener.log
  val textUI = MainActivity.dataListener.receiveText.toString()

  val isStretchUI by MainActivity.dataListener.isStretch
  val isStressUI by MainActivity.dataListener.isStress

  val isPlayStretch by MainActivity.serialService.isPlaySoundStretch
  val isPlayStress by MainActivity.serialService.isPlaySoundStress

  Column(
    modifier = modifier
      .background(Color(0xFFCCF8D4))
      .fillMaxSize()
      .padding(vertical = 50.dp),

    ) {

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "앉아 계신지 ",
        style = TextStyle(
          fontSize = 16.sp,
          lineHeight = 25.6.sp,
          fontWeight = FontWeight(500),
          color = Color(0xFF000000),
          textAlign = TextAlign.Center,
        )
      )
      Text(
        minuteStr + "분 ",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color(0xFF00AD53)
      )
      Text(secondStr, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF00AD53))
      Text(
        text = " 되었습니다.",
        style = TextStyle(
          fontSize = 16.sp,
          lineHeight = 25.6.sp,
          fontWeight = FontWeight(500),
          color = Color(0xFF000000),
          textAlign = TextAlign.Center,
        )
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = " 스트레스는 낮은 상태입니다.",
        style = TextStyle(
          fontSize = 16.sp,
          lineHeight = 25.6.sp,
          fontWeight = FontWeight(500),
          color = Color(0xFF000000),
          textAlign = TextAlign.Center,
        )
      )
    }
    Image(
      painter = painterResource(id = R.mipmap.ic_image_3_foreground),
      contentDescription = "Logo",
      modifier = Modifier
        .size(width = 400.dp, height = 150.dp)
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(color = Color(0xFF60AC70))
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {

      Icon(
        Icons.Filled.CalendarMonth,
        contentDescription = "Localized description",
        tint = Color.Black
      )
      //목요일
      Text(year + "년 " + month + "월 " + date + "일 ", color = Color.Black)
      Text("")
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(color = Color(0xCC60AC70))
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_graphic_eq),
        contentDescription = "Logo",
      )
      Text("스트레스 해소", color = Color.Black)

      if (isPlayStretch) {
        Button(onClick = {
          MainActivity.serialService.stopSoundStretch()
        }) {
          Icon(
            Icons.Filled.PauseCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }
      } else {
        Button(onClick = {
          MainActivity.serialService.playSoundStretch()
        }) {
          Icon(
            Icons.Filled.PlayCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }

      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(color = Color(0x9960AC70))
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_ar_on_you),
        contentDescription = "Logo",
      )
      Text("집중력 향상", color = Color.Black)
      if (isPlayStress) {
        Button(onClick = {
          MainActivity.serialService.stopSoundStress()
        }) {
          Icon(
            Icons.Filled.PauseCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }

      } else {
        Button(onClick = {
          MainActivity.serialService.playSoundStress()
        }) {
          Icon(
            Icons.Filled.PlayCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(onClick = { navController.navigate(AppDestinations.DEVICE_ROUTE) }) {
        Text("Back to device")
      }
      Spacer(modifier = Modifier.width(20.dp))
      Button(onClick = { disconnect() }) {
        Text("Disconnect")
      }
    }

    when {
      isStretchUI -> {
        AlertDialogExample(
          onDismissRequest = {
            MainActivity.dataListener.stretchStop()
          },
          onConfirmation = {
            if (isPlayStretch) {
              MainActivity.serialService.stopSoundStretch()
            } else {
              MainActivity.serialService.playSoundStretch()
            }
          },
          dialogTitle = "스트레스 솔루션",
          dialogText = "스트레칭이 필요한 시간입니다.\n" +
              "솔루션을 재생하시겠습니까?",
          icon = Icons.Default.AddAlert,
          isPlay = isPlayStretch
        )
      }
    }

    when {
      isStressUI -> {
        AlertDialogExample(
          onDismissRequest = {
            MainActivity.dataListener.stressStop()
          },
          onConfirmation = {
            if (isPlayStress) {
              MainActivity.serialService.stopSoundStress()
            } else {
              MainActivity.serialService.playSoundStress()
            }
          },
          dialogTitle = "스트레스 솔루션",
          dialogText = "스트레스가 감지되었습니다.\n" +
              "솔루션을 재생하시겠습니까?",
          icon = Icons.Default.AddAlert,
          isPlay = isPlayStress
        )
      }
    }

//    // Test
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//      Text(logUI, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
//    }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//      Text(textUI, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
//    }
//
//    Row {
//      Button(onClick = {
//        MainActivity.dataListener.stretchDetected()
//      }) {
//        Text("Stretch")
//      }
//      Button(onClick = {
//        MainActivity.dataListener.stressDetected()
//      }) {
//        Text("Stress")
//      }
//    }
  }
}

@Preview
@Composable
fun PreviewSleepActivity() {
  ScreenActivity()
}