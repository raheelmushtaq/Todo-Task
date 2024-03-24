package com.app.todolist.datastore

import androidx.datastore.core.Serializer
import com.app.todolist.datastore.model.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/*
* AppSettingsSerializer is used to save data in file and read from file
* reference: https://www.youtube.com/watch?v=yMGAbm84iIY&t=975s&ab_channel=PhilippLackner
*
* default value for app settings is assigned in the constructor
* */
class AppSettingsSerializer(override val defaultValue: AppSettings = AppSettings()) :
    Serializer<AppSettings> {

    /*
    * this functions read fom file and converts the input String into AppSettings
    and if their is any exception generated while reading from file, then default value is returned
     */
    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(), string = input.readBytes().decodeToString()
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            defaultValue
        }
    }

    /*
    * this function writes the AppSettings in to a files
    * */
    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(serializer = AppSettings.serializer(), t).encodeToByteArray()
            )
        }
    }
}