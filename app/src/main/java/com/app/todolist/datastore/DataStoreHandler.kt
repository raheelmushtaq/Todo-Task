package com.app.todolist.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.app.todolist.data.models.AppSettings
import com.app.todolist.data.models.TodoTask
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<AppSettings> by dataStore(
    fileName = "todo_datastore.json",
    serializer = AppSettingsSerializer()
)

class DataStoreHandler(private val context: Context) {


    fun getAppSettings() = context.dataStore.data

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
//                    task.id = appSettings.recordCount + 1
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
    suspend fun getRecourdCount(): Flow<Int> {
        return context.dataStore.data.map {
            it.recordCount
        }

    }



}