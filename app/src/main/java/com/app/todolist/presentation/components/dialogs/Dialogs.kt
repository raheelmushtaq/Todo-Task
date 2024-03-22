package com.app.todolist.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.todolist.R
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText

@Composable
fun ConfirmationDialog(
    dialogTitle: String,
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = {
            LargeText(text = dialogTitle)
        },
        text = {
            MediumText(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                MediumText(text =stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                MediumText(text = stringResource(R.string.cancel))
            }
        }
    )
}