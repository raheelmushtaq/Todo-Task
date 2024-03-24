package com.app.todolist.presentation.screens.splash

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.presentation.screens.splash.state_event.SplashUIEvent
import com.app.todolist.presentation.screens.splash.viewmodel.SplashViewModel
import com.app.todolist.presentation.utils.screens.ScreenRoutes
import com.app.todolist.utils.PermissionUtil
import kotlinx.coroutines.flow.collectLatest

//
@Composable
fun SplashScreen(
    navController: NavController, viewModel: SplashViewModel = hiltViewModel()
) {
    // getting the context
    val context = LocalContext.current

    // fetching the current value of the data state. when the datastate is update
    val uiData = viewModel.dataState.value


    // ath the first launch of the application
    LaunchedEffect(key1 = "true", block = {

        //checking if the permission is granter or not if not granted then requesting permission to be shown to user
        runWithNotificationPermissionCheck(context) {
            // getting tasks from viewmodel
            viewModel.fetchTasks()
        }
        // here we are listening to any action which is for user to see from viewmodel i.e. success of fetching tasks and category or error for network failure
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                // if event is success
                is SplashUIEvent.Success -> {
                    // navigate user to Task list Screen and making when user presses the back button, removing splash as well.
                    navController.navigate(ScreenRoutes.TaskListScreen.route) {
                        launchSingleTop = true
                        popUpTo(ScreenRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
                // if event error is occurred
                is SplashUIEvent.Error -> {
//                    show toast to user in case of data is not fetched
                    Toast.makeText(
                        context, context.getString(event.message), Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    })


    //Scaffold implements the basic material design visual layout structure.
    //This component provides API to put together several material components to construct your screen
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValue ->
        //create the base Box with the padding provided by scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValue)
        ) {
//            creating a column to show logo and other information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // adding image to UI
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                )

            }
            // at the bottom of the screen, if loading state is true than show loader, and if error is true than show the error
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // in case of loading show loader
                if (uiData.isLoading) {
                    Spacer(modifier = Modifier.height(32.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
                if (uiData.isError) {
                    // in case of Error show Retry Button and hide Loader  show loader

                    Button(onClick = { viewModel.fetchTasks() }) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                }
            }
        }
    }

}


private fun runWithNotificationPermissionCheck(context: Context, onPermission: (() -> Unit)) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionUtil.requestPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) {
            onPermission.invoke()
        }
    } else {
        onPermission.invoke()
    }
}