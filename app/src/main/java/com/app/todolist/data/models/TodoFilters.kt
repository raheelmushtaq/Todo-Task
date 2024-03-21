package com.app.todolist.data.models

import androidx.annotation.StringRes
import com.app.todolist.R

data class TodoFilters(
    val tasksPriority: TasksPriority? = null,
    val orderBy: OrderBy? = null,
    val category: String? = null
)

sealed class OrderBy(val value: String) {
    data object Date : OrderBy("date")
    data object Title : OrderBy("title")
    data object Completed : OrderBy("isCompleted")
}

sealed class TasksPriority(val value: String, @StringRes val resId: Int) {
    data object Low : TasksPriority("Low", R.string.low)
    data object Medium : TasksPriority("Medium", R.string.medium)
    data object High : TasksPriority("High", R.string.high)
}