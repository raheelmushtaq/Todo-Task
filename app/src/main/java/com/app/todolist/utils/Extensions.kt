package com.app.todolist.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.getDate(): Date? {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(this)
}