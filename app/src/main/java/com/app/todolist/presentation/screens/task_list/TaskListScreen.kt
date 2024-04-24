package com.app.todolist.presentation.screens.task_list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.app.todolist.presentation.screens.task_list.component.TaskItemCard
import com.app.todolist.presentation.screens.task_list.state_event.TaskListActionEvents
import com.app.todolist.presentation.screens.task_list.viewmodel.TaskListViewModel
import com.app.todolist.presentation.utils.screens.ScreenParams.TASK_ID
import com.app.todolist.presentation.utils.screens.ScreenRoutes

@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    // fetching the current value of the data state. when the datastate is update
    val state = viewModel.dataState.value
    //fetching the current value of the app datastore. when the task is filtered, or deleted or mark as completed the for
    val appSettings = viewModel.dataStoreLiveState.value

    //using focus manager here to clear focus
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(state.taskFilters, appSettings.recordCount) {
        listState.animateScrollToItem(0, 0)
    }


    //Scaffold implements the basic material design visual layout structure.
    //This component provides API to put together several material components to construct your screen
    Scaffold(
        modifier = Modifier
            .pointerInput(key1 = true) {
                // when user presses our side of the ui then close the keyboard
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
            ),
        topBar = {
            // add app header as Screen Header, showing settings icon
            AppHeader(
                navController = navController,
                title = stringResource(id = R.string.app_name),
                showSettingsIcon = true
            )

        },
        floatingActionButton = {
            // added a floating action button
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    // on clicking opening he new screen
                    navController.navigate(ScreenRoutes.AddEditTaskScreen.route)
                },
                containerColor = Color.White,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
    ) { defaultPadding ->
        //create the base column with the padding provided by scaffold
        Column(modifier = Modifier.padding(defaultPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)

            ) {
                // if current state is loading then show loader
                if (state.isLoading) {
                    Loader(modifier = Modifier.align(Alignment.Center))
                } else {
                    // adding a column to show composable
                    Column {

                        // adding a Search field for taking text to perform search
                        SearchEditTextField(
                            textFieldValue = state.searchText,
                            hint = stringResource(id = R.string.Filter),
                            showFilterIcon = true,
                            onFilterIconPressed = {
                                /// on pressing filter icon send event to viewModel
                                viewModel.onEvent(TaskListActionEvents.ShowFilter)

                            },
                            onValueChange = {
                                // when ever search is updated, then sent event to the ViewModel
                                viewModel.onEvent(TaskListActionEvents.Search(it))
                            },
                            onDone = {
                                //on pressing the done button hide the keyboard

                                focusManager.clearFocus()
                            }
                        )
                        // checking if the tasks size if not equal to 0 the showing list of columns
                        if (appSettings.tasks.size != 0) {
                            // showing lazy column
                            LazyColumn(
                                modifier = Modifier.padding(top = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                state = listState
                            ) {
                                items(appSettings.tasks, key = { it.id }) { item ->
//                                    adding a task-listItem with tasks as parameter
                                    TaskItemCard(
                                        item = item,
                                        onDelete = {
                                            // when user presses teh delete button send event to the viewmodel
                                            focusManager.clearFocus()
                                            viewModel.onEvent(TaskListActionEvents.Delete(item))
                                        },
                                        onMarkAsComplete = {
                                            // when user presses teh mark complete button send event to the viewmodel
                                            focusManager.clearFocus()
                                            viewModel.onEvent(
                                                TaskListActionEvents.MarkAsComplete(
                                                    item
                                                )
                                            )

                                        },
                                        onClick = {
                                            focusManager.clearFocus()
                                            // on click on the View, take to to the edit screen
                                            navController.navigate(
                                                ScreenRoutes.AddEditTaskScreen.route +
                                                        "?${TASK_ID}=${item.id}"
                                            )
                                        })
                                }
                            }
                        } else {
                            // if their are no tasks then show empty view
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 10.dp)
                            ) {

                                LargeText(
                                    text = stringResource(id = R.string.no_record_found),
                                    fontWeight = FontWeight.Bold
                                )
                                MediumText(
                                    text = stringResource(
                                        id = R.string.no_record_found_nessage,
                                        state.searchText
                                    ),
                                    align = TextAlign.Center
                                )
                            }
                        }

                        // if the show filtering is true then show the filterBottomSheet
                        if (state.showFilterDialog) {
                            FilterBottomSheet(
                                heading = "Filter",
                                dialogState = state.showFilterDialog,
                                selectedTaskFilters = state.taskFilters,
                                applyFilter = {
                                    // when user presses apply button this event to the viewmodel
                                    viewModel.onEvent(TaskListActionEvents.ApplyFilter(it))
                                }, onDismiss = {
                                    // when dialog auto dismisses on drag on tap Outside then  this event to the viewmodel
                                    viewModel.onEvent(TaskListActionEvents.HideFilter)
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
