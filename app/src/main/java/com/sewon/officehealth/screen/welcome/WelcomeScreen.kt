package com.sewon.officehealth.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.R
import com.sewon.officehealth.common.AppDestinations

@Composable
fun WelcomeScreen(
    navController: NavController,
) {

//  rgba(0, 150, 53, 1),
//
//
//  rgba(172, 225, 175, 1)

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .statusBarsPadding()
          .systemBarsPadding()
          .fillMaxSize()
          .paint(
            painterResource(id = R.mipmap.ic_image_2_foreground),
            alignment = Alignment.BottomCenter,
            contentScale = ContentScale.FillWidth
          ),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.mipmap.ic_image_5_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .size(width = 500.dp, height = 200.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "OFFICE HEALTH PROTECTOR!",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontWeight = FontWeight(700),
                color = Color(0x80000000),
                letterSpacing = 1.4.sp,
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "반갑습니다~ " +
                    " 당신의 건강 지킴이 오헬프!입니다. " +
                    " 먼저 기기를 연결해주세요",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 40.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                textAlign = TextAlign.Center,
            )
        )
        Button(onClick = {

            navController.navigate(AppDestinations.DEVICE_ROUTE)

        }) {
            Text("기기연결")
        }
    }


}