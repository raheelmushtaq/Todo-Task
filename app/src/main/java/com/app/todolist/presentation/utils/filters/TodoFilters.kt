package com.app.todolist.presentation.utils.filters

data class TodoFilters(
    val taskPriority: TaskPriority? = null,
    val orderBy: OrderBy? = null,
    val category: String? = null
)

