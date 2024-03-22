package com.app.todolist.datastore.model

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.datastore.SerializerPersistentString
import com.app.todolist.datastore.SerializerPersistentTask
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val recordCount: Int = 0,
    @Serializable(with = SerializerPersistentTask::class)
    val tasks: PersistentList<Tasks> = persistentListOf(),
    @Serializable(with = SerializerPersistentString::class)
    val categories: PersistentList<String> = persistentListOf()
)