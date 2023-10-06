package com.sewon.officehealth.screen.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.window.Dialog
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

  val milliseconds by MainActivity.bleDataListener.timeRemaining

  val second = milliseconds / 1000
  val minuteStr = ((second % 3600) / 60).toString();
  val secondStr = (second % 60).toString();


  fun disconnect() {
    MainActivity.bleHandleService.disconnect()
    navController.navigate(AppDestinations.DEVICE_ROUTE)
  }

  val calendar = Calendar.getInstance()
  val year = calendar.get(YEAR).toString()
  val month = (calendar.get(MONTH) + 1).toString()
  val date = calendar.get(DAY_OF_MONTH).toString()

  val logUI by MainActivity.bleDataListener.log
  val textUI = MainActivity.bleDataListener.receiveText.toString()

  val isStretchUI by MainActivity.bleDataListener.isStretch
  val isStressUI by MainActivity.bleDataListener.isStress
  val isWrongDeviceUi by MainActivity.bleDataListener.isWrongDeviceType

  val isPlayStretch by MainActivity.bleHandleService.isPlaySoundStretch
  val isPlayStress by MainActivity.bleHandleService.isPlaySoundStress

  val rowHeight = 80.dp

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
      .background(Color(0xFFCCF8D4))
      .fillMaxSize()
      .padding(vertical = 70.dp)

  ) {

    Row(
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
        color = Color(0xFF00AD53)
      )
      Text(
        secondStr + "초",
        fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF00AD53)
      )
      Text(
        text = " 되었습니다.",
        style = normalTextStyle
      )
    }

    Row(
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
          color = Color(0xFF00AD53),
          textAlign = TextAlign.Center,
        )
      )
      Text(
        text = "상태입니다.",
        style = normalTextStyle
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
        .height(rowHeight)
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
      Text(
        year + "년 " + month + "월 " + date + "일 ",
        style = TextStyle(
          fontSize = 20.sp,
          lineHeight = 26.sp,
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontWeight = FontWeight(500),
          color = Color(0xFF00210A),
          textAlign = TextAlign.Center,
        )
      )
      Text("")
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(rowHeight)
        .background(color = Color(0xCC60AC70))
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_graphic_eq),
        contentDescription = "Logo",
      )
      Text(
        text = "스트레스 해소",
        style = TextStyle(
          fontSize = 24.sp,
          lineHeight = 31.2.sp,
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontWeight = FontWeight(700),
          color = Color(0xFF003917),
          textAlign = TextAlign.Center,
        )
      )


      if (isPlayStretch) {
        Button(
          colors = ButtonDefaults.buttonColors(Color(0xCC60AC70)),
          onClick = {
            MainActivity.bleHandleService.stopSoundStretch()
          }) {
          Icon(
            Icons.Filled.PauseCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }
      } else {
        Button(
          colors = ButtonDefaults.buttonColors(Color(0xCC60AC70)),
          onClick = {
            MainActivity.bleHandleService.playSoundStretch()
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
        .height(rowHeight)
        .background(color = Color(0x9960AC70))
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_ar_on_you),
        contentDescription = "Logo",
      )

      Text(
        text = "집중력 향상",
        style = TextStyle(
          fontSize = 24.sp,
          lineHeight = 31.2.sp,
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontWeight = FontWeight(700),
          color = Color(0xFF003917),
          textAlign = TextAlign.Center,
        )
      )

      if (isPlayStress) {
        Button(
          colors = ButtonDefaults.buttonColors(Color(0x9960AC70)),
          onClick = {
            MainActivity.bleHandleService.stopSoundStress()
          }) {
          Icon(
            Icons.Filled.PauseCircleOutline,
            contentDescription = "Localized description",
            tint = Color.Black
          )
        }

      } else {
        Button(
          colors = ButtonDefaults.buttonColors(Color(0x9960AC70)),
          onClick = {
            MainActivity.bleHandleService.playSoundStress()
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
      Button(
        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
        modifier = Modifier
          .shadow(elevation = 4.dp, spotColor = Color(0x1A000000), ambientColor = Color(0x1A000000))
          .border(
            width = 4.dp,
            color = Color(0xFF4EA162),
            shape = RoundedCornerShape(size = 999.dp)
          )
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
            color = Color(0xFF4EA162),
          )
        )
      }
    }

    when {
      isStretchUI -> {
        AlertDialog(
          onDismissRequest = {
            MainActivity.bleDataListener.stretchStop()
          },
          onConfirmation = {
            if (isPlayStretch) {
              MainActivity.bleHandleService.stopSoundStretch()
            } else {
              MainActivity.bleHandleService.playSoundStretch()
            }
          },
          dialogTitle = "스트레칭 솔루션",
          dialogText = "스트레칭이 필요한 시간입니다.\n" +
              "솔루션을 재생하시겠습니까?",
          icon = Icons.Default.AddAlert,
          isPlay = isPlayStretch
        )
      }
    }

    when {
      isStressUI -> {
        AlertDialog(
          onDismissRequest = {
            MainActivity.bleDataListener.stressStop()
          },
          onConfirmation = {
            if (isPlayStress) {
              MainActivity.bleHandleService.stopSoundStress()
            } else {
              MainActivity.bleHandleService.playSoundStress()
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

//    Button(onClick = { MainActivity.serialService.createNotificationHealth() }) {
//        Text(text = "Test")
//    }

    var isShowLog by remember {
      mutableStateOf(false)
    }

    Row(
      modifier = Modifier
        .padding(10.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Button(
        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
        modifier = Modifier
          .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 999.dp)),
        onClick = { isShowLog = !isShowLog }
      ) {
        Text(
          text = "Log",
          style = TextStyle(
            fontFamily = FontFamily(Font(R.font.jalnan)),
            color = Color(0xFF4EA162),
          )
        )
      }
      if (isShowLog) {
        Text(logUI, fontWeight = FontWeight.Bold, color = Color.Black)
      }

    }
    fun onDismissRequest() {
      MainActivity.bleDataListener.isWrongDeviceType.value = false
    }
    when {
      isWrongDeviceUi -> {
        Dialog(onDismissRequest = { onDismissRequest() }) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .padding(16.dp),
            colors = CardDefaults.cardColors(Color(0xFF4EA162)),
            shape = RoundedCornerShape(16.dp),
          ) {
            Text(
              text = "Wrong device!",
              color = Color(0xFF003917),
              modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewSleepActivity() {
  ScreenActivity()
}