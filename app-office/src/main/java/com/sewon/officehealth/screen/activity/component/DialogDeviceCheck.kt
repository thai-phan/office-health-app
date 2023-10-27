package com.sewon.officehealth.screen.activity.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sewon.officehealth.MainActivity

@Composable
fun DeviceCheckDialog() {
  val isWrongDeviceUi by remember { MainActivity.lowEnergyClient.isWrongDeviceType }

  fun onDismissRequest() {
    MainActivity.lowEnergyClient.isWrongDeviceType.value = false
  }

  when {
    isWrongDeviceUi -> {
      Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp),
          colors = CardDefaults.cardColors(Color(0xFF4EA162)),
          shape = RoundedCornerShape(16.dp),
        ) {
          Text(
            text = "Wrong device!",
            color = Color(0xFF003917),
            modifier = Modifier
              .fillMaxSize()
              .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}