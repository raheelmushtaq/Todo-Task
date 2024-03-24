package com.app.todolist.presentation.utils.screens

/**
 * this class is used for creating the routes of the screen. they are used for routing screen
 */
sealed class ScreenRoutes(val route: String) {
    data object SplashScreen : ScreenRoutes("Splash")
    data object TaskListScreen : ScreenRoutes("TaskList")
    data object AddEditTaskScreen : ScreenRoutes("AddEditTask")
    data object Settings : ScreenRoutes("Settings")
}