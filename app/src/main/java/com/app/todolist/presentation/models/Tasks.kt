package com.app.todolist.presentation.models

import kotlinx.serialization.Serializable

/*This class is used for Tasks*/
@Serializable
data class Tasks(
    var id: Int,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "24-04-2024",
    val priority: String = "",
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false
)