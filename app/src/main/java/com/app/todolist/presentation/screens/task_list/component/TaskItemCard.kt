package com.app.todolist.presentation.screens.task_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.todolist.R
import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.components.textfields.RegularText
import com.app.todolist.presentation.components.textfields.SmallText

@Composable
fun TodoListItem(
    modifier: Modifier = Modifier, item: Tasks, onClick: (Tasks) -> Unit = {},
    onDelete: (item: Tasks) -> Unit,
    onMarkAsComplete: (item: Tasks) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(item) },
        colors = CardDefaults.cardColors(
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row {
                    MediumText(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                MediumText(text = item.description, maxLines = 3)
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
                                color = colorResource(id = R.color.accept),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable {
                                if (!item.isCompleted) onMarkAsComplete(item)
                            }, verticalAlignment = Alignment.CenterVertically

                    ) {
                        if (!item.isCompleted) {
                            RegularText(
                                text = stringResource(id = R.string.mark_as_complete),
                                color = Color.White
                            )

                        } else {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            MediumText(
                                text = stringResource(id = R.string.completed),
                                color = Color.White
                            )
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

        }
    }
}