package com.sewon.officehealth.screen.singleview

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .statusBarsPadding()
            .systemBarsPadding()
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .scale(scale.value)
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            buildAnnotatedString {
                append("건강 ")
                withStyle(style = SpanStyle(color = Color(0xFF03dac5))) {
                    append("모니터링")
                }
                append(" 토-퍼")
            },
//            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 26.sp,
        )
    }


}
