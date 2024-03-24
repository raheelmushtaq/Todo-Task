package com.app.todolist.presentation.screens.add_edit_task.state_event

import androidx.annotation.StringRes

/*
* AddEditUIEvent class is used for telling UI/Screen that a action has been completed by viewmodel.
* this class is used for such action, Success is sent when a task is added or updated.
* error is sent if user has not added completed information i.e. e.g Description is missing*/
sealed class AddEditUIEvent {
    // for showing success event to user
    data object Success : AddEditUIEvent()
    // for showing error case to user
    data class Error(@StringRes val errorId: Int) : AddEditUIEvent()
}