package com.app.todolist.presentation.screens.add_edit_task.state_event

import com.app.todolist.presentation.utils.filters.TaskPriority

/*
* AddEditDataState is used for saving data of the AddEditTaskScreen.
* when user enters the title, an action is sent to view model with the title, this title is saved in this class.
* */
data class AddEditDataState(
    val title: String = "",
    val description: String = "",
    val taskPriority: TaskPriority? = null,
    val category: String? = null,
    val date: String = "",
    val isCompleted: Boolean = false,
)