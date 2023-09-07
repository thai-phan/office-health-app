package com.sewon.officehealth.screen.setting.card1.component

import android.view.LayoutInflater
import android.widget.NumberPicker
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalGender(
    uiState: ProfileUiState,
    onChangeGender: (value: String) -> Unit,
    onToggleModal: () -> Unit,
) {

    val (gender, setGender) = remember { mutableStateOf(uiState.gender) }

    val skipPartiallyExpanded by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )


    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("연령", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                AndroidView(
                    factory = { context ->
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.number_picker, null)
                        val picker = view.findViewById<NumberPicker>(R.id.number_picker)
                        val data = arrayOf("남성", "여성")
                        picker.minValue = 0
                        picker.maxValue = data.size - 1
                        picker.displayedValues = data
                        picker.value = data.indexOf(gender)
                        picker.setOnValueChangedListener { picker, oldVal, newVal ->
                            setGender(data.get(newVal))
                            // do your other stuff depends on the new value
                        }
                        picker
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
                    onChangeGender(gender)
                    onToggleModal()
                }) {
                    Text("저장")
                }
            }
        }

    }
}