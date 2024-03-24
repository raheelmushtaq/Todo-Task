package com.app.todolist.datastore.model

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.datastore.SerializerPersistentString
import com.app.todolist.datastore.SerializerPersistentTask
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

/**
 * AppSettings is the class which is used with data store.
 * DataStore uses this class to save the data to save the list of tasks, categories and record count
 ***/
@Serializable
data class AppSettings(
    /* record count variable as a unique id for every record.
    when ever a new record is added, we add 1 to the record count and use it as a new id.
    */
    val recordCount: Int = 0,
    /*
    Persistent list is here to save the data in the list and it needs it own serializer
    reference https://medium.com/androiddevelopers/datastore-and-kotlin-serialization-8b25bf0be66c
    To Serialize Persistent Tasks
    reference https://programmerofpersia.medium.com/how-to-serialize-deserialize-a-persistentlist-persistentmap-with-kotlinx-serialization-72a11a226e56
    this fields holds the value of the all the task in the datastore
     */
    @Serializable(with = SerializerPersistentTask::class)
    val tasks: PersistentList<Tasks> = persistentListOf(),
    // this fields holds the the categories
    @Serializable(with = SerializerPersistentString::class)
    val categories: PersistentList<String> = persistentListOf()
)