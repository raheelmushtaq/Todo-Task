package com.app.todolist.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.todolist.R

/*
* This work manager is used to show notification to user.*/
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // get the content to show to the user and if it not present just show default
        val content = inputData.getString("content")
            ?: applicationContext.getString(R.string.reminder_for_app_delay)

        //show notification function is called.
        showNotification(content)
        return Result.success()
    }

    /*
    * this function is used to show notification to user
    */
    private fun showNotification(content: String) {
//        create a channel it
        val channelId = applicationContext.getString(R.string.task_reminder_channel)
//        create notificaion id
        val notificationId = System.currentTimeMillis()
//        get nottification Manager form context
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        // create notification channgedl and set it to the notification manager, if OS verison is above oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        //use notificaton builder to create te notification object.
        val notification =
            NotificationCompat.Builder(applicationContext, channelId).setContentTitle(
                applicationContext.getString(
                    R.string.reminder
                )
            )
                .setContentText(content).setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()

        // check if post notification permission is not granted by user then do not show notification
        if (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // show notification to user
        notificationManager.notify(notificationId.toInt(), notification)
    }
}