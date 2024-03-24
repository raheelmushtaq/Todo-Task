package com.app.todolist.presentation.screens.splash.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.network.ApiClient
import com.app.todolist.notification.NotificationScheduler
import com.app.todolist.presentation.screens.splash.state_event.SplashUIData
import com.app.todolist.presentation.screens.splash.state_event.SplashUIEvent
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/*
* ViewModel: SplashViewModel
* this is linked with the AddEditScreen.
* it takes 3 paramters
* dataStoreHandler for adding or updating task in datastore
* notificationSchedular to to schedule a notification for user
* apiClint to get the values from the server.
* */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val dataStoreHandler: DataStoreHandler,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    // created a shared flow, this is listened in the SplashScreen, when event is firest as success of failuer is sent then the value is updated in this flow
    private val _uiEvent = MutableSharedFlow<SplashUIEvent>()

    // listening is as a shared flow in the activitu
    val uiEvent = _uiEvent.asSharedFlow()

    // created a mutable state for user with default value,
    //addking loading and erro state loader using this
    private val _dataState = mutableStateOf(SplashUIData())

    //this is State variable of the _dataState, it is not mutable,
    val dataState = _dataState

    /*
    * fetchtask is used to fetch the data from the api or from the  datastore*/
    fun fetchTasks() {
        viewModelScope.launch {

            // set the load as true to a loader is shown at the app
            _dataState.value = dataState.value.copy(isError = false, isLoading = true)

            // setting error by deult to -1
            var errorId = -1
            // getting the first value found fromteh get Appsettings
            // this is because if done beccause if listener is attached to it, then the api was even if the data is already saved
            val settings = dataStoreHandler.getAppSettings().first()
            // checkinbg if in any categories are already saved. if size is  that means categories aren ot saved in the datastore
            if (settings.categories.size == 0) {
                try {
                    //fetching the tasks from server
                    val listOfTasks = apiClient.getTasks()
                    // fetching the llist o categories from the tasks
                    val categories = listOfTasks.map { item -> item.category }

                    // as all the task does not have id's from start, loopping througgh the all the tasks and adding id for the tasks
                    for (index in listOfTasks.indices) {
                        listOfTasks[index] = listOfTasks[index].copy(id = index + 1)
                    }

                    // saving the categories to the data store in this condition.
                    // that if categories from servor are empty., then create custom list
                    // if not then categories are returned
                    // make it to set in this way we will have only unique elmenets
                    dataStoreHandler.saveCategories(categories.ifEmpty {
                        listOf(
                            "Other",
                            "Family",
                            "Office",
                            "Work"
                        )
                    }.toSet().toList())
                    // saving all the list tags to the server
                    dataStoreHandler.addTasks(listOfTasks)

                    // for all the task created show noifiation
                    for (task in listOfTasks) {
                        notificationScheduler.scheduleNotificationWork(task)
                    }
                    // if their is exception while handing the api or response, then catch block is called

                } catch (e: Exception) {
                    // just getting the error id incase of errors
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
                // if the data is already saved then show a delay of 1 second then send event for succes
                delay(1000)

            }

            // settings data state to user i.e. setting error and loading
            _dataState.value = dataState.value.copy(isError = errorId != -1, isLoading = false)

            //sentind event success if erroId == -1, if it not then send error
            if (errorId != -1) {
                _uiEvent.emit(SplashUIEvent.Error(errorId))
            } else {
                _uiEvent.emit(SplashUIEvent.Success)

            }


        }
    }

}

