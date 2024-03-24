package com.app.todolist.presentation.screens.add_edit_task.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.datastore.DataStoreHandlerInterface
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.notification.NotificationSchedulerInterface
import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditActionEvent
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditDataState
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditUIEvent
import com.app.todolist.presentation.utils.filters.TaskPriority
import com.app.todolist.presentation.utils.screens.ScreenParams.TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* ViewModel: AddEditTodoViewModel
* this is linked with the AddEditScreen.
* it takes 3 paramters
* dataStoreHandler for adding or updating task in datastore
* notificationSchedular to to schedule a notification for user
* saveStateHandle to get the values from the routes if passed.
* */
@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val dataStoreHandler: DataStoreHandlerInterface,
    private val notificationScheduler: NotificationSchedulerInterface,
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    // created a mutable state for user with default value,
    // when user enters a title, then this state is updated with the lates title
    private var _dataState = mutableStateOf(AddEditDataState())
    //this is State variable of the _dataState, it is not mutable, so in AddEditScreen it is used to listen for update and recompose the ui on this state change
    val dataState: State<AddEditDataState> = _dataState

    // created a shared flow, this is listened in the AddEventTaskScreen, not is saved or an error is generated ten the value is updated in this flow
    private val _addEditUiEvent = MutableSharedFlow<AddEditUIEvent>()
    // listening is as a shared flow in the activitu
    val addEditUiEvent = _addEditUiEvent.asSharedFlow()

    //create a mutable state for listening to the datastore from the datastore. when datastore is updated or at first launch of viewmode, this value is updated
    private val _appSettings = mutableStateOf(AppSettings())

    //this is State variable of the _appsettings, it is not mutable, so in AddEditScreen it is used to listen for update and recompose the ui on this state change
    val appSettings: MutableState<AppSettings> = _appSettings

    // this variable is used to check if the we are editing a task or adding a new task. if it is -1 then it means we are adding a new task but else it is editing
    // it is not requried in the UI to show to user that why it is not part of the dataState
    private var currentTaskId: Int = -1


    init {
        // in this init of viewmodel  added a flow listener on the datastore, update the _appsettings value
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
            }.launchIn(viewModelScope)
        }

        // getting the value passed from to the viewmodel from the TaskListScreen
        savedStateHandle.get<Int>(TASK_ID)?.let { taskId ->
            // check if task id sent from does exists, default value is set to -1
            if (taskId != -1) {
                // if the id is not -1, then fetch the task from the _appsettings
                val task = getTaskById(taskId)
                //update the UI i.e. _datastate to all the text infomation is shown to user
                updateTaskData(task)
            }
        }
    }

    /*
    * getTaskById this takes task id as input and returns a Tasks classk*/
    public fun getTaskById(taskId: Int): Tasks? {
        // getting the value from _appsettings, and find the task using filter.
        val filterTasks = _appSettings.value.tasks.toList().filter { task -> task.id == taskId }
        //and check if the value exist then return the first task found else return null
        return if (filterTasks.isNotEmpty()) {
            filterTasks[0]

        } else null
    }

    /*
    * updateTaskData takes the Tasks object and update the datastate for recomposition and to show data in the UI */
    private fun updateTaskData(task: Tasks?) {
        //check if task is null
        if (task != null) {
            // set the curerntTaskId  from the task id
            currentTaskId = task.id

            // get the priroty from the task from.
            // if my any chance the priorotiy value of empty, then a default value Low is used to get the priority

            val priority = arrayListOf(
                TaskPriority.Low, TaskPriority.Medium, TaskPriority.High
            ).filter {
                it.value.equals(
                    task.priority.ifEmpty { TaskPriority.Low.value }, true
                )
            }[0]
            // updating the datastate value
            _dataState.value = _dataState.value.copy(
                title = task.title,
                description = task.description,
                taskPriority = priority,
                category = task.category,
                isCompleted = task.isCompleted,
                date = task.date,
            )
        }
    }

    /*
    * when user perform action on the UI, like pressing the save button , then this function is called.
    * this funtion takes the AddEditActionEvent as input and does the remaining handling
    */
    fun onEvent(event: AddEditActionEvent) {
        //checking which kind of action user whas perform
        when (event) {
            // user has entered title, then update the title in state
            is AddEditActionEvent.EnterTitle -> {
                _dataState.value = dataState.value.copy(title = event.text)
            }
            // user has entered description, then update the descrion in state
            is AddEditActionEvent.EnterDescription -> {
                _dataState.value = dataState.value.copy(description = event.text)
            }

            // user has entered Priority, then update the selected priority in state
            is AddEditActionEvent.EnterPriority -> {
                _dataState.value = dataState.value.copy(taskPriority = event.taskPriority)
            }

            // user has entered Category, then update the Category n the state
            is AddEditActionEvent.EnterCategory -> {
                _dataState.value = dataState.value.copy(category = event.value)
            }

            // user has entered DueDate, then update the date in the state
            is AddEditActionEvent.SelectDueDate -> {
                _dataState.value = dataState.value.copy(date = event.date)
            }

            // user has pressed Save button, then save task functionality is performd
            is AddEditActionEvent.SaveTask -> {

                //checking if we are editing the task or adding the task
                val isEditTask = currentTaskId != -1
                //from the pervous to values in the datastore, get teh  record count value and pass it as the idea bby makin it unique
                val id = if (isEditTask) currentTaskId else appSettings.value.recordCount + 1

                // function call to save the task
                saveTask(
                    id = id,
                    title = dataState.value.title,
                    description = dataState.value.description,
                    category = dataState.value.category,
                    date = dataState.value.date,
                    priority = dataState.value.taskPriority?.value,
                    isCompleted = false,
                    isEditTask
                )
            }
        }
    }

    /*
    * Save tasks function is to perform vallidation that a field is not missing and save or udpate Task*/
    public fun saveTask(
        id: Int,
        title: String,
        description: String,
        category: String?,
        date: String,
        priority: String?,
        isCompleted: Boolean,
        isEditTask: Boolean
    ) {
        viewModelScope.launch {
            //in stead of passing string, stringRes is passed in case of error, so we can see the translation of erro message
            // applying empty Field Valudation for now
            var errorId = -1
            if (title.isEmpty()) {
                errorId = R.string.title_is_required
            } else if (description.isEmpty()) {
                errorId = R.string.description_is_required
            } else if (priority == null) {
                errorId = R.string.priority_is_required
            } else if (category == null) {
                errorId = R.string.category_is_required
            } else if (date.isEmpty()) {
                errorId = R.string.due_date_is_required
            }

            // if error isn not equal to -1, that means a required data is not found or has not been entered by user
            if (errorId != -1) {
                viewModelScope.launch {
                    // sending Ui event to _addEditEvent so we can show eror message to the user
                    _addEditUiEvent.emit(AddEditUIEvent.Error(errorId))
                }
            } else {
                // create the task object, and if it is updated, the update the task else add a new task
                try {
                    val task = Tasks(
                        id = id,
                        title = title,
                        description = description,
                        category = category.toString(),
                        date = date,
                        priority = priority.toString(),
                        isCompleted = isCompleted,
                    )
                    if (isEditTask) {
                        dataStoreHandler.updateTask(task)
                    } else {
                        dataStoreHandler.addTask(task)
                    }
                    // after the task is completed ,start the notification workManger to shownotification
                    notificationScheduler.scheduleNotificationWork(task)

                    // if their is n error, update the UI with success
                    _addEditUiEvent.emit(AddEditUIEvent.Success)

                } catch (ex: Exception) {
//                    if exception is thrown, send the Error eevent
                    ex.printStackTrace()
                    _addEditUiEvent.emit(AddEditUIEvent.Error(R.string.unable_to_save_task))
                }
            }
        }
    }

}
