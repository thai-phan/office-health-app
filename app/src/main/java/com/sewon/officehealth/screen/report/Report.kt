package com.sewon.officehealth.screen.report


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sewon.officehealth.screen.report.component.CircularChart
import com.sewon.officehealth.screen.report.component.GradientProgressbar
import com.sewon.officehealth.screen.report.component.ThisChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(modifier: Modifier) {

    val context = LocalContext.current

//    val regularClass = RegularClass(context)
//    regularClass.findResource()

    val yellowBrush = Brush.verticalGradient(
        listOf(Color(0xFFFAFF00), Color(0xFFFF8A00))
    )
    val blueBrush = Brush.verticalGradient(
        listOf(Color(0xFF03DAC5), Color(0xFF3E4F9D))
    )

//    Donut chart 4 cai

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 10.dp,
                top = 20.dp,
                end = 10.dp,
                bottom = 10.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        Text("수면 리포트", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(10.dp))


        Row() {
            CircularChart(angle = 40f, modifier = Modifier.weight(1f), brush = yellowBrush)
            CircularChart(angle = 40f, modifier = Modifier.weight(1f), brush = blueBrush)
            CircularChart(angle = 80f, modifier = Modifier.weight(1f), brush = yellowBrush)
            CircularChart(angle = 70f, modifier = Modifier.weight(1f), brush = blueBrush)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("PQSI 수면평가점수")
        Spacer(modifier = Modifier.height(20.dp))

        GradientProgressbar()

        GradientProgressbar()

        GradientProgressbar()

        GradientProgressbar()


        ThisChart()

    }
}


@Preview
@Composable
fun PreviewReport() {
    Report(Modifier)
}