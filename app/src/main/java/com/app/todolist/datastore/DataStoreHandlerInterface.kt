package com.app.todolist.datastore

import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.coroutines.flow.Flow

interface DataStoreHandlerInterface {
    fun getAppSettings(): Flow<AppSettings>
    suspend fun addTask(task: Tasks)
    suspend fun updateTask(task: Tasks)
    suspend fun addTasks(tasks: List<Tasks>)
    suspend fun deleteTasks(task: Tasks)
    suspend fun saveCategories(categories: List<String>)
//    suspend fun getTasks(): Flow<List<Tasks>>
//    suspend fun getCategories(): Flow<List<String>>
}