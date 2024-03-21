package com.app.todolist.data.models

import com.app.todolist.datastore.SerializerPersistentString
import com.app.todolist.datastore.SerializerPersistentTodoTask
import com.app.todolist.presentation.screens.settings.modal.Languages
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val appLanguage: Languages = Languages.English,
    val isDataFetched: Boolean = false,
    val recordCount: Int = 0,

    @Serializable(with = SerializerPersistentTodoTask::class)
    val todoTasks: PersistentList<TodoTask> = persistentListOf(),
    @Serializable(with = SerializerPersistentString::class)
    val categories: PersistentList<String> = persistentListOf()
)