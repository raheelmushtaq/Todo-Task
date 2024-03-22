package com.app.todolist.presentation.utils.filters

import androidx.annotation.StringRes
import com.app.todolist.R

sealed class SortBy(@StringRes val resId: Int) {
    data object Ascending : SortBy(R.string.ascending)
    data object Descending : SortBy(R.string.descending)
}