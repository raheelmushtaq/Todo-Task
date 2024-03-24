package com.app.todolist.presentation.screens.add_edit_task.state_event

import com.app.todolist.presentation.utils.filters.TaskPriority

/*AddEditActionEvent is used to as between AddEventTaskScreen and AddEditVieModel
* when user perform an action on the Screen, an Event(AddEditAction) is pass to viewmodel to perform some actions
* this was the communication between compose and screen is being done*/
sealed class AddEditActionEvent {
    // when user is entering Title in Title field then this action is sent to Viewmodel
    // this takes a String as a parameter that need to be saved
    data class EnterTitle(val text: String) : AddEditActionEvent()

    // when user is entering the description in the description field, then this event is sent to viewmodel
    // this takes a String as a parameter that need to be saved
    data class EnterDescription(val text: String) : AddEditActionEvent()

    // when user is selects the Priority of the task, then this event is sent to viewmodel
    // this takes a priority of task as a parameter that need to be saved
    data class EnterPriority(val taskPriority: TaskPriority) : AddEditActionEvent()

    // when user is selects the category of the task, then this event is sent to viewmodel
    // this takes a String as a parameter that need to be saved
    data class EnterCategory(val value: String) : AddEditActionEvent()

    // when user is selects the the due date from the calender, then this event is sent to viewmodel
    // this takes a String as a parameter that need to be saved
    data class SelectDueDate(val date: String) : AddEditActionEvent()

    // when user has entered every thing or not, but presses the save button then this event is sent to viewmodel to perform the saveTask functionality
    // this does not take any parameter
    data object SaveTask : AddEditActionEvent()
}