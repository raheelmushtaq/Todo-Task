package com.app.todolist.presentation.screens.task_list.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.utils.filters.OrderBy
import com.app.todolist.presentation.utils.filters.TaskFilters
import com.app.todolist.presentation.models.Tasks
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.presentation.utils.filters.SortBy
import com.app.todolist.utils.DebounceSearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val dataStoreHandler: DataStoreHandler) :
    ViewModel() {
    private val _appSettings = mutableStateOf(AppSettings())

    private val _dataState = mutableStateOf(TodoUIState())
    val dataState: State<TodoUIState> = _dataState

    private val _dataStoreLiveState = mutableStateOf(AppSettings())
    val dataStoreLiveState: MutableState<AppSettings> = _dataStoreLiveState

    init {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
                _dataStoreLiveState.value = it
                getTasks(dataState.value.searchText, dataState.value.taskFilters)
            }.launchIn(viewModelScope)
        }
    }

    private fun getTasks(searchText: String = "", taskFilters: TaskFilters = TaskFilters()) {
        viewModelScope.launch {
            var tasks =
                if (searchText.isEmpty()) {
                    _appSettings.value.tasks.toList()
                } else {
                    _appSettings.value.tasks.toList().filter { task ->
                        task.title.lowercase().contains(searchText) || task.description.lowercase()
                            .contains(searchText)
                    }
                }
            if (taskFilters.taskPriority != null) {
                tasks = tasks.filter { task ->
                    task.priority.equals(
                        taskFilters.taskPriority.value, true
                    )
                }
            }
            if (taskFilters.category != null) {
                tasks = tasks.filter { task -> task.category.equals(taskFilters.category, true) }
            }

            tasks = when (taskFilters.sortBy) {
                is SortBy.Ascending -> {
                    when (taskFilters.orderBy) {
                        is OrderBy.Title -> tasks.sortedBy { it.title.lowercase() }
                        is OrderBy.Date -> tasks.sortedBy { it.date }
                        is OrderBy.Completed -> tasks.sortedBy { it.isCompleted }
                    }
                }

                is SortBy.Descending -> {
                    when (taskFilters.orderBy) {
                        is OrderBy.Title -> tasks.sortedByDescending { it.title.lowercase() }
                        is OrderBy.Date -> tasks.sortedByDescending { it.date }
                        is OrderBy.Completed -> tasks.sortedByDescending { it.isCompleted }
                    }
                }
            }
            _dataState.value =
                dataState.value.copy(
                    searchText = searchText,
                    taskFilters = taskFilters,
                    showFilterDialog = false
                )
            _dataStoreLiveState.value =
                _appSettings.value.copy(tasks = tasks.toPersistentList())
        }
    }

    fun onEvent(event: TodoListUIEvent) {
        when (event) {
            is TodoListUIEvent.ApplyFilter -> {
                getTasks(dataState.value.searchText, event.taskFilters)
            }

            is TodoListUIEvent.Search -> {
                _dataState.value = dataState.value.copy(searchText = event.searchText)
                DebounceSearchUtils.setSearchTextChange {
                    getTasks(event.searchText, dataState.value.taskFilters)
                }
            }

            is TodoListUIEvent.ShowFilter -> {
                _dataState.value = dataState.value.copy(showFilterDialog = true)
            }

            is TodoListUIEvent.HideFilter -> {
                _dataState.value = dataState.value.copy(showFilterDialog = false)
            }

            is TodoListUIEvent.Delete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreHandler.deleteTasks(event.task)
                }
            }

            is TodoListUIEvent.MarkAsComplete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val task = event.task.copy(isCompleted = true)
                    dataStoreHandler.updateTask(task)
                }
            }
        }

    }
}

data class TodoUIState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val showFilterDialog: Boolean = false,
    val taskFilters: TaskFilters = TaskFilters(),
)


sealed class TodoListUIEvent {
    data class ApplyFilter(val taskFilters: TaskFilters = TaskFilters()) : TodoListUIEvent()
    data class Search(val searchText: String) : TodoListUIEvent()
    data class Delete(val task: Tasks) : TodoListUIEvent()
    data class MarkAsComplete(val task: Tasks) : TodoListUIEvent()
    data object ShowFilter : TodoListUIEvent()
    data object HideFilter : TodoListUIEvent()
}