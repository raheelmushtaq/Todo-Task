package com.app.todolist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.todolist.presentation.screens.add_edit_task.AddEditTodoScreen
import com.app.todolist.presentation.screens.settings.SettingsScreen
import com.app.todolist.presentation.screens.splash.SplashScreen
import com.app.todolist.presentation.screens.task_list.TodoListScreen
import com.app.todolist.presentation.utils.screens.ScreenParams.TASK_ID
import com.app.todolist.presentation.utils.screens.ScreenRoutes
import com.app.todolist.ui.theme.TodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            TodoListTheme {
                // Creates a NavHostController that handles the adding of the ComposeNavigator and DialogNavigator.
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier
                        .statusBarsPadding()
                        .background(Color.White),
                    navController = navController,
                    startDestination = ScreenRoutes.SplashScreen.route // setting the splash screen as first destination
                ) {
                    // adding SplashScreen as Composable
                    composable(route = ScreenRoutes.SplashScreen.route) {
                        SplashScreen(navController = navController)
                    }
                    // adding TaskListScreen as Composable

                    composable(route = ScreenRoutes.TaskListScreen.route) {
                        TodoListScreen(navController = navController)
                    }
                    // adding AddEditTaskScreen as Composable
                    // and passing Task_ID as parameter
                    composable(
                        route = ScreenRoutes.AddEditTaskScreen.route +
                                "?${TASK_ID}={${TASK_ID}}",
                        arguments = listOf(
                            navArgument(
                                name = TASK_ID
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val taskId = it.arguments?.getInt(TASK_ID) ?: -1
                        AddEditTodoScreen(
                            navController = navController,
                            taskId = taskId
                        )
                    }
                    // adding SettingsScreen as Composable
                    composable(route = ScreenRoutes.Settings.route) {
                        SettingsScreen(navController = navController)
                    }

                }
            }
        }
    }
}