package com.app.todolist.presentation.screens.add_edit_task

import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.presentation.components.appheader.AppHeader
import com.app.todolist.presentation.components.bottomsheets.filter.CategoriesFilterView
import com.app.todolist.presentation.components.bottomsheets.filter.PriorityView
import com.app.todolist.presentation.components.button.AppButton
import com.app.todolist.presentation.components.edittext.AppDescriptionTextField
import com.app.todolist.presentation.components.edittext.AppEditTextField
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditActionEvent
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditUIEvent
import com.app.todolist.presentation.screens.add_edit_task.viewmodel.AddEditTodoViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    navController: NavController,
    taskId: Int = -1,
    viewModel: AddEditTodoViewModel = hiltViewModel(),
) {


    val state = viewModel.dataState.value
    val categories = viewModel.appSettings.value.categories

    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        viewModel.addEditUiEvent.collectLatest { event ->
            when (event) {
                is AddEditUIEvent.Error -> {
                    Toast.makeText(
                        context, context.getString(event.errorId), Toast.LENGTH_LONG
                    ).show()
                }

                is AddEditUIEvent.Success -> {
                    Toast.makeText(
                        context,
                        context.getString(if (taskId.toInt() == -1) R.string.task_added_successfully else R.string.task_updated_successfuly),
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
            }
        }
    }

    val calendarState = rememberSheetState()
    CalendarDialog(state = calendarState, config = CalendarConfig(
        monthSelection = true,
        yearSelection = true,
        minYear = Calendar.YEAR,
        disabledTimeline = CalendarTimeline.PAST,
    ), selection = CalendarSelection.Date { date ->
        viewModel.onEvent(
            AddEditActionEvent.SelectDueDate(
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            )
        )
    })

    val focusManager = LocalFocusManager.current
    Scaffold(modifier = Modifier.pointerInput(key1 = true) {
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }, topBar = {
        val title = if (taskId != -1) {
            if (state.isCompleted) R.string.completed_task
            else R.string.edit_todo
        } else {
            R.string.add_todo
        }
        AppHeader(
            showBackButton = true, navController = navController, title = stringResource(id = title)
        )
    }) {

        Column(modifier = Modifier.padding(it)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)

            ) {
                Column(
                    modifier = Modifier.padding(bottom = 5.dp),
                ) {

                    AppEditTextField(textFieldValue = state.title,
                        hint = stringResource(id = R.string.title),
                        readOnly = state.isCompleted,
                        onValueChange = { viewModel.onEvent(AddEditActionEvent.EnterTitle(it)) },
                        onDone = { focusManager.clearFocus() })

                    Spacer(modifier = Modifier.height(5.dp))

                    AppDescriptionTextField(textFieldValue = state.description,
                        hint = stringResource(id = R.string.description),
                        readOnly = state.isCompleted,
                        onValueChange = { viewModel.onEvent(AddEditActionEvent.EnterDescription(it)) },
                        onDone = { focusManager.clearFocus() })

                    Spacer(modifier = Modifier.height(10.dp))

                    PriorityView(
                        defaultPriority = state.taskPriority, showOnlySelected = state.isCompleted,
                    ) {
                        it?.let {
                            viewModel.onEvent(AddEditActionEvent.EnterPriority(it))
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CategoriesFilterView(
                        defaultValue = state.category,
                        categories = if (state.isCompleted) arrayListOf(state.category.toString()) else categories,
                        readOnly = state.isCompleted
                    ) {
                        it?.let {
                            viewModel.onEvent(AddEditActionEvent.EnterCategory(it ?: ""))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column() {

                        MediumText(
                            text = stringResource(id = R.string.due_date),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(modifier = Modifier
                            .background(Color.White)
                            .border(
                                width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                if (!state.isCompleted) calendarState.show()
                            }
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            MediumText(
                                text = if (state.date.isNotEmpty()) state.date else "DD-MM-YYYY",
                                color = if (state.date.isNotEmpty()) Color.Black else Color.Gray
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Icon(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(15.dp),
                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (!state.isCompleted) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (taskId != -1) {
                                Row(modifier = Modifier.weight(1f)) {

                                    AppButton(buttonText = R.string.cancel,
                                        modifier = Modifier.weight(1f),
                                        isDisabled = false,
                                        isSecondary = true,
                                        onClick = {
                                            navController.navigateUp()
                                        })
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                            AppButton(
                                onClick = {
                                    viewModel.onEvent(AddEditActionEvent.SaveNote)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(12.dp, 0.dp),
                                buttonText = if (taskId != -1) R.string.update else R.string.save
                            )
                        }
                    }
                }
            }
        }
    }
}