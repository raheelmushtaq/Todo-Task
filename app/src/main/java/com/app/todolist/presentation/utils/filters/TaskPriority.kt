package com.app.todolist.presentation.utils.filters

import androidx.annotation.StringRes
import com.app.todolist.R


sealed class TaskPriority(val value: String, @StringRes val resId: Int) {
    data object Low : TaskPriority("Low", R.string.low)
    data object Medium : TaskPriority("Medium", R.string.medium)
    data object High : TaskPriority("High", R.string.high)
}