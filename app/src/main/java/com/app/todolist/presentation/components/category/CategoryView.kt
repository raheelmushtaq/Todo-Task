package com.app.todolist.presentation.components.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.todolist.presentation.components.textfields.MediumText

/*
* CategoryView composable is used to show the categories. it is also used to show a single value
* this composable takes following parameter
* text to be shown for category
* isClickable, if it is set to true, then the category view is clickable and if it is false, them category view is not clickable
* onClick the callback function, when user presses some tasks, then this function is called.*/
@Composable
fun CategoryView(
    text: String,
    isClickable: Boolean = true,
    showIcon: Boolean = false,
    onClick: () -> Unit = {}
) {
    // show view horizontally
    Row(modifier = Modifier
        .background(Color.White)
        .border(width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp))
        .clickable {
            if (isClickable) {
                onClick.invoke()
            }
        }
        .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        //if show icon is true then hte tick icon.
        if (showIcon) {
            //icon view
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))

        }
        //show category title
        MediumText(text = text)
    }
}