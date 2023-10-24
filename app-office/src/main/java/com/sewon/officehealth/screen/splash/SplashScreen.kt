package com.sewon.officehealth.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sewon.officehealth.R
import kotlinx.coroutines.delay


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
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp)
  ) {
    Column(
      modifier = Modifier
        .weight(3f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Bottom
    ) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Image(
          painter = painterResource(id = R.mipmap.ic_image_4_foreground),
          contentDescription = "Logo",
        )
      }
      Image(
        painter = painterResource(id = R.drawable.ic_ohealp),
        contentDescription = "Logo",
        modifier = Modifier
//          .border(BorderStroke(1.dp, Color.Red))
          .fillMaxWidth()
          .fillMaxHeight(0.3f)
      )
    }
    Column(
      modifier = Modifier
        .weight(2f),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally

    ) {
      Image(
        painter = painterResource(id = R.mipmap.mm_sewon_white_foreground),
        contentDescription = "Logo",
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.5f)
      )
    }

  }
}
