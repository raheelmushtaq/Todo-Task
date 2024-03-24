package com.app.todolist.presentation.components.appheader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.todolist.R
import com.app.todolist.presentation.utils.screens.ScreenRoutes

/*
* AppHeader is the header composable which is used for app header.
* It take different paramters that needs to be shown in the header
* title for header title
* navcontroller for handling any navigation action
* showBackButton, to be shown if we want to add the navigation functioanality
* showSettingsIcon, for showing settings icon on the right side.
* Some Modifier for showing handling the UI
* */

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    navController: NavController,
    showBackButton: Boolean = false,
    title: String = "",
    showSettingsIcon: Boolean = false
) {
    // this box is the parent box. which uses the modifier
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        // a column composable to show views vertically
        Column(modifier = Modifier.fillMaxWidth()) {

            // a row composeable to show view horizontally
            Row(
                modifier = modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // show back button if this is true else add a little margin
                if (showBackButton) {
                    // Back btton
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .rotate(180f)
                            .padding(3.dp)
                            .clickable {
                                // when back button is clicked go back 1 screen
                                navController.navigateUp()
                            },
                        contentDescription = stringResource(
                            id = R.string.back
                        ),
                    )
                    // add a genaring space with the title
                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    // show a little space from left
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                            .height(30.dp)
                    )
                }
                // set title of the header
                Text(
                    text = title,
                    modifier = titleModifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )
                // show settings icon if this is true

                if (showSettingsIcon) {
                    // settings icon
                    Image(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable {
                                // when settings icon is pressed, navigation to the settings screen
                                navController.navigate(ScreenRoutes.Settings.route)
                            }
                            .padding(5.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                        contentDescription = "settings"
                    )
                }

            }
            // add space of 5 height
            Spacer(modifier = Modifier.width(5.dp))
            //show a horizontab bar with height of 1dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview
@Composable

fun HeaderPreview() {
}