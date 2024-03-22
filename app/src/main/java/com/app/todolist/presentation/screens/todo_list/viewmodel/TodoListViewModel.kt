package com.app.todolist.presentation.screens.todo_list.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.OrderBy
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.data.models.TodoTask
import com.app.todolist.datastore.DataStoreHandler
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
                getTasks(dataState.value.searchText, dataState.value.todoFilters)
            }.launchIn(viewModelScope)
        }
    }

    private fun getTasks(searchText: String = "", todoFilters: TodoFilters = TodoFilters()) {
        viewModelScope.launch {
            var tasks =
                if (searchText.isEmpty()) {
                    _appSettings.value.todoTasks.toList()
                } else {
                    _appSettings.value.todoTasks.toList().filter { task ->
                        task.title.lowercase().contains(searchText) || task.description.lowercase()
                            .contains(searchText)
                    }
                }
            if (todoFilters.tasksPriority != null) {
                tasks = tasks.filter { task ->
                    task.priority.equals(
                        todoFilters.tasksPriority.value, true
                    )
                }
            }
            if (todoFilters.category != null) {
                tasks = tasks.filter { task -> task.category.equals(todoFilters.category, true) }
            }

            if (todoFilters.orderBy != null) {
                tasks = when (todoFilters.orderBy) {
                    is OrderBy.Title -> tasks.sortedByDescending { it.title.lowercase() }
                    is OrderBy.Date -> tasks.sortedByDescending { it.date }
                    is OrderBy.Completed -> tasks.sortedByDescending { it.isCompleted }
                }
            }
            _dataState.value =
                dataState.value.copy(
                    searchText = searchText,
                    todoFilters = todoFilters,
                    showFilterDialog = false
                )
            _dataStoreLiveState.value =
                _appSettings.value.copy(todoTasks = tasks.toPersistentList())
        }
    }

    fun onEvent(event: TodoListUIEvent) {
        when (event) {
            is TodoListUIEvent.ApplyFilter -> {
                getTasks(dataState.value.searchText, event.todoFilters)
            }

            is TodoListUIEvent.Search -> {
                _dataState.value = dataState.value.copy(searchText = event.searchText)
                DebounceSearchUtils.setSearchTextChange {
                    getTasks(event.searchText, dataState.value.todoFilters)
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
    val todoFilters: TodoFilters = TodoFilters(),
)


sealed class TodoListUIEvent {
    data class ApplyFilter(val todoFilters: TodoFilters = TodoFilters()) : TodoListUIEvent()
    data class Search(val searchText: String) : TodoListUIEvent()
    data class Delete(val task: TodoTask) : TodoListUIEvent()
    data class MarkAsComplete(val task: TodoTask) : TodoListUIEvent()
    data object ShowFilter : TodoListUIEvent()
    data object HideFilter : TodoListUIEvent()
}