package com.sewon.officehealth.screen.activity

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

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
      Text(text = dialogText, )
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
          Text("재생"  )
        } else {
          Text("정지")
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          onDismissRequest()
        }
      ) {
        Text("닫기")
      }
    }
  )
}