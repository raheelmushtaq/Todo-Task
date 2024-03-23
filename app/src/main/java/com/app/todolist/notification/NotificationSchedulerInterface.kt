package com.app.todolist.notification

import com.app.todolist.presentation.models.Tasks

interface NotificationSchedulerInterface {

    fun scheduleNotificationWork(tasks: Tasks)
}