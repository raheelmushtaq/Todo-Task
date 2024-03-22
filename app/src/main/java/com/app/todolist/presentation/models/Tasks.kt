package com.app.todolist.presentation.models

import kotlinx.serialization.Serializable

@Serializable
data class Tasks(
    var id: Int,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val priority: String = "",
    val isCompleted: Boolean = false,
)