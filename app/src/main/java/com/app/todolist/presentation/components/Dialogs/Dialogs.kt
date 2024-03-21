package com.app.todolist.presentation.components.Dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.todolist.R
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText

@OptIn(ExperimentalMaterial3Api::class)
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
                MediumText(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                MediumText(stringResource(R.string.cancel))
            }
        }
    )
}