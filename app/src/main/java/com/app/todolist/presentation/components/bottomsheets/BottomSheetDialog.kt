package com.app.todolist.presentation.components.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    dialogState: Boolean,
    modifier: Modifier = Modifier,
    heading: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (dialogState) {

        ModalBottomSheet(
            modifier = Modifier.height(500.dp),
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