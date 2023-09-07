package com.sewon.officehealth.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.setting.card1.ProfileSetting
import com.sewon.officehealth.screen.setting.card2.SleepSetting
import com.sewon.officehealth.screen.setting.card3.InductionSolutionSetting
import com.sewon.officehealth.screen.setting.card4.GeneralSetting
import com.sewon.officehealth.common.theme.checkedBorderColor
import com.sewon.officehealth.common.theme.checkedThumbColor
import com.sewon.officehealth.common.theme.checkedTrackColor
import com.sewon.officehealth.common.theme.uncheckedBorderColor
import com.sewon.officehealth.common.theme.uncheckedThumbColor
import com.sewon.officehealth.common.theme.uncheckedTrackColor
import com.sewon.officehealth.screen.setting.card5.DeviceConnectionSetting

@Composable
fun UserSetting(
    modifier: Modifier = Modifier,
    viewModel: ViewModelUserSetting = hiltViewModel()
) {
    val switchColors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedBorderColor = checkedBorderColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedBorderColor = uncheckedBorderColor,
    )
    Column(
        modifier = modifier.padding(
                start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp
            ),
    ) {
        Text(stringResource(R.string.setting_title), fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProfileSetting()

            SleepSetting(switchColors = switchColors)

            InductionSolutionSetting(switchColors = switchColors)

            GeneralSetting(switchColors = switchColors)

            DeviceConnectionSetting()
        }

//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

    }

}

@Preview
@Composable
fun PreviewUserSetting() {
    UserSetting()
}