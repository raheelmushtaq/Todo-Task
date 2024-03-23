package com.app.todolist.presentation.screens.task_list.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.screens.DataProvider.categories
import com.app.todolist.presentation.screens.DataProvider.listOfTasks
import com.app.todolist.presentation.screens.DataProvider.taskCountOfCategoryOther
import com.app.todolist.presentation.screens.DataProvider.taskCountOfPriorityHigh
import com.app.todolist.presentation.screens.FakeDataSourceHandler
import com.app.todolist.presentation.screens.task_list.state_event.TaskListActionEvents
import com.app.todolist.presentation.utils.filters.TaskFilters
import com.app.todolist.presentation.utils.filters.TaskPriority
import com.google.common.truth.Truth
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TaskListViewModelTest {
    private lateinit var dataStoreHandler: FakeDataSourceHandler
    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setUp() {

        dataStoreHandler = FakeDataSourceHandler()

        runBlocking {
            dataStoreHandler.saveCategories(categories)
            dataStoreHandler.addTasks(listOfTasks)
        }
        viewModel = TaskListViewModel(dataStoreHandler)
    }

    @Test
    fun runInitialTest() {
        val value = viewModel.dataState.value
        Truth.assertThat(value.searchText).isEmpty()
    }

    @Test
    fun getTasksWithEmptySearchAndNoFilter() = runBlocking {
        viewModel.onEvent(TaskListActionEvents.Search(""))
        delay(2000)
        val setting: AppSettings = viewModel.dataStoreLiveState.value

        Truth.assertThat(setting.tasks).isNotEmpty()
    }

    @Test
    fun getTasksWithSearchAndNoFilter() = runBlocking {
        viewModel.onEvent(TaskListActionEvents.Search("7"))
        delay(2000)
        val setting: AppSettings = viewModel.dataStoreLiveState.value

        Truth.assertThat(setting.tasks.size).isEqualTo(1)
    }

    @Test
    fun getTasksWithEmptySearchAndPriorityHighFilter() = runBlocking {
        viewModel.onEvent(TaskListActionEvents.ApplyFilter(TaskFilters(taskPriority = TaskPriority.High)))
        delay(2000)
        val setting: AppSettings = viewModel.dataStoreLiveState.value

        Truth.assertThat(setting.tasks.find { item -> item.priority == TaskPriority.Low.value })
            .isEqualTo(null)

        Truth.assertThat(setting.tasks.size).isEqualTo(taskCountOfPriorityHigh)
    }

    @Test
    fun getTasksWithEmptySearchAndWithCategory() = runBlocking {
        viewModel.onEvent(TaskListActionEvents.ApplyFilter(TaskFilters(category = "Other")))
        delay(2000)
        val setting: AppSettings = viewModel.dataStoreLiveState.value

        Truth.assertThat(setting.tasks.find { item -> item.category == "asdf" }).isEqualTo(null)

        Truth.assertThat(setting.tasks.size).isEqualTo(taskCountOfCategoryOther)
    }

    @Test
    fun deleteTest() = runBlocking {
        delay(2000)
        var setting: AppSettings = viewModel.dataStoreLiveState.value

        val beforeDeleting = setting.tasks.find { item -> item.id == 3 }
        viewModel.onEvent(TaskListActionEvents.Delete(task = Tasks(id = 3)))
        delay(2000)
        setting = viewModel.dataStoreLiveState.value

        val afterDeleting = setting.tasks.find { item -> item.id == 3 }

        Truth.assertThat(beforeDeleting).isNotEqualTo(null)
        Truth.assertThat(afterDeleting).isEqualTo(null)
    }

    @Test
    fun markAsCompleteTest() = runBlocking {
        delay(2000)
        var setting: AppSettings = viewModel.dataStoreLiveState.value

        val beforeDeleting = setting.tasks.find { item -> item.id == 2 }
        viewModel.onEvent(TaskListActionEvents.MarkAsComplete(task = Tasks(id = 2)))
        delay(2000)
        setting = viewModel.dataStoreLiveState.value

        val afterDeleting = setting.tasks.find { item -> item.id == 2 }

        Truth.assertThat(beforeDeleting?.isCompleted).isEqualTo(false)
        Truth.assertThat(afterDeleting?.isCompleted).isEqualTo(true)
    }


}
