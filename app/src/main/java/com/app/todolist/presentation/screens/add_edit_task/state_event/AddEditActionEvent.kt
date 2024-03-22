package com.app.todolist.presentation.screens.add_edit_task.state_event

import com.app.todolist.presentation.utils.filters.TaskPriority

sealed class AddEditActionEvent {
    data class EnterTitle(val text: String) : AddEditActionEvent()
    data class EnterDescription(val text: String) : AddEditActionEvent()
    data class EnterPriority(val taskPriority: TaskPriority) : AddEditActionEvent()
    data class EnterCategory(val value: String) : AddEditActionEvent()
    data class SelectDueDate(val date: String) : AddEditActionEvent()
    data object SaveNote : AddEditActionEvent()
}