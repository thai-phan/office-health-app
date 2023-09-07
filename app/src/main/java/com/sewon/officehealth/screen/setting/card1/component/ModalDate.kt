package com.sewon.officehealth.screen.setting.card1.component

import android.view.LayoutInflater
import android.widget.DatePicker
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
import com.sewon.officehealth.screen.setting.card1.ProfileUiState
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDate(
    uiState: ProfileUiState,
    onSubmitBirthday: (Int, Int, Int) -> Unit,
    onToggleModal: () -> Unit,
) {
    val (year, setYear) = remember { mutableStateOf(uiState.calendar.get(Calendar.YEAR)) }
    val (month, setMonth) = remember { mutableStateOf(uiState.calendar.get(Calendar.MONTH)) }
    val (day, setDay) = remember { mutableStateOf(uiState.calendar.get(Calendar.DAY_OF_MONTH)) }

    val skipPartiallyExpanded by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )


    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("성별", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)
            AndroidView(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
                val datePicker = view.findViewById<DatePicker>(R.id.datePicker)

                datePicker.init(
                    year, month, day
                ) { _, year, monthOfYear, dayOfMonth ->
                    setYear(year)
                    setMonth(monthOfYear)
                    setDay(dayOfMonth)
                }
                datePicker
            })

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
                    onSubmitBirthday(year, month, day)
                    onToggleModal()
                }) {
                    Text("저장")
                }
            }
        }

    }
}