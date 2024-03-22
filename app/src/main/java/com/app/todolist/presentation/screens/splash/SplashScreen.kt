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
import com.app.todolist.presentation.screens.splash.viewmodel.SplashViewModel
import com.app.todolist.presentation.screens_routes.ScreenRoutes
import com.app.todolist.utils.PermissionUtil
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    navController: NavController, viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiData = viewModel.uiData.value


    LaunchedEffect(key1 = "true", block = {

//        LanguageUtils.changeLanguage(context, viewModel.getLanguage())
        runWithNotificationPermissionCheck(context) {
            viewModel.fetchTasks()
        }
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is SplashViewModel.UIEvent.Success -> {
                    navController.navigate(ScreenRoutes.TodoListScreen.route) {
                        launchSingleTop = true
                        popUpTo(ScreenRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }

                is SplashViewModel.UIEvent.Error -> {
                    Toast.makeText(
                        context, context.getString(event.message), Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    })
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                    Button(onClick = { viewModel.fetchTasks() }) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                }
            }
        }
    }

}


private fun runWithNotificationPermissionCheck(context: Context, onPremission: (() -> Unit)) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionUtil.requestPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) {
            onPremission.invoke()
        }
    } else {
        onPremission.invoke()
    }
}