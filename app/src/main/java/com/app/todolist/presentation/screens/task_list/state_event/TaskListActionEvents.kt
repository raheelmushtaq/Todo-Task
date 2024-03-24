package com.app.todolist.presentation.screens.task_list.state_event

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.utils.filters.TaskFilters


/*TaskListActionEvents is used to as between Task-listScreen and TaskListViewModel
* when user perform an action on the Screen, an Event(TaskListActionEvents) is pass to viewmodel to perform some actions
* this was the communication between compose and screen is being done*/
sealed class TaskListActionEvents {
    // when user is applies the filter or clears the filter then ApplyFilter action is sent to Viewmodel
    // this takes a TaskFilter as a using which the filter the tasks
    data class ApplyFilter(val taskFilters: TaskFilters = TaskFilters()) : TaskListActionEvents()

    // when user is entering the text for searching the task in the search field, then this event is sent to viewmodel
    // this takes a String as a parameter that need to be saved  and searched from data settings
    data class Search(val searchText: String) : TaskListActionEvents()

    // when user is presses the delete button in the task item, then this event is sent to viewmodel
    // this takes a Tasks as parameter which disused to delete the task from datastore
    data class Delete(val task: Tasks) : TaskListActionEvents()

    // when user is presses the markAsComplete button in the task item, then this event is sent to viewmodel
    // this takes a Tasks as parameter which disused to markComplete the task from datastore
    data class MarkAsComplete(val task: Tasks) : TaskListActionEvents()

    // when user is filter icon button from the search field then this event is sent to viewmodel
    data object ShowFilter : TaskListActionEvents()

    // when user applies or clears filter or dismiss the filter bottom sheet then this event is sent to viewmodel
    data object HideFilter : TaskListActionEvents()
}