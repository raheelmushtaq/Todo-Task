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

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    navController: NavController,
    showBackButton: Boolean = false,
    title: String = "",
    showSettingsIcon:Boolean=false
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (showBackButton) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .rotate(180f)
                            .padding(3.dp)
                            .clickable {
                                navController.navigateUp()
                            },
                        contentDescription = stringResource(
                            id = R.string.back
                        ),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                if (!showBackButton) {
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                            .height(30.dp)
                    )
                }
                Text(
                    text = title,
                    modifier = titleModifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )

                if(showSettingsIcon){
                    Image(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable {
                                navController.navigate(ScreenRoutes.Settings.route)
                            },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                        contentDescription = "settings"
                    )
                }

            }
            Spacer(modifier = Modifier.width(5.dp))
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