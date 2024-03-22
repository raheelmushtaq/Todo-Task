package com.app.todolist.presentation.screens.add_edit_task.state_event

import com.app.todolist.presentation.utils.filters.TaskPriority
data class AddEditDataState(
    val title: String = "",
    val description: String = "",
    val taskPriority: TaskPriority? = null,
    val category: String? = null,
    val date: String = "",
    val isCompleted: Boolean = false
)