package com.app.todolist.presentation.screens

import com.app.todolist.datastore.DataStoreHandlerInterface
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataSourceHandler : DataStoreHandlerInterface {

    private var appSettings = AppSettings()
    override fun getAppSettings(): Flow<AppSettings> = flowOf(appSettings)

    override suspend fun addTasks(tasks: List<Tasks>) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            list.clear()
            list.addAll(tasks)
        }, recordCount = tasks.size)
    }

    override suspend fun updateTask(task: Tasks) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            val index = list.indexOfFirst { item -> item.id == task.id }
            if (index != -1) list[index] = task
            list
        })
    }

    override suspend fun addTask(task: Tasks) {
        appSettings = appSettings.copy(
            tasks = appSettings.tasks.mutate { list ->
                list.add(task)
            }, recordCount = appSettings.recordCount + 1
        )
    }

    override suspend fun deleteTasks(task: Tasks) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            val index = list.indexOfFirst { item -> item.id == task.id }
            if (index != -1) list.removeAt(index)
            list
        })
    }

    override suspend fun saveCategories(categories: List<String>) {
        appSettings = appSettings.copy(categories = appSettings.categories.mutate { list ->
            list.clear()
            list.addAll(categories)
        })
    }


}