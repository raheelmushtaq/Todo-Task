package com.app.todolist.presentation.screens

import com.app.todolist.presentation.models.Tasks
import com.app.todolist.presentation.utils.filters.TaskPriority

object DataProvider {
    val priorityList =
        arrayListOf(TaskPriority.High, TaskPriority.Medium, TaskPriority.Low)

    val categories = listOf("Other", "Family", "Office")
    val listOfTasks = listOf(
        Tasks(
            id = 1,
            "Task 1",
            "Description 1",
            categories.random(),
            "20-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 2,
            "Task 2",
            "Description 2",
            categories.random(),
            "24-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 3,
            "Task 3",
            "Description 3",
            categories.random(),
            "20-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 4,
            "Task 4",
            "Description 4",
            categories.random(),
            "25-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 5,
            "Task 5",
            "Description 5",
            categories.random(),
            "31-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 6,
            "Task 6",
            "Description 6",
            categories.random(),
            "30-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 7,
            "Task 7",
            "Description 7",
            categories.random(),
            "27-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 8,
            "Task 8",
            "Description 8",
            categories.random(),
            "02-4-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 9,
            "Task 9",
            "Description 9",
            categories.random(),
            "26-3-2024",
            priorityList.random().value,
            false
        ),
        Tasks(
            id = 10,
            "Task 10",
            "Description 10",
            categories.random(),
            "19-3-2024",
            priorityList.random().value,
            false
        ),
    )

    val taskCountOfCategoryOther = listOfTasks.filter { it.category == "Other" }.size

    val taskCountOfPriorityHigh = listOfTasks.filter { it.priority == TaskPriority.High.value }.size
}