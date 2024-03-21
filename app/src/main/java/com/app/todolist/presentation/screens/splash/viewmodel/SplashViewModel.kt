package com.app.todolist.presentation.screens.splash.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.data.TodoListRepository
import com.app.todolist.network.ApiClient
import com.app.todolist.notification.NotificationScheduler
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val todoRepository: TodoListRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {
    data class SplashUIData(val isLoading: Boolean = false, val isError: Boolean = false)

    sealed class UIEvent() {
        object Success : UIEvent()
        data class Error(val message: Int) : UIEvent()
    }

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiData = mutableStateOf(SplashUIData())
    val uiData = _uiData

    fun fetchTasks() {
        viewModelScope.launch {

            _uiData.value = uiData.value.copy(isError = false, isLoading = true)


//            todoRepository.getAppSettings().collect { appSettings ->

            var isError = false
            var errorId = -1
//                if (!appSettings.isDataFetched) {
            if (!todoRepository.getIsDataFetched()) {
                try {
                    val listOfTasks = apiClient.getListOfMedicinesAndCategories()
                    val categories = listOfTasks.map { item -> item.category }

                    for (index in listOfTasks.indices) {
                        listOfTasks[index] = listOfTasks[index].copy(id = index + 1)
                    }

                    todoRepository.saveCategories(categories.toSet().toList())
                    todoRepository.saveTask(listOfTasks)

                    for (task in listOfTasks) {
                        notificationScheduler.scheduleNotificationWork(task)
                    }
                    todoRepository.setDataFetched(true)

                } catch (e: Exception) {
                    isError = true
                    errorId = when (e) {
                        is HttpException -> R.string.error_server
                        is SocketTimeoutException, is IOException -> R.string.internt_connection_error
                        is JsonIOException,
                        is JsonSyntaxException,
                        is JsonParseException,
                        -> R.string.error_unable_to_parse_response

                        else -> R.string.error_unkown
                    }
                }
            } else {
                delay(1000)

            }

            _uiData.value = uiData.value.copy(isError = isError, isLoading = false)

            if (isError) {
                _uiEvent.emit(UIEvent.Error(errorId))
            } else {
                _uiEvent.emit(UIEvent.Success)

            }
//            }

        }
    }

    suspend fun getLanguage(): String {
        return todoRepository.getLanguage().key
    }

//    suspend fun getDataStore() = todoRepository.getAppSettings()
}

