package com.app.todolist.presentation.screens.add_edit_todo

import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.app.todolist.presentation.components.textfields.SmallText
import com.app.todolist.presentation.screens.add_edit_todo.viewmodel.AddEditEvent
import com.app.todolist.presentation.screens.add_edit_todo.viewmodel.AddEditTodoViewModel
import com.app.todolist.presentation.screens.add_edit_todo.viewmodel.AddEditUIEvent
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    navController: NavController,
    taskId: Int = -1,
    viewModel: AddEditTodoViewModel = hiltViewModel(),
) {


    val state = viewModel.dataState.value
    val categories = viewModel.categories.value

    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditUIEvent.Error -> {
                    Toast.makeText(
                        context, context.getString(R.string.unable_to_save_task), Toast.LENGTH_LONG
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
        monthSelection = true, yearSelection = true,
        minYear = Calendar.YEAR,
        disabledTimeline = CalendarTimeline.PAST,
    ), selection = CalendarSelection.Date { date ->
        viewModel.onEvent(AddEditEvent.SelectDueDate(date.toString()))
    })

    val clockState = rememberSheetState()
    ClockDialog(state = clockState, config = ClockConfig(
        defaultTime = LocalTime.now()
    ), selection = ClockSelection.HoursMinutesSeconds { hours, minutes, seconds ->
        println("TIME IN HOURS AND MINUTES AND SECONDS: , $hours : $minutes : $seconds")
//            selectedTime.value = "$hours:$minutes"
//            println(selectedTime)
    })
    val focusManager = LocalFocusManager.current
    Scaffold(modifier = Modifier.pointerInput(key1 = true) {
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }, topBar = {
        AppHeader(
            showBackButton = true,
            navController = navController,
            title = stringResource(id = if (taskId.toInt() != -1) R.string.edit_todo else R.string.add_todo)
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
                        textError = state.titleError,
                        onValueChange = { viewModel.onEvent(AddEditEvent.EnterTitle(it)) })

                    Spacer(modifier = Modifier.height(5.dp))

                    AppDescriptionTextField(textFieldValue = state.description,
                        hint = stringResource(id = R.string.description),
                        textError = state.descriptionError,
                        onValueChange = { viewModel.onEvent(AddEditEvent.EnterDescription(it)) })

                    Spacer(modifier = Modifier.height(10.dp))

                    PriorityView(defaultPrority = state.tasksPriority) {
                        it?.let {
                            viewModel.onEvent(AddEditEvent.EnterPriority(it))
                        }

                    }
                    if (state.priorityError != -1) SmallText(
                        text = stringResource(id = state.priorityError), isError = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CategoriesFilterView(
                        defaultValue = state.category, categories = categories
                    ) {
                        it?.let {
                            viewModel.onEvent(AddEditEvent.EnterCategory(it ?: ""))
                        }
                    }
                    if (state.categoryError != -1) SmallText(
                        text = stringResource(id = state.categoryError), isError = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                    Column(modifier = Modifier.weight(1f)) {
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
                                calendarState.show()
                            }
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            MediumText(
                                text = if (state.date.isNotEmpty()) state.date else "YYYY/MM/DD",
                                color = if (state.date.isNotEmpty()) Color.Black else Color.Gray
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Icon(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(15.dp)
                                    .rotate(90f),
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = null
                            )
                        }
                    }
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Column(modifier = Modifier.weight(1f)) {
//
//                            MediumText(
//                                text = stringResource(id = R.string.due_time),
//                                fontWeight = FontWeight.Bold
//                            )
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Row(modifier = Modifier
//                                .background(Color.White)
//                                .border(
//                                    width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp)
//                                )
//                                .clickable {
//                                    calendarState.show()
//                                }
//                                .padding(vertical = 5.dp, horizontal = 10.dp),
//                                verticalAlignment = Alignment.CenterVertically) {
//                                MediumText(
//                                    text = if (state.date.isNotEmpty()) state.date else "hh:mm:ss",
//                                    color = if (state.date.isNotEmpty()) Color.Black else Color.Gray
//                                )
//
//                                Spacer(modifier = Modifier.width(10.dp))
//
//                                Icon(
//                                    modifier = Modifier
//                                        .width(15.dp)
//                                        .height(15.dp),
//                                    painter = painterResource(id = android.R.drawable.arrow_down_float),
//                                    contentDescription = null
//                                )
//                            }
//                        }

//                    }

                    if (state.dateError != -1) SmallText(
                        text = stringResource(id = state.dateError), isError = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.clickable {
                            viewModel.onEvent(AddEditEvent.IsAddToRemember(!state.isAddToReminder))
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(
                            checked = state.isAddToReminder,
                            onCheckedChange = {
                                viewModel.onEvent(AddEditEvent.IsAddToRemember(!state.isAddToReminder))
                            },
                            modifier = Modifier.padding(end = 2.dp),
                        )
                        MediumText(
                            text = stringResource(R.string.add_reminder),
                        )
                    }

                    AppButton(
                        onClick = {
                            viewModel.onEvent(AddEditEvent.SaveNote)
                        }, modifier = Modifier.padding(12.dp, 0.dp), buttonText = R.string.save
                    )
                }
            }

        }


    }
}