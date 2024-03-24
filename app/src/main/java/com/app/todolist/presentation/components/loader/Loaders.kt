package com.app.todolist.presentation.components.loader

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

//Loader class is used to show loader.
@Composable
fun Loader(modifier: Modifier = Modifier) {
    // CircularProgressIndicator is the loader which is used in the spalch sceem
    CircularProgressIndicator(
        color = Color.Black, modifier = modifier
    )
}