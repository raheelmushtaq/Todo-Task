package com.app.todolist.presentation.utils.filters



/*
* this data class is used for performing Filter. this class holds all the filter values, instead of passing single values,
*  a data class is used to manage the filter data.
* */
data class TaskFilters(
    val taskPriority: TaskPriority? = null,
    val sortBy: SortBy = SortBy.Descending,
    val orderBy: OrderBy = OrderBy.Date,
    val category: String? = null
)

