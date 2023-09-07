package com.sewon.officehealth.screen.report.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularChart3(
    angleList: List<Float> = listOf(65f, 60f, 55f),
    colors: List<Color> = listOf(
        Color(0xFFbe1558),
        Color(0xFFe75874),
        Color(0xFFfbcbc9)
    ),
    backgroundCircleColor: Color = Color.LightGray.copy(alpha = 0.3f),
    legend: List<String> = listOf("Mango", "Apple", "Melon"),
    size: Dp = 200.dp,
    thickness: Dp = 16.dp,
    gapBetweenCircles: Dp = 42.dp
) {

    // Convert each value to angle
    val sweepAngles = angleList.map {
        360 * it / 100
    }

    Canvas(
        modifier = Modifier
            .size(size)
    ) {

        var arcRadius = size.toPx()

        for (index in angleList.indices) {

            arcRadius -= gapBetweenCircles.toPx()

            drawCircle(
                color = backgroundCircleColor,
                radius = arcRadius / 2,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
            )

            drawArc(
                color = colors[index],
                startAngle = -90f,
                sweepAngle = sweepAngles[index],
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Round),
                size = Size(arcRadius, arcRadius),
                topLeft = Offset(
                    x = (size.toPx() - arcRadius) / 2,
                    y = (size.toPx() - arcRadius) / 2
                )
            )

        }

    }

    Spacer(modifier = Modifier.height(32.dp))

    Column {
        for (i in angleList.indices) {
            DisplayLegend(color = colors[i], legend = legend[i])
        }
    }

}

@Composable
fun DisplayLegend(color: Color, legend: String) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = legend,
            color = Color.White
        )
    }
}