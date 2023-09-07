package com.sewon.officehealth.screen.setting.card2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.setting.card2.SleepUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modal3AlarmSetting(
    uiState: SleepUiState, onChangeAlarmType: (String) -> Unit, onToggleModal: () -> Unit
) {

    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    var edgeToEdgeEnabled by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    var state by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {


        val iconOptions = listOf(
            R.drawable.ic_bell_on,
            R.drawable.ic_speaker_phone,
            R.drawable.ic_volume_off
        )

        val radioOptions = listOf("벨소리", "진동", "무음")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(uiState.alarmType) }

        // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("알람설정", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)

            Column(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                radioOptions.zip(iconOptions).forEach { pair ->
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .selectable(
                                selected = (pair.first == selectedOption),
                                onClick = { onOptionSelected(pair.first) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (pair.first == selectedOption),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = pair.first,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(50.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = pair.second),
                            contentDescription = "Logo",
                        )
                    }
                }
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
                    onChangeAlarmType(selectedOption)
                    onToggleModal()
                }) {
                    Text("저장")
                }
            }
        }


    }
}