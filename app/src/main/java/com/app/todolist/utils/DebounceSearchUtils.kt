package com.app.todolist.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* This class is used for handling a debounce search i.e. when user is typing do not perform any thing.
* but when user stops typing, after some type call the api/ perform search locally
 */
object DebounceSearchUtils {

    // it uses coroutine scrope with main dispacher.
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    // job which can be called while user is typeing
    private var searchJob: Job? = null

    //use this function is called every time user is typing.
    // when user stopped the typing, then after 700ms onTextChanged function is called which then perfrorms search
    fun setSearchTextChange(onTextChanged: () -> Unit) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(700)
            onTextChanged.invoke()
        }
    }
}