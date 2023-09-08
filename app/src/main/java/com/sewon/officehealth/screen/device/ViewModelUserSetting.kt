package com.sewon.officehealth.screen.device

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class ProfileSettingUiState(
    val calendar: Calendar = Calendar.getInstance(),
    val birthdayString: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)


@HiltViewModel
class ViewModelUserSetting @Inject constructor(
) : ViewModel() {

    private fun loadUser() {
        viewModelScope.launch {
//            if (userRepository.countUser().first() == 0) {
//                val str = "1955-04-16"
//                val df = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
//                val date: Date = df.parse(str)
//                val curDate = Calendar.getInstance().time
//                val user = User("admin_id", "admin", "남성", date, "cc", curDate, curDate)
//                userRepository.addUser(user)
//            }
//
//            if (settingRepository.countSetting().first() == 0) {
//                val curDate = Calendar.getInstance().time
//                val aaa = LocalTime.now()
//                val setting = Setting(
//                    userId = 0,
//                    alarmTime = aaa,
//                    bedTime = aaa,
//                    alarmOn = false,
//                    alarmSetting = "123",
//                    bedOn = false,
//                    energyOn = false,
//                    soundOn = false,
//                    cacheOn = false,
//                    initOn = false,
//                    sosOn = false,
//                    productSn = "aa",
//                    threshold = "threshold",
//                    createdAt = curDate,
//                    updatedAt = curDate,
//                    )
//                settingRepository.addSetting(setting)
//
//            }
        }
    }

    init {
        // Seed data
        loadUser()
    }

//    private fun handleTask(user: User?): Async<User?> {
//        if (user == null) {
//            return Async.Error(R.string.user_not_found)
//        }
//        return Async.Success(user)
//    }

    override fun onCleared() {
        Timber.d("onCleared")
//        coroutineScope.cancel()
    }


}