package com.app.todolist.presentation.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.app.todolist.datastore.DataStoreHandlerInterface
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FakeDataSourceHandler : DataStoreHandlerInterface {
    private var appSettings = AppSettings()

    private var _settings = MutableLiveData<AppSettings>()

    override fun getAppSettings(): Flow<AppSettings> = _settings.asFlow()

    override suspend fun addTasks(tasks: List<Tasks>) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            list.clear()
            list.addAll(tasks)
        }, recordCount = tasks.size)
        emitData()
    }

    override suspend fun updateTask(task: Tasks) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            val index = list.indexOfFirst { item -> item.id == task.id }
            if (index != -1) list[index] = task
        })
        emitData()
    }


    override suspend fun addTask(task: Tasks) {
        appSettings = appSettings.copy(
            tasks = appSettings.tasks.mutate { list ->
                list.add(task)
            }, recordCount = appSettings.recordCount + 1
        )
        emitData()
    }

    override suspend fun deleteTasks(task: Tasks) {
        appSettings = appSettings.copy(tasks = appSettings.tasks.mutate { list ->
            val index = list.indexOfFirst { item -> item.id == task.id }
            if (index != -1) list.removeAt(index)
            list
        })
        emitData()

    }

    override suspend fun saveCategories(categories: List<String>) {
        appSettings = appSettings.copy(categories = appSettings.categories.mutate { list ->
            list.clear()
            list.addAll(categories)
        })
        emitData()
    }

    fun getTask(): List<Tasks> {
        return appSettings.tasks.toList()
    }

    fun getCategories(): List<String> {
        return appSettings.categories.toList()
    }

    suspend fun emitData() {
        withContext(Dispatchers.Main) {
            _settings.value = appSettings
        }
    }


}