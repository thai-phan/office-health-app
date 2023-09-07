package com.sewon.officehealth.screen.report.component


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun CircularChart(
    modifier: Modifier = Modifier,
    angle: Float = 50f,
    brush: Brush = Brush.verticalGradient(listOf(Color.White, Color.Black)),
    backgroundCircleColor: Color = Color.LightGray.copy(alpha = 0.3f),
    thickness: Dp = 12.dp,
    gapBetweenCircles: Dp = 20.dp
) {
    val sweepAngles = 360 * angle / 100

    var sizeCom by remember { mutableStateOf(IntSize.Zero) }



    Canvas(
        modifier = modifier
            .fillMaxWidth()
//            .background(Color(0xFF071224))
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                sizeCom = coordinates.size
            }
    ) {
        var arcRadius = sizeCom.width.toFloat()

        arcRadius -= gapBetweenCircles.toPx()

        drawCircle(
            color = backgroundCircleColor,
            radius = arcRadius / 2,
            style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
        )

        drawArc(
            brush = brush,
            startAngle = -90f,
            sweepAngle = sweepAngles,
            useCenter = false,
            style = Stroke(width = thickness.toPx(), cap = StrokeCap.Round),
            size = Size(arcRadius, arcRadius),
            topLeft = Offset(
                x = (sizeCom.width.toFloat() - arcRadius) / 2,
                y = (sizeCom.width.toFloat() - arcRadius) / 2
            ),
            blendMode = BlendMode.SrcOver
        )
    }
}