package com.app.todolist.presentation.screens.add_edit_task.viewmodel

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.todolist.R
import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.screens.DataProvider
import com.app.todolist.presentation.screens.DataProvider.categories
import com.app.todolist.presentation.screens.DataProvider.priorityList
import com.app.todolist.presentation.screens.FakeDataSourceHandler
import com.app.todolist.presentation.screens.add_edit_task.FakeNotificationScheduler
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditActionEvent
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditDataState
import com.app.todolist.presentation.screens.add_edit_task.state_event.AddEditUIEvent
import com.google.common.truth.Truth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddEditTodoViewModelTest {
    private lateinit var dataStoreHandler: FakeDataSourceHandler
    private lateinit var notificationScheduler: FakeNotificationScheduler
    private lateinit var viewModel: AddEditTodoViewModel

    @Before
    fun setUp() {

        dataStoreHandler = FakeDataSourceHandler()
        notificationScheduler = FakeNotificationScheduler()

        runBlocking {
            dataStoreHandler.saveCategories(DataProvider.categories)
            dataStoreHandler.addTasks(DataProvider.listOfTasks)
        }
        viewModel =
            AddEditTodoViewModel(dataStoreHandler, notificationScheduler, SavedStateHandle())

    }


    @Test
    fun saveTask() = runBlocking {
        delay(500)

        viewModel.saveTask(
            100,
            "Hello",
            "Hello Description",
            categories.random(),
            "31-03-2024",
            priorityList.random().value,
            isCompleted = false,
            isEditTask = false
        )
        delay(1000)


        val tasks = viewModel.getTaskById(100)
        Truth.assertThat(tasks).isNotEqualTo(null)
    }

    @Test
    fun updateTask() = runBlocking {
        delay(500)

        val beforeUpdatingTask = viewModel.getTaskById(2)

        viewModel.saveTask(
            2,
            "Hello",
            "Hello Description",
            categories.random(),
            "31-03-2024",
            priorityList.random().value,
            isCompleted = false,
            isEditTask = true
        )
        delay(2000)

        val afterUpdatingTask = viewModel.getTaskById(2)

        Truth.assertThat(afterUpdatingTask).isNotEqualTo(null)
        Truth.assertThat(beforeUpdatingTask).isNotEqualTo(afterUpdatingTask)


    }

    @Test
    fun getTaskByIdTest() = runBlocking {
        delay(500)

        val tasks = viewModel.getTaskById(2)
        Truth.assertThat(tasks).isNotEqualTo(null)
    }


}