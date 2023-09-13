package com.sewon.officehealth.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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


@Composable
fun SplashScreen(navController: NavController, redirectRoute: String) {
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
        // Customize the delay time
        delay(1000L)
        navController.navigate(redirectRoute)
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .statusBarsPadding()
            .systemBarsPadding()
            .fillMaxSize()

    ) {
      Column(
        modifier = Modifier
          .width(344.dp)
          .height(608.7478.dp)
          .background(color = Color(0xFF106D34), shape = RoundedCornerShape(size = 608.7478.dp))
      ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
          painter = painterResource(id = R.mipmap.ic_image_5_foreground),
          contentDescription = "Logo",
          modifier = Modifier
            .size(width = 500.dp, height = 200.dp)
            .scale(scale.value)
        )
        Text(
          text = "OFFICE HEALTH PROTECTOR!",
          style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontWeight = FontWeight(700),
            color = Color(0x80000000),
            letterSpacing = 1.4.sp,
          )
        )
        Image(
          painter = painterResource(id = R.mipmap.ic_image_4_foreground),
          contentDescription = "Logo",
          modifier = Modifier
            .size(500.dp)
            .scale(scale.value)
        )
      }

    }


}
