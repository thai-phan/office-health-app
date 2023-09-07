package com.sewon.officehealth.screen.setting.card2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sewon.officehealth.screen.setting.card2.component.Modal3AlarmSetting
import com.sewon.officehealth.screen.setting.card2.component.Modal2SleepTime
import com.sewon.officehealth.screen.setting.card2.component.Modal1WakeUpTime

@Composable
fun SleepSetting(
    switchColors: SwitchColors = SwitchDefaults.colors(),
    viewModel: ViewModelCardSleep = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var openWakeupModal by rememberSaveable { mutableStateOf(false) }
    var openSleepTimeModal by rememberSaveable { mutableStateOf(false) }
    var openAlarmTypeModal by rememberSaveable { mutableStateOf(false) }


    Card(
        shape = RoundedCornerShape(size = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x33000000))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)

        ) {
            Text(
                "수면설정", fontSize = 18.sp, fontWeight = FontWeight(900), color = Color(0xFFEDEDED)
            )
            Column(
                modifier = Modifier
                    .clickable(onClick = { openWakeupModal = !openWakeupModal })
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
//                기상알람시간 Wake Up alarm time
                    Text("기상알람시간")
                    Switch(colors = switchColors,
                        checked = uiState.alarmOn,
                        onCheckedChange = {
                            viewModel.toggleAlarmOn(it)
                        })
                }
                Text(uiState.alarmTime.toString())
            }

            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color(0x1AFFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier
                    .clickable(onClick = { openSleepTimeModal = !openSleepTimeModal }),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
//                취침시간 Bedtime
                    Text("취침시간")
                    Switch(colors = switchColors,
                        checked = uiState.bedOn,
                        onCheckedChange = {
                            viewModel.toggleBedOn(it)
                        })
                }
                Text(uiState.bedTime.toString())
            }

            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color(0x1AFFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))
            Column(modifier = Modifier
                .clickable(onClick = { openAlarmTypeModal = !openAlarmTypeModal }),) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
//                알람설정 Alarm settings
                    Text("알람설정")
                    Switch(colors = switchColors,
                        checked = openAlarmTypeModal,
                        onCheckedChange = {
                            openAlarmTypeModal = !openAlarmTypeModal
                        })
                }
                Text(uiState.alarmType)
            }
        }
    }


    if (openWakeupModal) {
        Modal1WakeUpTime(
            uiState,
            onChangeAlarmTime = viewModel::changeAlarmTime,
            onToggleModal = { openWakeupModal = !openWakeupModal })
    }

    if (openSleepTimeModal) {
        Modal2SleepTime(
            uiState,
            onChangeBedTime = viewModel::changeBedTime,
            onToggleModal = { openSleepTimeModal = !openSleepTimeModal })
    }


    if (openAlarmTypeModal) {
        Modal3AlarmSetting(
            uiState,
            onChangeAlarmType = viewModel::changeAlarmTypeSetting,
            onToggleModal = { openAlarmTypeModal = !openAlarmTypeModal })
    }
}