package com.app.todolist.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Looper

fun addDelay(delay: Long, action: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())

    handler.postDelayed({

        action.invoke()
        handler.removeCallbacksAndMessages(null)
    }, delay)
}

fun ApplicationInfo.getAppName(context: Context) = loadLabel(context.packageManager).toString()