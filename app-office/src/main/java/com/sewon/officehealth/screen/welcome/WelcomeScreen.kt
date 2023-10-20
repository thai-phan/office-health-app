package com.sewon.officehealth.screen.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.AppDestinations
import com.sewon.officehealth.screen.a0common.whiteBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
  navController: NavController,
) {

  fun navigateToDevice() {
    navController.navigate(AppDestinations.DEVICE_ROUTE)
  }


  val textStyle = TextStyle(
    fontSize = 24.sp,
    lineHeight = 50.sp,
    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    fontWeight = FontWeight(600),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
  )
  Column(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      modifier = Modifier
        .weight(2f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Image(
        modifier = Modifier
          .fillMaxSize(0.7f),
        painter = painterResource(id = R.drawable.ic_ohealp),
        contentDescription = "Logo",
      )
    }

    Column(
      modifier = Modifier
//        .border(BorderStroke(1.dp, Color.Red))
        .weight(2f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = "반갑습니다~ ",
        style = textStyle
      )
      Spacer(
        modifier = Modifier
          .fillMaxWidth(fraction = 0.8f)
          .height(1.dp)
          .background(color = Color(0x66000000))
      )
      FlowRow(horizontalArrangement = Arrangement.Center) {
        Text(
          text = "당신의 건강 지킴이 ",
          style = textStyle
        )
        Text(
          text = "오헬프!",
          style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 50.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontWeight = FontWeight(600),
            color = Color(0xFF4DB39D),
            textAlign = TextAlign.Center,
          )
        )
        Text(
          text = " 입니다.",
          style = textStyle
        )
      }
      Spacer(
        modifier = Modifier
          .fillMaxWidth(fraction = 0.8f)
          .height(1.dp)
          .background(color = Color(0x66000000))
      )
      Text(
        text = " 먼저 기기를 연결해주세요",
        style = textStyle
      )
      Spacer(
        modifier = Modifier
          .fillMaxWidth(fraction = 0.8f)
          .height(1.dp)
          .background(color = Color(0x66000000))
      )
    }
    Column(
      modifier = Modifier
        .weight(6f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.SpaceAround,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Button(
        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
        modifier = Modifier
          .width(320.dp)
          .height(73.dp)
          .background(
            color = Color(0xFFFFFFFF),
            shape = RoundedCornerShape(size = 999.dp)
          ),
        onClick = { navigateToDevice() }
      ) {
        Text(
          "기기연결",
          style = TextStyle(
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.jalnan)),
            fontWeight = FontWeight(700),
            color = Color(0xFF4DB39D),
          )
        )
      }
      Image(
        painter = painterResource(id = R.mipmap.ic_image_3_foreground),
        contentDescription = "Logo",
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
      )
    }
  }
}