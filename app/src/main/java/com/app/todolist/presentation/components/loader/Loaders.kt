package com.app.todolist.presentation.components.loader

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Loader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = Color.Black, modifier = modifier
    )
}