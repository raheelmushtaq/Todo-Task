package com.app.todolist.presentation.screens.task_list.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.datastore.DataStoreHandlerInterface
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.screens.task_list.state_event.TaskListActionEvents
import com.app.todolist.presentation.screens.task_list.state_event.TaskListDataState
import com.app.todolist.presentation.utils.filters.OrderBy
import com.app.todolist.presentation.utils.filters.SortBy
import com.app.todolist.presentation.utils.filters.TaskFilters
import com.app.todolist.utils.DebounceSearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* ViewModel: AddEditTodoViewModel
* this is linked with the AddEditScreen.
* it takes 1 paramters
* dataStoreHandler for adding or updating task in datastore
* */
@HiltViewModel
class TaskListViewModel @Inject constructor(private val dataStoreHandler: DataStoreHandlerInterface) :
    ViewModel() {
    // create a mutable state for listening to the datastore from the datastore. when datastore is updated or at first launch of viewmode, this value is updated
    // here it is only listening the the data state.
    private val _appSettings = mutableStateOf(AppSettings())

    // created a mutable state for user with default value,
    // when user enters a title, then this state is updated with the lates title
    private val _dataState = mutableStateOf(TaskListDataState())
    //this is State variable of the _dataState, it is not mutable, so in AddEditScreen it is used to listen for update and recompose the ui on this state change
    val dataState: State<TaskListDataState> = _dataState
    //create a mutable state for listening to the datastore from the datastore. when datastore is updated or at first launch of viewmode, this value is updated

    private val _dataStoreLiveState = mutableStateOf(AppSettings())
    //this is State variable of the _appsettings, it is not mutable, so in AddEditScreen it is used to listen for update and recompose the ui on this state change
    val dataStoreLiveState: MutableState<AppSettings> = _dataStoreLiveState

    init {
        // in this init of viewmodel  added a flow listener on the datastore, update the _appsettings value
        viewModelScope.launch {
            dataStoreHandler. getAppSettings().onEach {
                _appSettings.value = it
                _dataStoreLiveState.value = it
                // fetch task on teh basis of previous search
                getTasks(dataState.value.searchText, dataState.value.taskFilters)
            }.launchIn(viewModelScope)
        }
    }

    /*
    * fetching the task based on the search or iflter*/
    private fun getTasks(searchText: String = "", taskFilters: TaskFilters = TaskFilters()) {
        viewModelScope.launch {
            var tasks =
                if (searchText.isEmpty()) {
//            if search is empty the use all the tasks for applying fikter
                    _appSettings.value.tasks.toList()
                } else {
                    // if search is not empty then check check tiel or description contains the searched text
                    _appSettings.value.tasks.toList().filter { task ->
                        task.title.lowercase().contains(searchText) || task.description.lowercase()
                            .contains(searchText)
                    }
                }
            // now here check if the taskPriority has been selected
            if (taskFilters.taskPriority != null) {
                // if priority is selected, then filter out all only the selected tasks based on priority
                tasks = tasks.filter { task ->
                    task.priority.equals(
                        taskFilters.taskPriority.value, true
                    )
                }
            }
            // now here check if the category has been selected
            if (taskFilters.category != null) {
                // if category is selected, then filter out all only the selected tasks based on category
                tasks = tasks.filter { task -> task.category.equals(taskFilters.category, true) }
            }


//            apply sorting based on sort by
            tasks = when (taskFilters.sortBy) {
                is SortBy.Ascending -> {
                    // Ascending sort, then sort them by the orderby field
                    when (taskFilters.orderBy) {
                        is OrderBy.Title -> tasks.sortedBy { it.title.lowercase() }
                        is OrderBy.Date -> tasks.sortedBy { it.date }
                        is OrderBy.Completed -> tasks.sortedBy { it.isCompleted }
                    }
                }
                    // Descending sort, then sort them by the orderby field
                is SortBy.Descending -> {
                    when (taskFilters.orderBy) {
                        is OrderBy.Title -> tasks.sortedByDescending { it.title.lowercase() }
                        is OrderBy.Date -> tasks.sortedByDescending { it.date }
                        is OrderBy.Completed -> tasks.sortedByDescending { it.isCompleted }
                    }
                }
            }
            // set state the of the filter applies
            _dataState.value =
                dataState.value.copy(
                    searchText = searchText,
                    taskFilters = taskFilters,
                    showFilterDialog = false
                )
            // set filter tasks for the Ui to display
            _dataStoreLiveState.value =
                _appSettings.value.copy(tasks = tasks.toPersistentList())
        }
    }

    /*
      * when user perform action on the UI, like pressing the save button , then this function is called.
      * this funtion takes the TaskListActionEvents as input and does the remaining handling
      */
    fun onEvent(event: TaskListActionEvents) {
        //checking which kind of action user whas perform
        when (event) {
            // whe Apply filter is pressed, the getTasks from the datastore using the selected fitler and search Text
            is TaskListActionEvents.ApplyFilter -> {
                getTasks(dataState.value.searchText, event.taskFilters)
            }

            // whe search has been entered , the getTasks from the datastore usin that seach and filter
            is TaskListActionEvents.Search -> {
                _dataState.value = dataState.value.copy(searchText = event.searchText)
                DebounceSearchUtils.setSearchTextChange {
                    getTasks(event.searchText, dataState.value.taskFilters)
                }
            }
            // whe show filter is press, then update the value of  showFilterDialog
            is TaskListActionEvents.ShowFilter -> {
                _dataState.value = dataState.value.copy(showFilterDialog = true)
            }
            // whe hide filter is press, then update the value of showFilterDialog
            is TaskListActionEvents.HideFilter -> {
                _dataState.value = dataState.value.copy(showFilterDialog = false)
            }

            // whe delete button is pressed, then delete the tasks from datastore
            is TaskListActionEvents.Delete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreHandler.deleteTasks(event.task)
                }
            }

            // whe mark as complete button si pressed,  then update the tasks from datastore
            is TaskListActionEvents.MarkAsComplete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val task = event.task.copy(isCompleted = true)
                    dataStoreHandler.updateTask(task)
                }
            }
        }
    }
}
