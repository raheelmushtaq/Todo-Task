package com.app.todolist.presentation.screens.todo_list.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.data.TodoListRepository
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.data.models.TodoTask
import com.app.todolist.utils.DebounceSearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val repository: TodoListRepository) :
    ViewModel() {
    private val _dataState = mutableStateOf(TodoUIState())
    val dataState: State<TodoUIState> = _dataState

    private val _showFilterDialog = mutableStateOf(false)
    val showFilterDialog: State<Boolean> = _showFilterDialog


    init {
        listenToAppSettings()
    }


    private fun listenToAppSettings() {
        viewModelScope.launch {
            repository.getAppSettings().collect {
                getCategories()
                getTasks()
            }
        }
    }

    private fun getTasks(searchText: String = "", todoFilters: TodoFilters = TodoFilters()) {
        viewModelScope.launch {

            repository.getTasks(
                searchText,
                todoFilters
            ).onEach {
                _dataState.value = dataState.value.copy(
                    todoList = it,
                    searchText = searchText,
                    todoFilters = todoFilters
                )
            }.launchIn(viewModelScope)

        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            repository.getCategories().onEach { list ->
                _dataState.value = dataState.value.copy(
                    categories = list
                )
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: TodoListUIEvent) {
        when (event) {
            is TodoListUIEvent.ApplyFilter -> {
                getTasks(dataState.value.searchText, event.todoFilters)
                _showFilterDialog.value = false
            }

            is TodoListUIEvent.Search -> {
                _dataState.value = dataState.value.copy(searchText = event.searchText)
                DebounceSearchUtils.setSearchTextChange(event.searchText) {
                    getTasks(event.searchText, dataState.value.todoFilters)
                }
            }

            is TodoListUIEvent.ShowFilter -> {
                _showFilterDialog.value = true
            }

            is TodoListUIEvent.HideFilter -> {
                _showFilterDialog.value = false
            }

            is TodoListUIEvent.Delete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteTasks(event.task)
                    _dataState.value =
                        dataState.value.copy(todoList = dataState.value.todoList.filter {
                            it.id != event.task.id
                        })
                }
            }

            is TodoListUIEvent.MarkAsComplete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val task = event.task.copy(isCompleted = true)
                    repository.updateTask(task)

                    _dataState.value =
                        dataState.value.copy(todoList = dataState.value.todoList.also {
                            val index = it.indexOfFirst { it.id == event.task.id }
                            if (index != -1) {
                                it.toMutableList()[index] = task
                            }
                        })
                }
            }
        }

    }
}

data class TodoUIState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val todoFilters: TodoFilters = TodoFilters(),
    val categories: List<String> = arrayListOf(),
    val todoList: List<TodoTask> = arrayListOf()
)


sealed class TodoListUIEvent {
    data class ApplyFilter(val todoFilters: TodoFilters = TodoFilters()) : TodoListUIEvent()
    data class Search(val searchText: String) : TodoListUIEvent()
    data class Delete(val task: TodoTask) : TodoListUIEvent()
    data class MarkAsComplete(val task: TodoTask) : TodoListUIEvent()
    data object ShowFilter : TodoListUIEvent()
    data object HideFilter : TodoListUIEvent()
}