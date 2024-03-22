package com.app.todolist.presentation.utils.filters

data class TaskFilters(
    val taskPriority: TaskPriority? = null,
    val sortBy: SortBy = SortBy.Descending,
    val orderBy: OrderBy = OrderBy.Date,
    val category: String? = null
)

