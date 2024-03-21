package com.app.todolist.network

import com.app.todolist.BuildConfig
import com.app.todolist.data.models.TodoTask
import retrofit2.http.GET

interface ApiClient {

    @GET("d30a3b97-90f2-4f04-8a64-44b42e7ea919")
    suspend fun getListOfMedicinesAndCategories(): ArrayList<TodoTask>

    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }

}