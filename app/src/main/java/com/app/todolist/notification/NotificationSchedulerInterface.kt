package com.app.todolist.notification

import com.app.todolist.presentation.models.Tasks

/*this is the interface created for testing the AddEditViewModel*/
interface NotificationSchedulerInterface {

    fun scheduleNotificationWork(tasks: Tasks)
}