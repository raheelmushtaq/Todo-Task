package com.app.todolist.datastore

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.OrderBy
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.data.models.TodoTask
import com.app.todolist.presentation.screens.settings.modal.Languages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val dataStoreHandler: DataStoreHandler) :
    ViewModel() {

    private val _appSettings = mutableStateOf(AppSettings())
    val appSettings: MutableState<AppSettings> = _appSettings

    init {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().onEach {
                _appSettings.value = it
            }.launchIn(viewModelScope)
        }
    }


    fun changeLanguage(language: Languages) {
        viewModelScope.launch {
            dataStoreHandler.changeLanguage(language)
        }
    }

    fun saveTask(tasks: List<TodoTask>) {
        viewModelScope.launch {
            dataStoreHandler.saveTask(tasks)
        }
    }

    fun updateTask(task: TodoTask) {
        viewModelScope.launch {
            dataStoreHandler.updateTask(task)
        }
    }

    fun addTask(task: TodoTask, isUpdate: Boolean) {
        viewModelScope.launch {
            if (isUpdate) {
                updateTask(task)
            } else {
                dataStoreHandler.addTask(task)
            }
        }

    }

    fun deleteTasks(task: TodoTask) {
        viewModelScope.launch {
            dataStoreHandler.deleteTasks(task)
        }
    }

    fun getTasks(searchText: String, todoFilters: TodoFilters): List<TodoTask> {
        var tasks = _appSettings.value.todoTasks.toList().filter { task ->
            task.title.lowercase().contains(searchText) || task.description.lowercase()
                .contains(searchText)
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
        return tasks

    }

    suspend fun getCategories(): List<String> {
        return _appSettings.value.categories.toList()
    }

    suspend fun saveCategories(categories: List<String>) {

        viewModelScope.launch {
            dataStoreHandler.saveCategories(categories)
        }
    }
    suspend fun getTaskById(taskId: Int): TodoTask? {
        val filterTasks = appSettings.value.todoTasks.toList().filter { task -> task.id == taskId }
        return if (filterTasks.size > 0) {
            filterTasks[0]
        } else null
    }

    fun getCurrentRecordCount(): Int {
        return _appSettings.value.recordCount
    }

    fun getLanguage(): Languages {
        return _appSettings.value.appLanguage
    }
}

sealed class DataStoreEvent {
    data class ApplyFilter(val todoFilters: TodoFilters = TodoFilters()) : DataStoreEvent()
    data class Search(val searchText: String) : DataStoreEvent()
    data class Delete(val task: TodoTask) : DataStoreEvent()
    data class MarkAsComplete(val task: TodoTask) : DataStoreEvent()
}

data class DataStoreData(
    val appLanguage: Languages = Languages.English,
    val isDataFetched: Boolean = false,
    val recordCount: Int = 0,
    val todoTasks: List<TodoTask> = listOf(),
    val categories: List<String> = listOf()
)