package com.app.todolist.presentation.screens.add_edit_task

import com.app.todolist.notification.NotificationSchedulerInterface
import com.app.todolist.presentation.models.Tasks

class FakeNotificationScheduler : NotificationSchedulerInterface {
    override fun scheduleNotificationWork(tasks: Tasks) {
        // empty function 
    }
}