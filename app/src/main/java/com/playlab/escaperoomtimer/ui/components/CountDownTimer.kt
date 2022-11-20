package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun CountDownTimer(
    modifier: Modifier = Modifier,
    time: String,
    color: Color = MaterialTheme.colors.onPrimary,
    textAlign: TextAlign? = TextAlign.Center,
    fontSize: TextUnit = dimensionResource(id = R.dimen.timer_font_size).value.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal,
    textStyle: TextStyle = MaterialTheme.typography.h1,
    lineHeight: TextUnit =  TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified
    ){

    Text(
        modifier = modifier,
        text = time,
        color = color,
        style = textStyle,
        textAlign = textAlign,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = FontFamily(Font(R.font.ds_digital_normal)),
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        maxLines = 1
        )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewCountDownTimer(){
    EscapeRoomTimerTheme() {
        Surface() {
            CountDownTimer(time = "08:15:43")
        }
    }
}