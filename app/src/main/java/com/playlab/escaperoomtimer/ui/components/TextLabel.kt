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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = MaterialTheme.colors.onSurface.copy(.6f),
    fontSize: TextUnit = dimensionResource(id = R.dimen.text_label_font_size).value.sp
) {
    Text(
        modifier =modifier,
        fontSize = fontSize,
        text = text,
        maxLines = 1,
        color = textColor,
        style = textStyle
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextLabel() {
    EscapeRoomTimerTheme() {
        Surface() {
            TextLabel(
                text = "Timer"
            )
        }
    }
}