package com.sewon.officehealth.screen.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.sewon.officehealth.screen.AppDestinations
import com.sewon.officehealth.screen.activity.component.DialogStress
import com.sewon.officehealth.screen.activity.component.DeviceCheckDialog
import timber.log.Timber
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("RememberReturnType")
@Composable
fun ScreenActivity(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  viewModel: ViewModelSleepActivity = hiltViewModel()
) {

  SideEffect {
    Timber.tag("ScreenActivity").d("recompose")
  }

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val milliseconds by MainActivity.bleServiceListener.realtimeDataObject.stretchObj.timeRemaining

  val second = milliseconds / 1000
  val minuteStr = ((second % 3600) / 60).toString()
  val secondStr = (second % 60).toString()

  fun disconnect() {
    MainActivity.bleServiceHandler.disconnect()
    navController.navigate(AppDestinations.DEVICE_ROUTE)
  }

  val calendar = Calendar.getInstance()
  val year = calendar.get(YEAR).toString()
  val month = (calendar.get(MONTH) + 1).toString()
  val date = calendar.get(DAY_OF_MONTH).toString()


//  val isStretchUI by remember { MainActivity.listenerBleData.isStretch }
  val isStressUI by remember { MainActivity.bleServiceListener.isStress }

  val isPlayStretch by remember { MainActivity.bleServiceHandler.isPlaySoundStretch }
  val isPlayStress by remember { MainActivity.bleServiceHandler.isPlaySoundStress }

  val normalTextStyle = TextStyle(
    fontSize = 20.sp,
    lineHeight = 25.6.sp,
    fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
    fontWeight = FontWeight(400),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
  )

  Column(
    modifier = modifier
      .statusBarsPadding()
      .systemBarsPadding()
      .fillMaxSize()
  ) {

//    Button(onClick = {
//      MainActivity.listenerBleData.send("aaaaa")
//    }) {
//      Text("Test")
//    }
    Column(
      modifier = Modifier
        .weight(4f),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(20.dp))
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "앉아 계신지 ",
          style = normalTextStyle
        )
        Text(
          minuteStr + "분 ",
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontSize = 20.sp,
          color = Color(0xFF4DB39D)
        )
        Text(
          secondStr + "초",
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFF4DB39D)
        )
        Text(
          text = " 되었습니다.",
          style = normalTextStyle
        )
      }

      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = " 스트레스는",
          style = normalTextStyle
        )
        Text(
          text = " 낮은 ",
          style = TextStyle(
            fontSize = 20.sp,
            lineHeight = 25.6.sp,
            fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
            fontWeight = FontWeight.Black,
            color = Color(0xFF4DB39D),
            textAlign = TextAlign.Center,
          )
        )
        Text(
          text = "상태입니다.",
          style = normalTextStyle
        )
      }
      Image(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        painter = painterResource(id = R.mipmap.ic_image_3_foreground),
        contentDescription = "Logo",

        )
    }
    val actionTextStyle = TextStyle(
      fontSize = 24.sp,
      lineHeight = 32.sp,
      fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
      fontWeight = FontWeight(700),
      color = Color(0xFF003917),
      textAlign = TextAlign.Center,
    )
    Column(modifier = Modifier.weight(4f)) {
      Row(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .background(color = Color(0xFF7AC6B5))
          .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          Icons.Filled.CalendarMonth,
          contentDescription = "Localized description",
          modifier = Modifier
            .weight(2f),
          tint = Color.Black
        )
        Text(
          year + "년 " + month + "월 " + date + "일 ",
          style = actionTextStyle,
          modifier = Modifier
            .weight(6f)
        )
        Spacer(modifier = Modifier.weight(2f))
      }
      Row(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .background(color = Color(0xFF4DB39D))
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          modifier = Modifier.weight(2f),
          painter = painterResource(id = R.drawable.ic_graphic_eq),
          contentDescription = "Logo",
        )
        Text(
          modifier = Modifier
            .weight(6f),
          text = "스트레스 해소",
          style = actionTextStyle
        )
        Button(
          modifier = Modifier.weight(2f),
          colors = ButtonDefaults.buttonColors(Color(0xFF378171)),
          onClick = {
            if (isPlayStretch) {
              MainActivity.bleServiceHandler.stopSoundStretch()
            } else {
              MainActivity.bleServiceHandler.playSoundStretch()
            }
          }) {
          if (isPlayStretch) {
            Icon(
              Icons.Filled.PauseCircleOutline,
              contentDescription = "Localized description",
              tint = Color.Black
            )
          } else {
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
          .weight(1f)
          .fillMaxWidth()
          .background(color = Color(0xFF45A18D))
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          modifier = Modifier.weight(2f),
          painter = painterResource(id = R.drawable.ic_ar_on_you),
          contentDescription = "Logo",
        )

        Text(
          text = "집중력 향상",
          modifier = Modifier.weight(6f),
          style = actionTextStyle
        )

        Button(
          modifier = Modifier.weight(2f),
          colors = ButtonDefaults.buttonColors(Color(0xFF378171)),
          onClick = {
            if (isPlayStress) {
              MainActivity.bleServiceHandler.stopSoundStress()
            } else {
              MainActivity.bleServiceHandler.playSoundStress()
            }
          }
        ) {
          if (isPlayStress) {
            Icon(
              Icons.Filled.PauseCircleOutline,
              contentDescription = "Localized description",
              tint = Color.Black
            )
          } else {
            Icon(
              Icons.Filled.PlayCircleOutline,
              contentDescription = "Localized description",
              tint = Color.Black
            )
          }
        }
      }
    }

    Column(
      modifier = Modifier
        .weight(2f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Button(
        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
        modifier = Modifier
          .width(320.dp)
          .height(72.dp),
        onClick = { disconnect() }
      ) {
        Text(
          text = "연결됨",
          style = TextStyle(
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.jalnan)),
            fontWeight = FontWeight(700),
            color = Color(0xFF4DB39D),
          )
        )
      }
//      Log()
    }


    when {
      isStressUI -> {
        DialogStress(
          onDismissRequest = {
            MainActivity.bleServiceListener.stressStop()
          },
          onConfirmation = {
            if (isPlayStress) {
              MainActivity.bleServiceHandler.stopSoundStress()
            } else {
              MainActivity.bleServiceHandler.playSoundStress()
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


    DeviceCheckDialog()
  }
}

@Preview
@Composable
fun PreviewSleepActivity() {
  ScreenActivity()
}