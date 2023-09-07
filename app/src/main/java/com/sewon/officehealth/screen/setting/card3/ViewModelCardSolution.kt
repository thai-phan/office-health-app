package com.sewon.officehealth.screen.setting.card3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sewon.officehealth.R
import com.sewon.officehealth.data.model.Setting
import com.sewon.officehealth.data.repository.UserRepository
import com.sewon.officehealth.data.repository.SettingRepository
import com.sewon.officehealth.util.Async
import com.sewon.officehealth.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SleepUiState(
    val alarmTime: String = "",
    val alarmOn: Boolean = false,
    val isLoading: Boolean = false,
    val message: Int? = null
)

@HiltViewModel
class ViewModelCardSolution @Inject constructor(
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    var userId = 0
    private val _isLoading = MutableStateFlow(false)
    private val _message: MutableStateFlow<Int?> = MutableStateFlow(null)
    private var _settingAsync = settingRepository.loadUserSetting(userId).map {
        handleSetting(it)
    }
        .catch { emit(Async.Error(R.string.setting_not_found)) }

    val uiState: StateFlow<SleepUiState> = combine(_settingAsync, _message, _isLoading) {
        settingAsync, message, isLoading ->
        when (settingAsync) {
            Async.Loading -> {
                SleepUiState(isLoading = true)
            }

            is Async.Error -> {
                SleepUiState(message = settingAsync.errorMessage)
            }

            is Async.Success -> {
                SleepUiState(
                    alarmTime = settingAsync.data!!.alarmTime.toString(),
                    alarmOn = settingAsync.data.alarmOn,
                    message= message,
                    isLoading = isLoading
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = SleepUiState(isLoading = true)
    )

    fun toggleAlarmSetting(alarmOn: Boolean) = viewModelScope.launch {
//        settingRepository.updateAlarmSetting(userId, alarmOn)
    }

    private fun handleSetting(setting: Setting?): Async<Setting?> {
        if (setting == null) {
            return Async.Error(R.string.setting_not_found)
        }
        return Async.Success(setting)
    }


}