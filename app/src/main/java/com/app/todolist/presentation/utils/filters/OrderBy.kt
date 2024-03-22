package com.app.todolist.presentation.utils.filters

import androidx.annotation.StringRes
import com.app.todolist.R

sealed class OrderBy(val value: String, @StringRes var keyRes: Int) {
    data object Date : OrderBy("date", R.string.date)
    data object Title : OrderBy("title", R.string.title)
    data object Completed : OrderBy("isCompleted", R.string.completed      )
}