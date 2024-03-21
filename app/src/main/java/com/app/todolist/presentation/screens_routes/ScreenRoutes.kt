package com.app.todolist.presentation.screens_routes

sealed class ScreenRoutes(val route: String) {
    data object SplashScreen : ScreenRoutes("Splash")
    data object TodoListScreen : ScreenRoutes("TodoList")
    data object AddEditTaskScreen : ScreenRoutes("AddEditTask")
    data object Settings : ScreenRoutes("Settings")
}