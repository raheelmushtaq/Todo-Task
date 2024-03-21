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
