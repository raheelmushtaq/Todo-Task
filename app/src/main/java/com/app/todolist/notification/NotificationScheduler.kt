package com.app.todolist.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.app.todolist.data.models.TodoTask
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {
    fun scheduleNotificationWork(todoTask: TodoTask) {
        scheduleNotificationWork(context, todoTask)
    }


    fun calculateDelay(date: String): Long {
        // Get the current time


        val splitDate = date.split("-")
        val dayOfMonth: Int = splitDate[0].toInt()
        val month: Int = splitDate[1].toInt()
        val year: Int = splitDate[2].toInt()

        val currentTimeMillis = System.currentTimeMillis()

        // Create a Calendar instance for the desired time
        val calendar = Calendar.getInstance().apply {
            set(
                year, month - 1, dayOfMonth, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE) + 5
            ) // Month in Calendar starts from 0
        }

        // Calculate the delay until the desired time
        val desiredTimeMillis = calendar.timeInMillis
        return desiredTimeMillis - currentTimeMillis
    }


    fun scheduleNotificationWork(context: Context, todoTask: TodoTask) {
        val time = calculateDelay(todoTask.date)
        if (time > 0) {
            val notificationWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInputData(workDataOf("content" to todoTask.title))
                .setInitialDelay(time, TimeUnit.MILLISECONDS).build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "notification_${todoTask.id}", ExistingWorkPolicy.REPLACE, notificationWorkRequest
            )
        }
    }
}