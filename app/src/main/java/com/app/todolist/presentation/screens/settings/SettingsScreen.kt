package com.app.todolist.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.data.models.OrderBy
import com.app.todolist.presentation.components.Dialogs.ConfirmationDialog
import com.app.todolist.presentation.components.appheader.AppHeader
import com.app.todolist.presentation.components.bottomsheets.filter.CategoriesFilterView
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.screens.settings.modal.Languages
import com.app.todolist.presentation.screens.settings.viewmodel.SettingsScreenViewModel
import com.app.todolist.presentation.screens.settings.viewmodel.SettingsUIEvent
import com.app.todolist.utils.LanguageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController, viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val state = viewModel.dataState.value

    val focusManager = LocalFocusManager.current

    val currentLanguage = remember {
        mutableStateOf(state.language)
    }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.pointerInput(key1 = true) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        topBar = {
            AppHeader(
                navController = navController,
                title = stringResource(id = R.string.settings),
                showBackButton = true
            )

        }
    ) { defaultPadding ->
        Column(modifier = Modifier.padding(defaultPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)

            ) {

                Column(
                    modifier = Modifier.padding(bottom = 5.dp),
                ) {

                    MediumText(text = stringResource(id = R.string.languge), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        Languages.entries.map { lang ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = Languages.entries.indexOf(lang),
                                    count = Languages.entries.size,
                                ),
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = Color.White,
                                    inactiveContainerColor = Color.White,
                                ),
                                onClick = {
                                    if (currentLanguage.value != lang) {
                                        currentLanguage.value = lang
                                        viewModel.onEvent(SettingsUIEvent.ShowDialog)
                                    }

                                },
                                selected = currentLanguage.value == lang
                            ) {
                                MediumText(text = lang.key)
                            }
                        }
                    }

                    if (state.showConfirmationDialog) {
                        ConfirmationDialog(
                            dialogTitle = stringResource(id = R.string.change_language),
                            dialogText = stringResource(id = R.string.change_language_confirmation),
                            onDismissRequest = {
                                viewModel.onEvent(SettingsUIEvent.ChangeLanguage(state.language))
                                currentLanguage.value = state.language

                            },
                            onConfirmation = {
                                viewModel.onEvent(SettingsUIEvent.ChangeLanguage(currentLanguage.value))
                                LanguageUtils.changeLanguage(context, currentLanguage.value.key)

                            })
                    }
                }
            }
        }
    }


}