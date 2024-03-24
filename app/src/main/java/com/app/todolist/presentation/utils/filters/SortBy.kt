package com.app.todolist.presentation.utils.filters

import androidx.annotation.StringRes
import com.app.todolist.R
/*
* this sealed class is used for performing Filter operation Sort By by selecting this, we are showing tasks in ascending or descending .
* */
sealed class SortBy(@StringRes val resId: Int) {
    data object Ascending : SortBy(R.string.ascending)
    data object Descending : SortBy(R.string.descending)
}