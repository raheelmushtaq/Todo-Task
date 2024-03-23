package com.app.todolist.presentation.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.presentation.components.appheader.AppHeader
import com.app.todolist.presentation.components.textfields.MediumText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {

    val focusManager = LocalFocusManager.current

    val localeOptions = mapOf(
        R.string.en to "en",
        R.string.ar to "ar-AE",
    ).mapKeys { stringResource(it.key) }

    fun handleChangeLocation(selectionLocale: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(
                localeOptions[selectionLocale]
            )
        )
    }

    Scaffold(modifier = Modifier.pointerInput(key1 = true) {
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }, topBar = {
        AppHeader(
            navController = navController,
            title = stringResource(id = R.string.settings),
            showBackButton = true
        )

    }) { defaultPadding ->
        Column(modifier = Modifier.padding(defaultPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)

            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MediumText(
                        modifier = Modifier.weight(0.5f),
                        text = stringResource(id = R.string.language), fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        modifier = Modifier.weight(1f),

                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        TextField(
                            readOnly = true,
                            value = stringResource(R.string.selected_language),
                            onValueChange = { },
                            modifier = Modifier
                                .menuAnchor(),
                            label = { Text(stringResource(id = R.string.language)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            localeOptions.keys.forEach { selectionLocale ->
                                DropdownMenuItem(
                                    text = {
                                        Text(selectionLocale)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        handleChangeLocation(selectionLocale)
                                        expanded = false

                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}