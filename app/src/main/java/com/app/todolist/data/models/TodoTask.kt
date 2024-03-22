package com.app.todolist.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TodoTask(
    var id: Int,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val priority: String = "",
    val isCompleted: Boolean = false,
)