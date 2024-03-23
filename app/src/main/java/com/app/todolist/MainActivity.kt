package com.app.todolist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import com.app.todolist.ui.theme.TodoListTheme
import com.app.todolist.presentation.utils.screens.ScreenParams.TASK_ID
import com.app.todolist.presentation.utils.screens.ScreenRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            TodoListTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier
                        .statusBarsPadding()
                        .background(Color.White),
                    navController = navController,
                    startDestination = ScreenRoutes.SplashScreen.route
                ) {
                    composable(route = ScreenRoutes.SplashScreen.route) {
                        SplashScreen(navController = navController)
                    }

                    composable(route = ScreenRoutes.TaskListScreen.route) {
                        TodoListScreen(navController = navController)
                    }
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
                    composable(route = ScreenRoutes.Settings.route) {
                        SettingsScreen(navController = navController)
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoListTheme {
        Greeting("Android")
    }
}