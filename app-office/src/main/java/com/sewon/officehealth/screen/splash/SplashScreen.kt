package com.sewon.officehealth.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sewon.officehealth.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SplashScreen(delayTime: Long, onRedirectRoute: () -> Unit) {
  val scale = remember {
    androidx.compose.animation.core.Animatable(0f)
  }

  // Animation
  LaunchedEffect(key1 = true) {
    scale.animateTo(
      targetValue = 0.7f,
      // tween Animation
      animationSpec = tween(
        durationMillis = 800,
        easing = {
          OvershootInterpolator(4f).getInterpolation(it)
        })
    )
    delay(delayTime)
    onRedirectRoute()
  }
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {

      Row(horizontalArrangement = Arrangement.End) {
        Image(
          modifier = Modifier
            .border(BorderStroke(1.dp, Color.Red))
            .height(250.dp),
          painter = painterResource(id = R.mipmap.ic_image_4_foreground),
          contentDescription = "Logo",

          )
      }


      Image(
        painter = painterResource(id = R.mipmap.ic_ohealp_foreground),
        contentDescription = "Logo",
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .scale(scale.value)
      )
//      FlowRow(horizontalArrangement = Arrangement.Center) {
//        val textStyle = TextStyle(
//          fontSize = 20.sp,
//          fontFamily = FontFamily(Font(R.font.jalnan)),
//          fontWeight = FontWeight(700),
//          color = Color(0xFFFFFFFF),
//          letterSpacing = 1.4.sp,
//        )
//        Text(text = "OFFICE HEALTH ", style = textStyle)
//        Text(
//          "PROTECTOR!", style = textStyle
//        )
//      }


//      Image(
//        painter = painterResource(id = R.mipmap.ic_logo_png_foreground),
//        contentDescription = "Table Logo",
//        modifier = Modifier
//          .size(200.dp)
//      )
    }
  }
}
