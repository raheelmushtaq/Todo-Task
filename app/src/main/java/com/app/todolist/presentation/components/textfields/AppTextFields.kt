package com.app.todolist.presentation.components.textfields

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun SmallText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 1,
    isError: Boolean = false
) {
    Text(
        text = text,
        color = if (isError) Color.Red else Color.Black,
        fontSize = 13.sp,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier,
        maxLines = maxLines
    )
}

@Composable
fun RegularText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        maxLines = maxLines,
        color = color,
        fontSize = 15.sp,
        fontWeight = fontWeight,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier
    )
}

@Composable
fun MediumText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        maxLines = maxLines,
        color = color,
        fontSize = 17.sp,
        fontWeight = fontWeight,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier
    )
}

@Composable
fun LargeText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        color = color,
        fontSize = 19.sp,
        maxLines = maxLines,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        textAlign = align
    )
}

@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontSize = 24.sp,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        textAlign = align
    )
}