package com.app.todolist.presentation.components.edittext

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.todolist.R
import com.app.todolist.presentation.components.textfields.SmallText

@Composable
fun AppEditTextField(
    textFieldValue: String,
    hint: String,
    onValueChange: (String) -> Unit,
    textError: Int = -1,
    readOnly: Boolean = true,
    onDone: () -> Unit = {},
) {
    Column {
        OutlinedTextField(
            value = textFieldValue,
            label = { Text(text = hint) },
            singleLine = true,
            readOnly = readOnly,
            shape = RoundedCornerShape(30),
            isError = textError != -1,
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
        if (textError != -1) SmallText(text = stringResource(id = textError), isError = true)
    }
}

@Composable
fun AppDescriptionTextField(
    textFieldValue: String,
    hint: String,
    onValueChange: (String) -> Unit,
    textError: Int = -1,
    readOnly: Boolean = true,
    onDone: () -> Unit = {},
) {
    Column {

        OutlinedTextField(
            value = textFieldValue,
            label = { Text(text = hint) },
            singleLine = false,
            readOnly = readOnly,
            isError = textError != -1,
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
        if (textError != -1) SmallText(text = stringResource(id = textError), isError = true)
    }
}

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
            Image(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),

                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.Filter)
            )
        },
        visualTransformation = VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth(),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        shape = RoundedCornerShape(30),
        keyboardActions = KeyboardActions(onSearch = { onDone() }),
        trailingIcon = {
            if (textFieldValue.isNotEmpty() || showFilterIcon) {
                Row {

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


                    if (showFilterIcon) {
                        Image(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .padding(1.dp)
                                .padding(end = 10.dp)
                                .clickable {
                                    onFilterIconPressed()
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.is_filter),
                            contentDescription = stringResource(id = R.string.filter)
                        )
                    }
                }
            }
        })
}


@Preview
@Composable
fun PreviewAppEditText() {
    Column {
        SearchEditTextField(textFieldValue = "", hint = "Search", onValueChange = {})
    }
}