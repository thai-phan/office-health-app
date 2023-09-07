package com.sewon.officehealth.screen.setting.card4

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sewon.officehealth.screen.setting.card4.component.Modal2ClearHistory
import com.sewon.officehealth.screen.setting.card4.component.Modal1DeviceAccess
import com.sewon.officehealth.screen.setting.card4.component.Modal3SOSRecipient


// Card 4
@Composable
fun GeneralSetting(switchColors: SwitchColors =  SwitchDefaults.colors()) {

    var openDeviceAccessModal by rememberSaveable { mutableStateOf(false) }
    var openClearHistoryModal by rememberSaveable { mutableStateOf(false) }
    var openSOSRecipientModal by rememberSaveable { mutableStateOf(false) }


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
                "일반설정", fontSize = 18.sp, fontWeight = FontWeight(900), color = Color(0xFFEDEDED)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
//                Cell phone cache access rights
                Text("핸드폰 캐시 접근 권한")

                var checked by remember { mutableStateOf(true) }

                Switch(
                    colors = switchColors,
                    modifier = Modifier.semantics { contentDescription = "Demo" },
                    checked = checked,
                    onCheckedChange = { checked = it })
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color(0x1AFFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
//                Clear all sleep history
                Text("수면 기록 모두 지우기")

                var checked by remember { mutableStateOf(true) }

                Switch(
                    colors = switchColors,
                    modifier = Modifier.semantics { contentDescription = "Demo" },
                    checked = checked,
                    onCheckedChange = { checked = it })
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color(0x1AFFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

//                Emergency SOS Recipient Settings
                Text("위급 SOS 수신자 설정")

                var checked by remember { mutableStateOf(true) }

                Switch(
                    colors = switchColors,
                    modifier = Modifier.semantics { contentDescription = "Demo" },
                    checked = checked,
                    onCheckedChange = { checked = it })
            }
        }
    }

    if (openDeviceAccessModal) {
        Modal1DeviceAccess(
            onToggleModal = { openDeviceAccessModal = !openDeviceAccessModal })
    }

    if (openClearHistoryModal) {
        Modal2ClearHistory(
            onToggleModal = { openClearHistoryModal = !openClearHistoryModal })
    }

    if (openSOSRecipientModal) {
        Modal3SOSRecipient(
            onToggleModal = { openSOSRecipientModal = !openSOSRecipientModal })
    }
}