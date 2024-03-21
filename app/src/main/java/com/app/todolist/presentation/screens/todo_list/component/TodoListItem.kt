package com.app.todolist.presentation.screens.todo_list.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.todolist.R
import com.app.todolist.data.models.TodoTask
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.components.textfields.RegularText
import kotlinx.coroutines.delay

@Composable
fun TodoListItem(
    modifier: Modifier = Modifier, item: TodoTask, onClick: (TodoTask) -> Unit = {},
    onDelete: (item: TodoTask) -> Unit,
    onMarkAsComplete: (item: TodoTask) -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth(), onClick = { onClick(item) }) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row {
                    MediumText(
                        text = item.title ?: "",
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.isReminderAdded)
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                        )
                }
                Spacer(modifier = Modifier.height(10.dp))
                MediumText(text = item.description ?: "", maxLines = 3)
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        RegularText(
                            text = stringResource(id = R.string.date), fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        MediumText(text = item.date)
                    }


                    Row(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.light_green),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable {
                                if (!item.isCompleted) onMarkAsComplete(item)
                            }, verticalAlignment = Alignment.CenterVertically

                    ) {
                        if (!item.isCompleted) {
                            RegularText(text = stringResource(id = R.string.mark_as_complete))

                        } else {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            MediumText(text = stringResource(id = R.string.completed))
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                    Row(modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.delete),
                            shape = RoundedCornerShape(30)
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable {
                            onDelete(item)
                        }


                    ) {
                        RegularText(
                            text = stringResource(id = R.string.delete),
                            color = Color.White
                        )
                    }
                }
            }
//
//            Icon(
//                imageVector = Icons.Filled.Notifications,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(30.dp)
//                    .align(Alignment.TopCenter)
//            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    onMarkAsComplete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isDismissed by remember { mutableStateOf(false) }

    val swipeToDismissState = rememberSwipeToDismissBoxState(confirmValueChange = { direction ->
        when (direction) {
            SwipeToDismissBoxValue.EndToStart -> { // Swipe Left - Delete
                isDismissed = true
                false
            }

            SwipeToDismissBoxValue.StartToEnd -> { // Swipe Right - Mark Complete
                onMarkAsComplete(item)
                false // Don't dismiss on mark complete
            }

            else -> false
        }
    })

    LaunchedEffect(key1 = isDismissed) {
        if (isDismissed) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isDismissed, exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration), shrinkTowards = Alignment.Top
        )
    ) {
        SwipeToDismissBox(state = swipeToDismissState,
            backgroundContent = { DeleteBackground(swipeDismissState = swipeToDismissState) },
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = true,
            content = { content(item) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val isLeftSwipe = swipeDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
    val isRightSwipe = swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
    val color = if (isLeftSwipe) {
        Color.Green // Adjusted for mark as complete
    } else if (isRightSwipe) {
        Color.Red // Adjusted for delete
    } else {
        Color.Transparent
    }
    val alignment = if (isLeftSwipe) Alignment.CenterStart else Alignment.CenterEnd
    val icon = if (isLeftSwipe) Icons.Default.Check else Icons.Default.Delete
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),

        contentAlignment = alignment
    ) {
        Icon(
            imageVector = icon, contentDescription = null, tint = Color.White
        )
    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun <T> SwipeToDeleteContainer(
//    item: T,
//    onDelete: (T) -> Unit,
//    onMarkAsComplete: (T) -> Unit,
//    animationDuration: Int = 500,
//    content: @Composable (T) -> Unit
//) {
//    var isRemoved by remember { mutableStateOf(false) }
//    var isMarkComplete by remember { mutableStateOf(false) }
//    val state = rememberSwipeToDismissBoxState(
//        confirmValueChange = { value ->
//            if (value == SwipeToDismissBoxValue.StartToEnd) { // Left Swipe - Mark Complete
//                isMarkComplete = true
//                true
//            } else if (value == SwipeToDismissBoxValue.EndToStart) { // Right Swipe - Delete
//                isRemoved = true
//                true
//            } else {
//                false
//            }
//        }
//    )
//
//    LaunchedEffect(key1 = isRemoved) {
//        if (isRemoved) {
//            delay(animationDuration.toLong())
//            onDelete(item)
//        }
//    }
//
//    LaunchedEffect(key1 = isMarkComplete) {
//        if (isMarkComplete) {
//            delay(animationDuration.toLong())
//            onMarkAsComplete(item)
//            isMarkComplete = false // Reset after marking complete
//        }
//    }
//
//    AnimatedVisibility(
//        visible = !isRemoved,
//        exit = shrinkVertically(
//            animationSpec = tween(durationMillis = animationDuration),
//            shrinkTowards = Alignment.Top
//        ) + fadeOut()
//    ) {
//        SwipeToDismissBox(
//            state = state,
//            backgroundContent = {
//                DeleteBackground(swipeDismissState = state)
//            },
//            enableDismissFromEndToStart = true,
//            enableDismissFromStartToEnd = true,
//            content = { content(item) },
//        )
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DeleteBackground(
//    swipeDismissState: SwipeToDismissBoxState
//) {
//    val isLeftSwipe = swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
//    val isRightSwie = swipeDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
//    val color =
//        if (isLeftSwipe) {
//            Color.Red
//        } else if (isRightSwie) {
//            Color.Green
//        } else {
//            Color.Transparent
//        }
//    val alignment = if (isLeftSwipe) Alignment.CenterEnd else Alignment.CenterStart
//    val icon = if (isLeftSwipe) Icons.Default.Delete else Icons.Default.Check
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color)
//            .padding(16.dp),
//        contentAlignment = alignment
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = Color.White
//        )
//    }
//}

@Preview
@Composable
fun PreviewMedicineListItem() {


}