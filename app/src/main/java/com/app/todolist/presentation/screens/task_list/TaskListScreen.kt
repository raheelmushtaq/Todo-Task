package com.app.todolist.presentation.screens.task_list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.presentation.components.appheader.AppHeader
import com.app.todolist.presentation.components.bottomsheets.filter.FilterBottomSheet
import com.app.todolist.presentation.components.edittext.SearchEditTextField
import com.app.todolist.presentation.components.loader.Loader
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.screens.task_list.component.TodoListItem
import com.app.todolist.presentation.screens.task_list.viewmodel.TodoListUIEvent
import com.app.todolist.presentation.screens.task_list.viewmodel.TodoListViewModel
import com.app.todolist.presentation.utils.screens.ScreenParams.TASK_ID
import com.app.todolist.presentation.utils.screens.ScreenRoutes
import com.app.todolist.ui.theme.TodoListTheme

@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    val state = viewModel.dataState.value
    val appSettings = viewModel.dataStoreLiveState.value

    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.pointerInput(key1 = true) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        topBar = {
            AppHeader(
                navController = navController,
                title = stringResource(id = R.string.app_name),
                showSettingsIcon = true
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    navController.navigate(ScreenRoutes.AddEditTaskScreen.route)
                },
                containerColor = Color.White,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
    ) { defaultPadding ->
        Column(modifier = Modifier.padding(defaultPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)

            ) {
                if (state.isLoading) {
                    Loader(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(
                    ) {

                        SearchEditTextField(
                            textFieldValue = state.searchText,
                            hint = stringResource(id = R.string.Filter),
                            showFilterIcon = true,
                            onFilterIconPressed = {
                                viewModel.onEvent(TodoListUIEvent.ShowFilter)

                            },
                            onValueChange = {
                                viewModel.onEvent(TodoListUIEvent.Search(it))
                            },
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                        if (appSettings.tasks.size != 0) {
                            LazyColumn(
                                modifier = Modifier.padding(top = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(appSettings.tasks, key = { it.id }) { item ->
                                    TodoListItem(
                                        item = item,
                                        onDelete = {
                                            viewModel.onEvent(TodoListUIEvent.Delete(item))
                                        },
                                        onMarkAsComplete = {
                                            viewModel.onEvent(TodoListUIEvent.MarkAsComplete(item))

                                        },
                                        onClick = {
                                            navController.navigate(
                                                ScreenRoutes.AddEditTaskScreen.route +
                                                        "?${TASK_ID}=${item.id}"
                                            )
                                        })
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {

                                LargeText(
                                    text = stringResource(id = R.string.no_record_found),
                                    fontWeight = FontWeight.Bold
                                )
                                MediumText(
                                    text = stringResource(
                                        id = R.string.no_record_found_nessage,
                                        state.searchText
                                    )
                                )
                            }
                        }

                        if (state.showFilterDialog) {
                            FilterBottomSheet(
                                heading = "Filter",
                                dialogState = state.showFilterDialog,
                                selectedTodoFilters = state.todoFilters,
                                applyFilter = {
                                    viewModel.onEvent(TodoListUIEvent.ApplyFilter(it))
                                }, onDismiss = {
                                    viewModel.onEvent(TodoListUIEvent.HideFilter)
                                },
                                categories = appSettings.categories
                            )
                        }
                    }
                }
            }
        }
    }


}

@Preview
@Composable
private fun TodoListPreview() {
    TodoListTheme {
    }
}