package com.app.todolist.presentation.components.bottomsheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.app.todolist.presentation.components.textfields.LargeText

/*
* BottomSheetDialog is the Base composable when we have to show Bottom Sheet dialog.
* this class will the main handling for showing the bottom sheet.
* It take different parameters for handling
* heading to show in the bottom sheet
* dialogState notifying whether the dialog is visible or not.
* onDismiss function, when the dialog is dismissed
* content to show in the bottom sheet/
* Some Modifier for showing handling the UI
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    dialogState: Boolean,
    modifier: Modifier = Modifier,
    heading: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    //rememberModalBottomSheetState notifies the current state of the bottom Sheet.
    val sheetState = rememberModalBottomSheetState(
        // skipPartiallyExpanded if it set to false, then bottom sheet partial screen of the content will be visible.
        skipPartiallyExpanded = true
    )

    // show dialog if the dialog state is true
    if (dialogState) {

        // Modal Bottom sheet Composable to show the bottom sheet
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = { onDismiss() },
            containerColor = Color.White,
            sheetState = sheetState,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                securePolicy = SecureFlagPolicy.SecureOn,
                isFocusable = true
            ),
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 12.dp,
                topEnd = 12.dp
            ),

            windowInsets = WindowInsets.ime,
            dragHandle = {}
        ) {
            // showing the header view for the bottom sheet.
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    LargeText(text = heading)
                    Spacer(modifier = Modifier.height(10.dp))
                    content()
                }
            }
        }
    }
}