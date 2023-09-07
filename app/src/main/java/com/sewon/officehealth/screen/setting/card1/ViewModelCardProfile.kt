package com.sewon.officehealth.screen.setting.card1

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sewon.officehealth.R
import com.sewon.officehealth.data.model.User
import com.sewon.officehealth.data.repository.UserRepository
import com.sewon.officehealth.util.Async
import com.sewon.officehealth.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


data class ProfileUiState(
    val calendar: Calendar = Calendar.getInstance(),
    val birthday: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)


@HiltViewModel
class ViewModelCardProfile @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    var curUsername = "admin_id"
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private var _userAsync = userRepository.getUserByUsername(curUsername).map { handleUser(it) }
        .catch { emit(Async.Error(R.string.loading_user_error)) }


    val uiState: StateFlow<ProfileUiState> =
        combine(_userAsync, _userMessage, _isLoading) { userAsync, userMessage, isLoading ->
            when (userAsync) {
                Async.Loading -> {
                    ProfileUiState(isLoading = true)
                }

                is Async.Error -> {
                    ProfileUiState(userMessage = userAsync.errorMessage)
                }

                is Async.Success -> {
                    val calendar = Calendar.getInstance()
                    if (userAsync.data !== null) {
                        calendar.time = userAsync.data.birthday
                    }
                    val dateformat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                    val birthdayString = dateformat.format(calendar.time)
                    ProfileUiState(
                        calendar = calendar,
                        gender = userAsync.data!!.gender,
                        birthday = birthdayString,
                        isLoading = isLoading,
                        userMessage = userMessage
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ProfileUiState(isLoading = true)
        )

    init {
//        https://stackoverflow.com/questions/73839026/jetpack-compose-displaying-data-in-compose-using-mvvm
        CoroutineScope(Dispatchers.IO).launch {
            Timber.tag("asdfasdf").d("CoroutineScope IO")
//            userSetting = userSettingRepository.loadUserSetting()
//            print(userSetting.first().get(0).gender)
        }
//            /* _posts.value is used now due to the datatype change */
//            _posts.value = KtorClient.httpClient.get("https://learnchn.herokuapp.com/") {
//                header("Content-Type", "application/json")
//            }
//
//            Log.d("HomeViewModel", "init: ${_posts.value[1].phrase}")
//            Log.d("HomeViewModel", "init: ${_posts.value[1].id}")
//        }
    }

    fun changeGender(gender: String) = viewModelScope.launch {
        userRepository.updateUserGender(curUsername, gender)
    }

    fun changeBirthday(year: Int, month: Int, day: Int) = viewModelScope.launch {
        Timber.d("changeBirthday")
        val date = Calendar.getInstance().apply {
            set(year, month, day)
        }.time
        userRepository.updateUserBirthday(curUsername, date)
    }

    override fun onCleared() {
        Timber.d("onCleared")
    }

    private fun handleUser(user: User?): Async<User?> {
        if (user == null) {
            return Async.Error(R.string.user_not_found)
        }
        return Async.Success(user)
    }
}