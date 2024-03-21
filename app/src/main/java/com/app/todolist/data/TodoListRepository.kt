package com.app.todolist.data

import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.OrderBy
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.data.models.TodoTask
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.presentation.screens.settings.modal.Languages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/*
* @HiltViewModel
class DataStoreViewModel @Inject constructor(private val dataStoreHandler: DataStoreHandler) :
    ViewModel() {

    private val _appSettings = MutableSharedFlow<AppSettings>()

    init {
        viewModelScope.launch {
            dataStoreHandler.getAppSettings().collect {
                _appSettings.emit(it)
            }
        }
    }
    * */

class TodoListRepository(private val dataStoreHandler: DataStoreHandler) {

    suspend fun changeLanguage(language: Languages) {
        dataStoreHandler.changeLanguage(language)
    }

    suspend fun saveTask(tasks: List<TodoTask>) {
        dataStoreHandler.saveTask(tasks)
    }

    suspend fun updateTask(task: TodoTask) {
        dataStoreHandler.updateTask(task)
    }

    suspend fun addTask(task: TodoTask, isUpdate: Boolean) {
        if (isUpdate) {
            updateTask(task)
        } else {
            dataStoreHandler.addTask(task)
        }
    }

    suspend fun deleteTasks(task: TodoTask) {
        dataStoreHandler.deleteTasks(task)
    }

    suspend fun getTasks(searchText: String, todoFilters: TodoFilters): Flow<List<TodoTask>> {
        var resultTasks = dataStoreHandler.getTasks().map { todoTasks ->
            var tasks = todoTasks.toList()
                .filter { task ->
                    task.title.lowercase().contains(searchText) || task.description.lowercase()
                        .contains(searchText)
                }
            if (todoFilters.tasksPriority != null) {
                tasks = tasks.filter { task ->
                    task.priority.equals(
                        todoFilters.tasksPriority.value,
                        true
                    )
                }
            }
            if (todoFilters.category != null) {
                tasks =
                    tasks.filter { task -> task.category.equals(todoFilters.category, true) }
            }

            if (todoFilters.orderBy != null) {
                tasks = when (todoFilters.orderBy) {
                    is OrderBy.Title -> tasks.sortedByDescending { it.title.lowercase() }
                    is OrderBy.Date -> tasks.sortedByDescending { it.date }
                    is OrderBy.Completed -> tasks.sortedByDescending { it.isCompleted }
                }
            }
            tasks
        };



        return resultTasks

    }

    suspend fun getCategories(): Flow<List<String>> {
        return dataStoreHandler.getCategories().map { categories ->
            categories
        }
    }


    suspend fun saveCategories(categories: List<String>) {
        dataStoreHandler.saveCategories(categories)
    }

    //
    suspend fun getAppSettings(): Flow<AppSettings> {
        return dataStoreHandler.getAppSettings()
    }

    suspend fun setDataFetched(isDataFetched: Boolean) {
        dataStoreHandler.setDataFetched(isDataFetched)
    }

    suspend fun getIsDataFetched(): Boolean {
        return dataStoreHandler.getIsDataFetched().first()
    }

    suspend fun getTaskById(taskId: Int): TodoTask? {
        val tasks: TodoTask? = dataStoreHandler.getTasks().map { todoTasks ->
            val filterTasks = todoTasks.toList().filter { task -> task.id == taskId }
            if (filterTasks.size > 0) {
                filterTasks[0]
            } else
                null

        }.first()
        return tasks
    }

    suspend fun getCurrentRecordCount(): Int {
        return dataStoreHandler.getRecourdCount().first()
    }

    suspend fun getLanguage(): Languages {
        return dataStoreHandler.getLanguage().first()
    }

}