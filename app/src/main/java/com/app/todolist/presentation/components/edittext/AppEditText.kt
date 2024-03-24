package com.app.todolist.presentation.components.edittext

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.app.todolist.R

/*
* AppEditText is composable which is used in the the application.this component is based on InputField.
* this component take different option to show a InputField
*  textFieldValue to be, which is the value we have saved on when user types the text
* hint is used to show the placeholder in case if their is no textFieldValue
* onValueChange callback is used when user has listen the change on text field i.e. when user is typing. t
* read only is to show that this value is only for read and it cannot be editable
* onDone callback is called when user presses the search or done button on the keyboard
* */
@Composable
fun AppEditTextField(
    textFieldValue: String,
    hint: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = true,
    onDone: () -> Unit = {},
) {
//    show outlined TextField for user
    OutlinedTextField(
            value = textFieldValue,
            label = { Text(text = hint) },
            singleLine = true,
            readOnly = readOnly,
            shape = RoundedCornerShape(30),
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
        )
}

/*
* AppDescriptionTextField is composable which is used in the the application.this component is based on InputField.
* this component take different option to show a InputField
*  textFieldValue to be, which is the value we have saved on when user types the text
* hint is used to show the placeholder in case if their is no textFieldValue
* onValueChange callback is used when user has listen the change on text field i.e. when user is typing. t
* read only is to show that this value is only for read and it cannot be editable
* onDone callback is called when user presses the search or done button on the keyboard
* */
@Composable
fun AppDescriptionTextField(
    textFieldValue: String,
    hint: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = true,
    onDone: () -> Unit = {},
) {
    //    show outlined TextField for user with a bigger height
    OutlinedTextField(
            value = textFieldValue,
            label = { Text(text = hint) },
            singleLine = false,
            readOnly = readOnly,
            shape = RoundedCornerShape(10),
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
        )
}
/*
* SearchEditTextField is composable which is used in the the application.this component is based on search bar.
* this component take different option to show a InputField
*  textFieldValue to be, which is the value we have saved on when user types the text
* hint is used to show the placeholder in case if their is no textFieldValue
* onValueChange callback is used when user has listen the change on text field i.e. when user is typing. t
* onDone callback is called-called when user presses the search or done button on the keyboard
* showFilterIcon is used to show filterIcon on the right side if it is true,
* onFilterIconPressed is called when the filter icon is pressed.
* */
@Composable
fun SearchEditTextField(
    textFieldValue: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit = {},
    showFilterIcon: Boolean = false,
    onFilterIconPressed: () -> Unit = {},

    ) {

    OutlinedTextField(value = textFieldValue,
        label = { Text(text = hint) },
        singleLine = true,
        leadingIcon = {
            // leadingIcon is the left icon of the TextField.
            Image(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),

                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.Filter)
            )
        },
        visualTransformation = VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        shape = RoundedCornerShape(30),
        keyboardActions = KeyboardActions(onSearch = { onDone() }),
        trailingIcon = {
            //trailing icon are the right icons
            // if their is a value in the text field or filter icon is set to try then this layout is visible to user
            if (textFieldValue.isNotEmpty() || showFilterIcon) {
                Row {

                    //if textFieldValue is not empty, then show the cross button on which when user presses, set the value to empty
                    if (textFieldValue.isNotEmpty()) {
                        Image(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable {
                                    onValueChange("")
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cross),
                            contentDescription = stringResource(id = R.string.clear)
                        )
                    }

                    // if showFilterIcon is tru then show the filter icon

                    if (showFilterIcon) {
                        Image(modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(end = 5.dp)
                            .clickable {
                                onFilterIconPressed()
                            }
                            .padding(5.dp),

                            imageVector = ImageVector.vectorResource(R.drawable.is_filter),
                            contentDescription = stringResource(id = R.string.filter))
                    }
                }
            }
        })
}