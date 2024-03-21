package com.app.todolist.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Looper
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.app.todolist.data.models.TodoTask
import com.app.todolist.notification.NotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun addDelay(delay: Long, action: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())

    handler.postDelayed({

        action.invoke()
        handler.removeCallbacksAndMessages(null)
    }, delay)
}

fun ApplicationInfo.getAppName(context: Context) = loadLabel(context.packageManager).toString()

fun String.calculateDelay(): Long {
    // Get the current time


    val splitDate = this.split("-")
    val dayOfMonth: Int = splitDate[0].toInt()
    val month: Int = splitDate[1].toInt()
    val year: Int = splitDate[2].toInt()

    val currentTimeMillis = System.currentTimeMillis()

    // Create a Calendar instance for the desired time
    val calendar = Calendar.getInstance().apply {
        set(
            year,
            month - 1,
            dayOfMonth,
            get(Calendar.HOUR_OF_DAY),
            get(Calendar.MINUTE) + 5
        ) // Month in Calendar starts from 0
    }

    // Calculate the delay until the desired time
    val desiredTimeMillis = calendar.timeInMillis
    return desiredTimeMillis - currentTimeMillis
}


fun Context.scheduleNotificationWork(todoTask: TodoTask) {
    val notificationWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
        .setInputData(workDataOf("content" to todoTask.title))
        .setInitialDelay(todoTask.date.calculateDelay(), TimeUnit.MILLISECONDS).build()

    WorkManager.getInstance(this).enqueueUniqueWork(
        "notification_${todoTask.id}", ExistingWorkPolicy.REPLACE, notificationWorkRequest
    )
}