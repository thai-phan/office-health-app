package com.sewon.officehealth.common

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sewon.officehealth.common.theme.BackgroundBottom
import com.sewon.officehealth.common.theme.BackgroundTop


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.whiteGreenBackground(): Modifier = this.composed {
  Modifier.drawWithCache {
    val gradientBrush = Brush.linearGradient(
      listOf(BackgroundTop, BackgroundBottom)
    )
    onDrawBehind {
      drawRect(gradientBrush)
    }
  }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.yellowBackground(): Modifier = this.composed {
  Modifier.drawWithCache {
    val gradientBrush = Brush.verticalGradient(
      listOf(Color(0xFFFAFF00), Color(0xFFFF8A00))
    )
    onDrawBehind {
      drawRect(gradientBrush)
    }
  }
}