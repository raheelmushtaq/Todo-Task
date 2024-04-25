package com.app.todolist.presentation.screens.task_list.state_event

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.utils.filters.TaskFilters


/*
* TaskListDataState is used for saving data of the TaskListScreen.
* when user enters the search  or applies filter, an action is sent to view model with the search or filters, which are saved in this class
* */
data class TaskListDataState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val showFilterDialog: Boolean = false,
    val taskFilters: TaskFilters = TaskFilters(),
    val tasks: List<Tasks> = arrayListOf(),
    val categories: List<String> = arrayListOf()
)