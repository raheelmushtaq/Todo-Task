package com.app.todolist.network

import com.app.todolist.BuildConfig
import com.app.todolist.presentation.models.Tasks
import retrofit2.http.GET

/*
* This class is uses with Di to implement the Api calling for fetching the tasks at the start.*/

interface ApiClient {

    @GET("d30a3b97-90f2-4f04-8a64-44b42e7ea919")
    suspend fun getTasks(): ArrayList<Tasks>

    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }

}