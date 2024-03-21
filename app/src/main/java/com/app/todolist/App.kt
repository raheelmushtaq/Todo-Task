package com.app.todolist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.app.todolist.data.models.AppSettings
import com.app.todolist.datastore.AppSettingsSerializer
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
//    val Context.dataStore: DataStore<AppSettings> by dataStore(
//        "todo_datastore.json",
//        AppSettingsSerializer()
//    )
}