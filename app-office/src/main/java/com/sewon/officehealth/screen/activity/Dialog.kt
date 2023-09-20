package com.sewon.officehealth.screen.activity

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.sewon.officehealth.R

@Composable
fun AlertDialogExample(
  onDismissRequest: () -> Unit,
  onConfirmation: () -> Unit,
  dialogTitle: String,
  dialogText: String,
  icon: ImageVector,
  isPlay: Boolean
) {
  AlertDialog(
    icon = {
      Icon(icon, contentDescription = "Example Icon")
    },
    title = {
      Text(text = dialogTitle)
    },
    text = {
      Text(
        text = dialogText, style = TextStyle(
          fontSize = 20.sp,
          lineHeight = 31.2.sp,
          fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
          fontWeight = FontWeight(700),
          color = Color(0xFFFFFFFF),
          textAlign = TextAlign.Center,
        )
      )
    },
    onDismissRequest = {
      onDismissRequest()
    },
    confirmButton = {
      TextButton(
        onClick = {
          onConfirmation()
        }
      ) {
        if (!isPlay) {
          Text(
            "재생", style = TextStyle(
              fontSize = 20.sp,
              lineHeight = 31.2.sp,
              fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
              fontWeight = FontWeight(700),
              textAlign = TextAlign.Center,
            )
          )
        } else {
          Text(
            "정지", style = TextStyle(
              fontSize = 20.sp,
              lineHeight = 31.2.sp,
              fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
              fontWeight = FontWeight(700),
              textAlign = TextAlign.Center,
            )
          )
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          onDismissRequest()
        }
      ) {
        Text(
          "닫기", style = TextStyle(
            fontSize = 20.sp,
            lineHeight = 31.2.sp,
            fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_regular)),
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Center,
          )
        )
      }
    }
  )
}