package com.sewon.officehealth.screen.setting.card3.component

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sewon.officehealth.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modal3ScoreThreshold(
    onToggleModal: () -> Unit
) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    var test = "aaaa"


    ModalBottomSheet(
        onDismissRequest = onToggleModal,
        sheetState = bottomSheetState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 50.dp)) {
            Text("header", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)


            Row(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                horizontalArrangement = Arrangement.Center) {
                AndroidView(
                    factory = { context ->
                        val view = LayoutInflater.from(context).inflate(R.layout.number_picker, null)
                        val picker = view.findViewById<NumberPicker>(R.id.number_picker)
                        val data = arrayOf("80", "81")

                        picker.minValue = 0
                        picker.maxValue = data.size - 1
                        picker.displayedValues = data
                        picker.setOnValueChangedListener { picker, oldVal, newVal ->
                            test = data.get(newVal)
                        }
                        picker

                    }
                )
            }

            Row(modifier = Modifier.fillMaxWidth().height(100.dp),
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