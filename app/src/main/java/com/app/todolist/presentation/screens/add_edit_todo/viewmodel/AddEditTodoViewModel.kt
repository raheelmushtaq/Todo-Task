package com.app.todolist.presentation.screens.add_edit_todo.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.TasksPriority
import com.app.todolist.data.models.TodoTask
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.notification.NotificationScheduler
import com.app.todolist.presentation.screens_routes.ScreenParams.TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val dataStoreHandler: DataStoreHandler,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle

) :
    ViewModel() {

    private var _dataState = mutableStateOf<AddEditDataState>(AddEditDataState())
    val dataState: State<AddEditDataState> = _dataState

    private val _addEditUiEvent = MutableSharedFlow<AddEditUIEvent>()
    val addEditUiEvent = _addEditUiEvent.asSharedFlow()

    private val _appSettings = mutableStateOf(AppSettings())
    val appSettings: MutableState<AppSettings> = _appSettings

    var currentTaskId: Int = -1


    init {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
            }.launchIn(viewModelScope)
        }

        savedStateHandle.get<Int>(TASK_ID)?.let { taskId ->
            if (taskId.toInt() != -1) {
                getTaskById(taskId)
            }
        }
    }

    private fun getTaskById(taskId: Int) {
        viewModelScope.launch {
            val filterTasks =
                _appSettings.value.todoTasks.toList().filter { task -> task.id == taskId }
            val task = if (filterTasks.isNotEmpty()) {
                filterTasks[0]
            } else null
            if (task != null) {
                currentTaskId = task.id


                val priority = arrayListOf(
                    TasksPriority.Low,
                    TasksPriority.Medium,
                    TasksPriority.High
                ).filter {
                    it.value.equals(
                        task.priority.ifEmpty { TasksPriority.Low.value },
                        true
                    )
                }[0]
                _dataState.value = _dataState.value.copy(
                    title = task.title,
                    titleError = -1,
                    description = task.description,
                    descriptionError = -1,
                    tasksPriority = priority,
                    priorityError = -1,
                    category = task.category,
                    categoryError = -1,
                    isCompleted = task.isCompleted,
                    date = task.date,
                    dateError = -1,
                )
            }
        }
    }


    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.EnterTitle -> {
                _dataState.value = dataState.value.copy(title = event.text, titleError = -1)
            }

            is AddEditEvent.EnterDescription -> {
                _dataState.value =
                    dataState.value.copy(description = event.text, descriptionError = -1)
            }

            is AddEditEvent.EnterPriority -> {
                _dataState.value =
                    dataState.value.copy(tasksPriority = event.tasksPriority, priorityError = -1)
            }

            is AddEditEvent.EnterCategory -> {
                _dataState.value = dataState.value.copy(category = event.value, categoryError = -1)
            }

            is AddEditEvent.SelectDueDate -> {
                _dataState.value = dataState.value.copy(date = event.date, dateError = -1)
            }

            is AddEditEvent.SaveNote -> {
                validateNote()
            }
        }
    }

    private fun validateNote() {
        var isError = false
        var titleError = -1
        var descriptionError = -1
        var priorityError = -1
        var categoryEror = -1
        var dueDateError = -1
        if (dataState.value.title.isEmpty()) {
            isError = true
            titleError = R.string.title_is_required
        }
        if (dataState.value.description.isEmpty()) {
            isError = true
            descriptionError = R.string.description_is_required
        }
        if (dataState.value.tasksPriority == null) {
            isError = true
            priorityError = R.string.priority_is_required
        }
        if (dataState.value.category == null) {
            isError = true
            categoryEror = R.string.category_is_required
        }
        if (dataState.value.date.isEmpty()) {
            isError = true
            dueDateError = R.string.due_date_is_required
        }

        if (isError) {
            _dataState.value = dataState.value.copy(
                titleError = titleError,
                priorityError = priorityError,
                descriptionError = descriptionError,
                categoryError = categoryEror,
                dateError = dueDateError
            )
        } else {
            saveNote()
        }
    }

    private fun saveNote() {
        viewModelScope.launch {

            try {

                val isEditTask = currentTaskId != -1
                val id = if (isEditTask) currentTaskId else appSettings.value.recordCount + 1
                val task = TodoTask(
                    id = id,
                    title = dataState.value.title,
                    description = dataState.value.description,
                    category = dataState.value.category.toString(),
                    date = dataState.value.date,
                    priority = dataState.value.tasksPriority?.value
                        ?: TasksPriority.Low.value,
                    isCompleted = false,
                )
                if (isEditTask) {
                    dataStoreHandler.updateTask(task)
                } else {
                    dataStoreHandler.addTask(task)
                }
                notificationScheduler.scheduleNotificationWork(task)

                _addEditUiEvent.emit(AddEditUIEvent.Success)

            } catch (ex: Exception) {
                ex.printStackTrace()
                _addEditUiEvent.emit(AddEditUIEvent.Error)
            }
        }
    }


}

sealed class AddEditUIEvent() {
    object Success : AddEditUIEvent()
    object Error : AddEditUIEvent()
}

sealed class AddEditEvent {
    data class EnterTitle(val text: String) : AddEditEvent()
    data class EnterDescription(val text: String) : AddEditEvent()
    data class EnterPriority(val tasksPriority: TasksPriority) : AddEditEvent()
    data class EnterCategory(val value: String) : AddEditEvent()
    data class SelectDueDate(val date: String) : AddEditEvent()
    data object SaveNote : AddEditEvent()
}

data class AddEditDataState(
    val title: String = "",
    val titleError: Int = -1,
    val description: String = "",
    val descriptionError: Int = -1,
    val tasksPriority: TasksPriority? = null,
    val priorityError: Int = -1,
    val category: String? = null,
    val categoryError: Int = -1,
    val date: String = "",
    val dateError: Int = -1,
    val isCompleted: Boolean = false
)