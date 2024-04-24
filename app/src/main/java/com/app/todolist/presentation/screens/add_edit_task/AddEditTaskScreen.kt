package com.app.todolist.presentation.screens.add_edit_task

import android.icu.util.Calendar
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    navController: NavController,
    taskId: Int = -1,
    viewModel: AddEditTodoViewModel = hiltViewModel(),
) {


    // fetching the current value of the data state. when the datastate is update
    val state = viewModel.dataState.value

    //fetching the categories from  data store
    val categories = viewModel.appSettings.value.categories

    // context to show toast to user
    val context = LocalContext.current


    // by setting the key1 to true, this LaunchEffect is only generated at first and it is not recomposed when state changes
    LaunchedEffect(key1 = true) {
        // here we are listening to any action which is fot user to see from viewmodel i.e. success of saving or updating tasks, or showing error title is missing
        viewModel.addEditUiEvent.collectLatest { event ->
            when (event) {
                is AddEditUIEvent.Error -> {
                    // the error is thrown than show toast to user
                    Toast.makeText(
                        context, context.getString(event.errorId), Toast.LENGTH_SHORT
                    ).show()
                }

                is AddEditUIEvent.Success -> {
                    // the success is thrown than show toast to user and navigate back to previous screen
                    Toast.makeText(
                        context,
                        context.getString(if (taskId == -1) R.string.task_added_successfully else R.string.task_updated_successfuly),
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }
            }
        }
    }

    // this is used from teh library maxkeppeler where it is used to remember the current state of hte Calender
    val calendarState = rememberSheetState()
    //created a calender Dialog for user with default settings
    CalendarDialog(state = calendarState, config = CalendarConfig(
        monthSelection = true,
        yearSelection = true,
        minYear = Calendar.YEAR,
        disabledTimeline = CalendarTimeline.PAST,
    ), selection = CalendarSelection.Date { date ->
//        when date is selected the pass to the viewmodel
        viewModel.onEvent(
            AddEditActionEvent.SelectDueDate(
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            )
        )
    })

    // focus manager is used remove the focus from any text field
    val focusManager = LocalFocusManager.current
    //Scaffold implements the basic material design visual layout structure.
    //This component provides API to put together several material components to construct your screen
    Scaffold(modifier = Modifier.pointerInput(key1 = true) {
        // when user presses our side of the ui then close the keyboard
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }, topBar = {
        // check which title to show. if task id is not provided then show add task,
        // but if it provided then check what is the state of completing status and update the title according
        val title = if (taskId != -1) {
            if (state.isCompleted) R.string.completed_task
            else R.string.edit_todo
        } else {
            R.string.add_todo
        }
        // add AppHeader as Screen Header, showing settings icon
        AppHeader(
            showBackButton = true, navController = navController, title = stringResource(id = title)
        )
    }) { padding ->

//create the base column with the padding provided by scaffold
        Column(modifier = Modifier.padding(padding)) {
            // create a box as parent
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                // adding a column to show composable
                Column(
                    modifier = Modifier.padding(bottom = 5.dp),
                ) {

                    // adding a text field for taking title as input from user
                    AppEditTextField(textFieldValue = state.title,
                        hint = stringResource(id = R.string.title),
                        readOnly = state.isCompleted,
                        onValueChange = { text ->
                            // when ever title is updated, then sent event to the ViewModel
                            viewModel.onEvent(
                                AddEditActionEvent.EnterTitle(
                                    text
                                )
                            )
                        },
                        onDone = {
                            //when user presses the done button, then clear the focus and hide keyboard
                            focusManager.clearFocus()
                        })

                    Spacer(modifier = Modifier.height(5.dp))

                    // adding a description field for taking description of task as input from user

                    AppDescriptionTextField(textFieldValue = state.description,
                        hint = stringResource(id = R.string.description),
                        readOnly = state.isCompleted,
                        onValueChange = { text ->
                            // when ever title is updated, then sent event to the ViewModel
                            viewModel.onEvent(AddEditActionEvent.EnterDescription(text))
                        },
                        onDone = {                            //when user presses the done button, then clear the focus and hide the keyboard
                            focusManager.clearFocus()
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    // showing the priority view
                    PriorityView(
                        defaultPriority = state.taskPriority, showOnlySelected = state.isCompleted,
                    ) {
                        it?.let {
                            // when ever Task Priority is selected, then sent event to the ViewModel
                            viewModel.onEvent(AddEditActionEvent.EnterPriority(it))
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // showing Categories view to user where user ha to select the categories.
                    // the categories are retrieved from the datastore and showed here
                    CategoriesFilterView(
                        defaultValue = state.category,
                        categories = if (state.isCompleted) arrayListOf(state.category.toString()) else categories,
                        readOnly = state.isCompleted
                    ) {
                        it?.let {
                            // when ever Category is selected, then sent event to the ViewModel
                            viewModel.onEvent(AddEditActionEvent.EnterCategory(it))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    //showing Due date view
                    Column {

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
                                // when user presses the due date then show the dialog if the task is not completed
                                if (!state.isCompleted) calendarState.show()
                            }
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            // set the date
                            MediumText(
                                text = state.date.ifEmpty { "DD-MM-YYYY" },
                                color = if (state.date.isNotEmpty())
                                    Color.Black
                                else
                                    Color.Gray
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // arrow icon to the end of due date
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

                    // if the task is competed the their is no need to show the button to update or save
                    if (!state.isCompleted) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // checking if user is updating the task, show cancel button
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
                            // show add button to user but if it is editing the task then change the text to user
                            AppButton(
                                onClick = {
                                    // on pressing the save button, send event to save task
                                    viewModel.onEvent(AddEditActionEvent.SaveTask)
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