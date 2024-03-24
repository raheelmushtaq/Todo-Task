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

// All the components in this class are the used to showing text of different sizes
// these classes are used here to show a generic and fixed font size that is displayed in the whole application

/*SmallText is used to show  text with  font size 13
* it takes text as input to show
* and color to set color of the text, by default its black.
* font weight to show font bold or normal
* max Lines this Text will have. */
@Composable
fun SmallText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    maxLines: Int = Int.MAX_VALUE,
    align: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        color = color,
        fontSize = 13.sp,
        fontWeight = fontWeight,
        fontFamily = FontFamily.SansSerif,
        modifier = modifier,
        maxLines = maxLines,
        textAlign = align
    )
}


/*RegularText is used to show  text with  font size 15
* it takes text as input to show
* and color to set color of the text, by default its black.
* font weight to show font bold or normal
* max Lines this Text will have. */
@Composable
fun RegularText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    align: TextAlign = TextAlign.Start,
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
        modifier = modifier,
        textAlign = align

    )
}

/*MediumText is used to show  text with  font size 17
* it takes text as input to show
* and color to set color of the text, by default its black.
* font weight to show font bold or normal
* max Lines this Text will have. */
@Composable
fun MediumText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    align: TextAlign = TextAlign.Start,
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
        modifier = modifier,
        textAlign = align

    )
}

/*LargeText is used to show  text with  font size 19
* it takes text as input to show
* and color to set color of the text, by default its black.
* font weight to show font bold or normal
* max Lines this Text will have. */
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


/*HeadingText is used to show  text with  font size 24
* it takes text as input to show
* and color to set color of the text, by default its black.
* font weight to show font bold or normal
* max Lines this Text will have. */
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