package com.app.todolist.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.app.todolist.presentation.models.Tasks
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
* This is class us used the scheduling notification.
* The task of this class is the calculate the time for showing the push notification
* This class uses work manager to create 1 time request which is use to run after a certain time of show the notification to user.*/
class NotificationScheduler @Inject constructor(private val context: Context) :
    NotificationSchedulerInterface {

    /*
    * this function is called when user want to create notification*/
    override fun scheduleNotificationWork(tasks: Tasks) {
        // get the time from task date
        val time = calculateDelay(tasks.date)
//        check if the task is of on old date then don't show it to user
        if (time > 0) {
            //create OneTimeWorkRequest to create notification by pass the title of the task to be shown in the notification.
//            it should run after the calculated time
            val notificationWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInputData(workDataOf("content" to tasks.title))
                .setInitialDelay(time, TimeUnit.MILLISECONDS).build()

            // enqueue Unique work with the task id, because if use edits the tasks and the for the precious work a notifications is already scheduled,
            // then cancel it and add new notification
            WorkManager.getInstance(context).enqueueUniqueWork(
                "notification_${tasks.id}", ExistingWorkPolicy.REPLACE, notificationWorkRequest
            )
        }
    }

    /*
    * This function is used to calculate teh remaining time from the current time and desired time to show  */
    private fun calculateDelay(date: String): Long {
        // Get the current date from the time

        val calendar = if (date.isEmpty()) {
            Calendar.getInstance()
        } else {
            val splitDate = date.split("-")
            val dayOfMonth: Int = splitDate[0].toInt()
            val month: Int = splitDate[1].toInt()
            val year: Int = splitDate[2].toInt()


            // Creating a Calendar instance for the desired time
            Calendar.getInstance().apply {
                set(
                    year, month - 1, dayOfMonth, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE) + 5
                )
            }
        }
        // get the current time
        val currentTimeMillis = System.currentTimeMillis()

        // Calculate the delay from the remaining time
        val desiredTimeMillis = calendar.timeInMillis
        return desiredTimeMillis - currentTimeMillis
    }

}