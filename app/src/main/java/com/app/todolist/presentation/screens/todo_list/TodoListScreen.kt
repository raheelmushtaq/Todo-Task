package com.app.todolist.presentation.screens.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
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
import com.app.todolist.presentation.components.progress.Loader
import com.app.todolist.presentation.components.textfields.LargeText
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.screens.todo_list.component.TodoListItem
import com.app.todolist.presentation.screens.todo_list.viewmodel.TodoListUIEvent
import com.app.todolist.presentation.screens.todo_list.viewmodel.TodoListViewModel
import com.app.todolist.ui.theme.TodoListTheme
import com.app.todolist.presentation.screens_routes.ScreenParams.TASK_ID
import com.app.todolist.presentation.screens_routes.ScreenRoutes

@Composable
fun TodoListScreen(
    navController: NavController, viewModel: TodoListViewModel = hiltViewModel()
) {
    val state = viewModel.dataState.value
    val showFilterDialog = viewModel.showFilterDialog.value

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.pointerInput(key1 = true) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        topBar = {
            AppHeader(
                navController = navController,
                title = stringResource(id = R.string.app_name),
                showSettingsIcon = false
            )

        },
        floatingActionButton = {
            FloatingActionButton(
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
                        if (state.todoList.size != 0) {
                            LazyColumn(
                                modifier = Modifier.padding(top = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(state.todoList, key = { it.id }) { item ->
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

                        if (showFilterDialog) {
                            FilterBottomSheet(
                                heading = "Filter",
                                dialogState = showFilterDialog,
                                selectedTodoFilters = state.todoFilters,
                                applyFilter = {
                                    viewModel.onEvent(TodoListUIEvent.ApplyFilter(it))
                                }, onDismiss = {
                                    viewModel.onEvent(TodoListUIEvent.HideFilter)
                                },
                                categories = state.categories
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