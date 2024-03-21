package com.app.todolist.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.asLiveData
import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.data.models.TodoTask
import com.app.todolist.presentation.screens.settings.modal.Languages
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<AppSettings> by dataStore(
    fileName = "todo_datastore.json",
    serializer = AppSettingsSerializer()
)

class DataStoreHandler(private val context: Context) {


    fun getAppSettings() = context.dataStore.data

    suspend fun changeLanguage(language: Languages) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(appLanguage = language)
        }
    }

    suspend fun saveTask(tasks: List<TodoTask>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(todoTasks = appSettings.todoTasks.mutate { list ->
                list.clear();
                list.addAll(tasks)
            }, recordCount = tasks.size)
        }
    }

    suspend fun updateTask(task: TodoTask) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(todoTasks = appSettings.todoTasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list[index] = task
            })
        }
    }

    suspend fun addTask(task: TodoTask) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(
                todoTasks = appSettings.todoTasks.mutate { list ->
                    task.id = appSettings.recordCount + 1
                    list.add(task)
                },
                recordCount = appSettings.recordCount + 1
            )
        }
    }

    suspend fun deleteTasks(task: TodoTask) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(todoTasks = appSettings.todoTasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1)
                    list.removeAt(index)
            })
        }
    }

    fun getTasks(): Flow<List<TodoTask>> =
        context.dataStore.data.map { it.todoTasks.toList() }

    fun getCategories(): Flow<List<String>> =
        context.dataStore.data.map { it.categories.toList() }


    suspend fun saveCategories(categories: List<String>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(categories = appSettings.categories.mutate { list ->
                list.clear();
                list.addAll(categories)
            })
        }
    }

    suspend fun setDataFetched(dataFetched: Boolean) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(isDataFetched = dataFetched)
        }
    }

    suspend fun getIsDataFetched(): Boolean {
        return context.dataStore.data.map {
            it.isDataFetched
        }.first() ?: false
    }


}