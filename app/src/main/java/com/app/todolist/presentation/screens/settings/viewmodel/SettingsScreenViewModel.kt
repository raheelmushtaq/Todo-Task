package com.app.todolist.presentation.screens.settings.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.data.models.AppSettings
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.presentation.screens.settings.modal.Languages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(private val dataStoreHandler: DataStoreHandler) :
    ViewModel() {
    private val _dataState = mutableStateOf(SettingUIState())
    val dataState: State<SettingUIState> = _dataState


    private val _appSettings = mutableStateOf(AppSettings())
    val appSettings: MutableState<AppSettings> = _appSettings

    init {
        listenToAppSettings()
    }


    private fun listenToAppSettings() {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: SettingsUIEvent) {
        when (event) {
            is SettingsUIEvent.ChangeLanguage -> {
                _dataState.value = dataState.value.copy(
                    language = event.language, false, showConfirmationDialog = false
                )
                viewModelScope.launch {
                    dataStoreHandler.changeLanguage(language = event.language)
                }
            }

            is SettingsUIEvent.ChangeNotification -> {
            }

            is SettingsUIEvent.ShowDialog -> {
                _dataState.value = dataState.value.copy(showConfirmationDialog = true)
            }
        }

    }
}

data class SettingUIState(
    val language: Languages = Languages.English,
    val isNotificationOn: Boolean = false,
    val showConfirmationDialog: Boolean = false
)


sealed class SettingsUIEvent {
    data class ChangeLanguage(val language: Languages) : SettingsUIEvent()
    data class ChangeNotification(val searchText: String) : SettingsUIEvent()
    data object ShowDialog : SettingsUIEvent()
}