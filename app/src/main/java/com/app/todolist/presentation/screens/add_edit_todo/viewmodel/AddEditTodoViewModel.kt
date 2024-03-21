package com.app.todolist.presentation.screens.add_edit_todo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.data.TodoListRepository
import com.app.todolist.data.models.TasksPriority
import com.app.todolist.data.models.TodoTask
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
    private val repository: TodoListRepository, savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private var _dataState = mutableStateOf<AddEditDataState>(AddEditDataState())
    val dataState: State<AddEditDataState> = _dataState

    private val _eventFlow = MutableSharedFlow<AddEditUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _categories = mutableStateOf<List<String>>(arrayListOf())
    val categories: State<List<String>> = _categories

    var currentTaskId: Int = -1

    init {
        listenToAppSettings()
        savedStateHandle.get<Int>(TASK_ID)?.let { taskId ->
            if (taskId.toInt() != -1) {
                viewModelScope.launch {
                    repository.getTaskById(taskId).also { task ->
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
                                isAddToReminder = task.isReminderAdded,
                                date = task.date,
                                dateError = -1,
                            )
                        }
                    }
                }
            }
        }
    }


    private fun listenToAppSettings() {
        viewModelScope.launch {
            getCategories()
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            repository.getCategories().onEach {
                _categories.value = it
            }.launchIn(viewModelScope)
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

            is AddEditEvent.IsAddToRemember -> {
                _dataState.value = dataState.value.copy(isAddToReminder = event.value)

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
                repository.addTask(
                    TodoTask(
                        id = currentTaskId,
                        title = dataState.value.title,
                        description = dataState.value.description,
                        category = dataState.value.category.toString(),
                        date = dataState.value.date,
                        priority = dataState.value.tasksPriority?.value
                            ?: TasksPriority.Low.value,
                        isCompleted = false,
                        isReminderAdded = dataState.value.isAddToReminder
                    )
                )
                _eventFlow.emit(AddEditUIEvent.Success)

            } catch (ex: Exception) {
                ex.printStackTrace()
                _eventFlow.emit(AddEditUIEvent.Error)
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
    data class IsAddToRemember(val value: Boolean) : AddEditEvent()
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
    val isAddToReminder: Boolean = false,
    val date: String = "",
    val dateError: Int = -1,
)