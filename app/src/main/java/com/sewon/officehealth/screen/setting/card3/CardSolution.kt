package com.sewon.officehealth.screen.setting.card3

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
import com.sewon.officehealth.screen.setting.card3.component.Modal1InduceEnergy
import com.sewon.officehealth.screen.setting.card3.component.Modal2InduceSound
import com.sewon.officehealth.screen.setting.card3.component.Modal3ScoreThreshold


// Card 3
@Composable
fun InductionSolutionSetting(
    switchColors: SwitchColors = SwitchDefaults.colors()

) {

    var openInduceEnergyModal by rememberSaveable { mutableStateOf(false) }
    var openInduceSoundModal by rememberSaveable { mutableStateOf(false) }
    var openScoreThresholdModal by rememberSaveable { mutableStateOf(false) }

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
                "수면유도 솔루션", fontSize = 18.sp, fontWeight = FontWeight(900), color = Color(0xFFEDEDED)
            )

            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = { openInduceEnergyModal = !openInduceEnergyModal }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("수면유도 에너지")

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
                modifier = Modifier.fillMaxWidth().clickable(onClick = { openInduceSoundModal = !openInduceSoundModal }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("수면유도 사운드")

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
                modifier = Modifier.fillMaxWidth().clickable(onClick = { openScoreThresholdModal = !openScoreThresholdModal }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("수면점수 임계값 설정")

                var checked by remember { mutableStateOf(true) }

                Switch(
                    colors = switchColors,
                    modifier = Modifier.semantics { contentDescription = "Demo" },
                    checked = checked,
                    onCheckedChange = { checked = it })
            }
        }
    }

    if (openInduceEnergyModal) {
        Modal1InduceEnergy(
            onToggleModal = { openInduceEnergyModal = !openInduceEnergyModal })
    }

    if (openInduceSoundModal) {
        Modal2InduceSound(
            onToggleModal = { openInduceSoundModal = !openInduceSoundModal })
    }

    if (openScoreThresholdModal) {
        Modal3ScoreThreshold(
            onToggleModal = { openScoreThresholdModal = !openScoreThresholdModal })
    }
}