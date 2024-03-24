package com.app.todolist.datastore

import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.coroutines.flow.Flow

/*
* This interface is implemented by DataStoreHandler for handling the user Tasks, and also the FakeDataStoreHandler for  unit testing*/
interface DataStoreHandlerInterface {
    fun getAppSettings(): Flow<AppSettings>
    suspend fun addTask(task: Tasks)
    suspend fun updateTask(task: Tasks)
    suspend fun addTasks(tasks: List<Tasks>)
    suspend fun deleteTasks(task: Tasks)
    suspend fun saveCategories(categories: List<String>)
}