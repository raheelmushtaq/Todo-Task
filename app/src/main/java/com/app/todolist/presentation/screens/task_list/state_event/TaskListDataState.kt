package com.app.todolist.presentation.screens.task_list.state_event

import com.app.todolist.presentation.utils.filters.TaskFilters


data class TaskListDataState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val showFilterDialog: Boolean = false,
    val taskFilters: TaskFilters = TaskFilters(),
)