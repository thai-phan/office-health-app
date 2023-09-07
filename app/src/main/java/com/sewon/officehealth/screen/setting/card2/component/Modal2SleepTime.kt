package com.sewon.officehealth.screen.setting.card2.component

import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.setting.card2.SleepUiState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modal2SleepTime(
    uiState: SleepUiState,
    onChangeBedTime: (LocalTime) -> Unit,
    onToggleModal: () -> Unit
) {

    val (time, setTime) = remember { mutableStateOf(uiState.alarmTime) }

    val skipPartiallyExpanded by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )


    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("취침시간", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    factory = { context ->
                        val view = LayoutInflater.from(context).inflate(R.layout.time_picker, null)
                        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)

                        timePicker.hour = time.hour
                        timePicker.minute = time.minute
                        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                            setTime(LocalTime.of(hourOfDay, minute))
                        }
                        timePicker
                    }
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onToggleModal) {
                    Text("취소")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    onChangeBedTime(time)
                    onToggleModal()
                }) {
                    Text("저장")
                }
            }


        }



    }
}