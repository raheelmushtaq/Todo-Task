package com.app.todolist.presentation.screens.add_edit_task.state_event

import androidx.annotation.StringRes

sealed class AddEditUIEvent() {
    object Success : AddEditUIEvent()
    data class Error(@StringRes val errorId: Int) : AddEditUIEvent()
}