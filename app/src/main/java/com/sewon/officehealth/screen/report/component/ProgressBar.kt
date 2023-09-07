package com.sewon.officehealth.screen.report.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sewon.officehealth.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GradientProgressbar(
    viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    indicatorHeight: Dp = 10.dp,
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.3f),
    indicatorPadding: Dp = 10.dp,
    gradientColors: List<Color> = listOf(
        Color(0xFF6ce0c4),
        Color(0xFF40c7e7),
        Color(0xFF6ce0c4),
        Color(0xFF40c7e7)
    ),
    numberStyle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.suite_bold, FontWeight.Bold)),
        fontSize = 32.sp
    ),
    animationDuration: Int = 1000,
    animationDelay: Int = 0
) {
    val downloadedPercentage by viewModel.downloadedPercentage.observeAsState(initial = 0f)

    val animateNumber = animateFloatAsState(
        targetValue = downloadedPercentage,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    LaunchedEffect(Unit) {
        viewModel.startThreadGradient()
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(indicatorHeight)
            .padding(start = indicatorPadding, end = indicatorPadding)
    ) {

        // Background indicator
        drawLine(
            color = backgroundIndicatorColor,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        // Convert the downloaded percentage into progress (width of foreground indicator)
        val progress =
            (animateNumber.value / 100) * size.width // size.width returns the width of the canvas

        // Foreground indicator
        drawLine(
            brush = Brush.linearGradient(
                colors = gradientColors
            ),
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = progress, y = 0f)
        )

    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = downloadedPercentage.toInt().toString() + "%",
        style = numberStyle
    )

}

class MyViewModel : ViewModel() {

    var downloadedPercentage = MutableLiveData<Float>()

    fun startThreadGradient() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {


                val totalDownloadSize = 1024f
                var downloadedSize = 0f

//                while (true) {
//
//                    downloadedSize += ((1..100).random().toFloat())
//
//                    if (downloadedSize < totalDownloadSize) {
//                        withContext(Dispatchers.Main) {
//                            downloadedPercentage.value =
//                                ((downloadedSize / totalDownloadSize) * 100)
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            downloadedPercentage.value = 100f
//                        }
//                        break
//                    }
//
//                    delay(1000)
//                }

            }
        }
    }
}