package com.app.todolist.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DebounceSearchUtils {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var searchJob: Job? = null

    fun setSearchTextChange(onTextChanged: () -> Unit) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(700)
            onTextChanged.invoke()
        }
    }
}