package com.app.todolist.presentation.utils.screens

sealed class ScreenRoutes(val route: String) {
    data object SplashScreen : ScreenRoutes("Splash")
    data object TaskLisstScreen : ScreenRoutes("TaskList")
    data object AddEditTaskScreen : ScreenRoutes("AddEditTask")
    data object Settings : ScreenRoutes("Settings")
}