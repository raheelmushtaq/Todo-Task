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

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val dataStoreHandler: DataStoreHandlerInterface,
    private val notificationScheduler: NotificationSchedulerInterface,
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    private var _dataState = mutableStateOf(AddEditDataState())
    val dataState: State<AddEditDataState> = _dataState

    private val _addEditUiEvent = MutableSharedFlow<AddEditUIEvent>()
    val addEditUiEvent = _addEditUiEvent.asSharedFlow()

    private val _appSettings = mutableStateOf(AppSettings())
    val appSettings: MutableState<AppSettings> = _appSettings

    private var currentTaskId: Int = -1


    init {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
            }.launchIn(viewModelScope)
        }

        savedStateHandle.get<Int>(TASK_ID)?.let { taskId ->
            if (taskId != -1) {
                val task = getTaskById(taskId)
                updateTaskData(task)
            }
        }
    }

    public fun getTaskById(taskId: Int): Tasks? {
        val filterTasks = _appSettings.value.tasks.toList().filter { task -> task.id == taskId }
        return if (filterTasks.isNotEmpty()) {
            filterTasks[0]
        } else null
    }

    private fun updateTaskData(task: Tasks?) {
        if (task != null) {
            currentTaskId = task.id


            val priority = arrayListOf(
                TaskPriority.Low, TaskPriority.Medium, TaskPriority.High
            ).filter {
                it.value.equals(
                    task.priority.ifEmpty { TaskPriority.Low.value }, true
                )
            }[0]
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

    fun onEvent(event: AddEditActionEvent) {
        when (event) {
            is AddEditActionEvent.EnterTitle -> {
                _dataState.value = dataState.value.copy(title = event.text)
            }

            is AddEditActionEvent.EnterDescription -> {
                _dataState.value = dataState.value.copy(description = event.text)
            }

            is AddEditActionEvent.EnterPriority -> {
                _dataState.value = dataState.value.copy(taskPriority = event.taskPriority)
            }

            is AddEditActionEvent.EnterCategory -> {
                _dataState.value = dataState.value.copy(category = event.value)
            }

            is AddEditActionEvent.SelectDueDate -> {
                _dataState.value = dataState.value.copy(date = event.date)
            }

            is AddEditActionEvent.SaveNote -> {

                val isEditTask = currentTaskId != -1
                val id = if (isEditTask) currentTaskId else appSettings.value.recordCount + 1

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

            if (errorId != -1) {
                viewModelScope.launch {
                    _addEditUiEvent.emit(AddEditUIEvent.Error(errorId))
                }
            } else {
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
                    notificationScheduler.scheduleNotificationWork(task)

                    _addEditUiEvent.emit(AddEditUIEvent.Success)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _addEditUiEvent.emit(AddEditUIEvent.Error(R.string.unable_to_save_task))
                }
            }
        }
    }

}
