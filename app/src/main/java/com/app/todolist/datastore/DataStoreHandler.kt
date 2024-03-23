package com.app.todolist.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.mutate
import javax.inject.Inject

val Context.dataStore: DataStore<AppSettings> by dataStore(
    fileName = "task_datastore.json", serializer = AppSettingsSerializer()
)

class DataStoreHandler @Inject constructor(private val context: Context) :
    DataStoreHandlerInterface {
    override fun getAppSettings() = context.dataStore.data

    override suspend fun addTasks(tasks: List<Tasks>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                list.clear()
                list.addAll(tasks)
            }, recordCount = tasks.size)
        }
    }

    override suspend fun updateTask(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list[index] = task
            })
        }
    }

    override suspend fun addTask(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(
                tasks = appSettings.tasks.mutate { list ->
                    list.add(task)
                }, recordCount = appSettings.recordCount + 1
            )
        }
    }

    override suspend fun deleteTasks(task: Tasks) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list.removeAt(index)
            })
        }
    }

    override suspend fun saveCategories(categories: List<String>) {
        context.dataStore.updateData { appSettings ->
            appSettings.copy(categories = appSettings.categories.mutate { list ->
                list.clear()
                list.addAll(categories)
            })
        }
    }
}