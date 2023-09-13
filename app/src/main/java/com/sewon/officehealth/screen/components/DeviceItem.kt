package com.sewon.officehealth.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DeviceItem(color: Color) {

  Row(
    modifier = Modifier
      .shadow(elevation = 10.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
      .width(320.dp)
      .height(80.dp)
      .background(color = color, shape = RoundedCornerShape(size = 10.dp)),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
    Text("Device name")
    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
  }
}