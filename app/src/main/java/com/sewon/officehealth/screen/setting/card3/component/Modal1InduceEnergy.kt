package com.sewon.officehealth.screen.setting.card3.component

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modal1InduceEnergy(
    onToggleModal: () -> Unit) {

    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    var edgeToEdgeEnabled by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("header", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)

            Text(
                "미주신경 자극으로 심신을 안정화시켜 잠에 빨리 들 수 있도록 도와줍니다. " +
                        "소리에 민감한 사용자에게 추천합니다."
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
                horizontalArrangement = Arrangement.Center) {
                Button(onClick = onToggleModal) {
                    Text("취소")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {}) {
                    Text("저장")
                }
            }
        }


    }
}