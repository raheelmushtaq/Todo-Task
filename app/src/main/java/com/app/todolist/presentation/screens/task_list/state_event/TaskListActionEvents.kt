package com.app.todolist.presentation.screens.task_list.state_event

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.utils.filters.TaskFilters


sealed class TaskListActionEvents {
    data class ApplyFilter(val taskFilters: TaskFilters = TaskFilters()) : TaskListActionEvents()
    data class Search(val searchText: String) : TaskListActionEvents()
    data class Delete(val task: Tasks) : TaskListActionEvents()
    data class MarkAsComplete(val task: Tasks) : TaskListActionEvents()
    data object ShowFilter : TaskListActionEvents()
    data object HideFilter : TaskListActionEvents()
}