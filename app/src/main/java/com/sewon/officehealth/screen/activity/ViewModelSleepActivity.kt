package com.sewon.officehealth.screen.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class UiState(
    val status1: String = "",
    val status2: String = "",
    val status3: String = "",
    val description: String = "",
    val isTaskCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)


@HiltViewModel
class ViewModelSleepActivity @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    fun createNewTask() = viewModelScope.launch {
//        val localRadar = LocalRadar(0.1f, 0.1f, 0.1f, "X", "X", "X", "X")
//
//        radarRepository.createTopper(localRadar)
//        _uiState.update {
//            it.copy(status1 = "Ahahahah")
//        }
    }

    fun getToppers() = viewModelScope.launch {

//        var aaa = radarRepository.getTopper()
//
//        _uiState.update {
//            it.copy(status2 = aaa.first().get(0).moving)
//        }
    }

    fun getCount() = viewModelScope.launch {

//        var bbb = radarRepository.getCountTopper()
//
//        _uiState.update {
//            it.copy(status3 = bbb.first().toString())
//        }
    }



    val exceptionHandler = CoroutineExceptionHandler { _, error ->
        // Do what you want with the error
        Timber.e( error)
    }

    fun queryFromServer() = viewModelScope.launch(exceptionHandler) {
//        try {
//            val quotesApi = ServerService.create().testServer()
//            quotesApi.let {
//                Timber.v(it.totalCount.toString())
//            }
//            _uiState.update {
//                it.copy(status3 = quotesApi.count.toString())
//            }
//        }  catch (error: Error) {
//            _uiState.update {
//                it.copy(status3 = "Disconnect")
//            }
//        }
        // launching a new coroutine

        println("asdas")
    }

    fun queryFromServerHttp() = viewModelScope.launch {
//        try {
//            val quotesApi = HttpService.create().testServer()
//            quotesApi.let {
//                Timber.v(it.data)
//            }
//            // launching a new coroutine
//            _uiState.update {
//                it.copy(status3 = quotesApi.data)
//            }
//        } catch (error: Error) {
//            Timber.v("catch")
//            _uiState.update {
//                it.copy(status3 = "Disconnect")
//            }
//        }


        println("asdas")
    }

    fun updateTime() = viewModelScope.launch {
//        settingRepository.countSetting()

    }

}