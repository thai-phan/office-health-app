package com.sewon.officehealth.screen.activity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sewon.officehealth.MainActivity
import com.sewon.officehealth.R

@Composable
fun LogView() {
  val logUI by remember { MainActivity.bleServiceListener.log }
  val uiCountHR by remember { MainActivity.bleServiceListener.realtimeDataObject.stressObj.countReferenceHR }
  val uiCountBR by remember { MainActivity.bleServiceListener.realtimeDataObject.stressObj.countReferenceBR }
  val uiRefHR by remember { MainActivity.bleServiceListener.realtimeDataObject.stressObj.refHR }
  val uiRefBR by remember { MainActivity.bleServiceListener.realtimeDataObject.stressObj.refBR }
  val uiStressMessage by remember { MainActivity.bleServiceListener.realtimeDataObject.stressObj.stressMessage }

  var isShowLog by remember { mutableStateOf(false) }



  Row(
    modifier = Modifier
      .padding(10.dp)
  ) {


    Button(
      colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
      modifier = Modifier
        .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 999.dp)),
      onClick = { isShowLog = !isShowLog }
    ) {
      Text(
        text = "Log",
        style = TextStyle(
          fontFamily = FontFamily(Font(R.font.jalnan)),
          color = Color(0xFF4EA162),
        )
      )
    }
    if (isShowLog) {
      Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(logUI, fontWeight = FontWeight.Bold, color = Color.Black)
        Row() {
          Text("HR count $uiCountHR", color = Color.Black)
          Spacer(modifier = Modifier.width(10.dp))
          Text("BR count $uiCountBR", color = Color.Black)
        }
        Row() {
          Text("HR ref $uiRefHR", color = Color.Black)
          Spacer(modifier = Modifier.width(10.dp))
          Text("BR ref $uiRefBR", color = Color.Black)
        }
        Text(uiStressMessage, color = Color.Black)
      }
    }
  }
}