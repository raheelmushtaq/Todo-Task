package com.app.todolist.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.mutate

val Context.dataStore: DataStore<AppSettings> by dataStore(
    fileName = "task_datastore.json",
    serializer = AppSettingsSerializer()
)

class DataStoreHandler(private val context: Context) {
    fun getAppSettings() = context.dataStore.data

    suspend fun saveTask(tasks: List<Tasks>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                list.clear()
                list.addAll(tasks)
            }, recordCount = tasks.size)
        }
    }

    suspend fun updateTask(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list[index] = task
            })
        }
    }

    suspend fun addTask(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(
                tasks = appSettings.tasks.mutate { list ->
                    list.add(task)
                },
                recordCount = appSettings.recordCount + 1
            )
        }
    }

    suspend fun deleteTasks(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1)
                    list.removeAt(index)
            })
        }
    }

    suspend fun saveCategories(categories: List<String>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(categories = appSettings.categories.mutate { list ->
                list.clear()
                list.addAll(categories)
            })
        }
    }


}