package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun KeypadButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    color: Color = MaterialTheme.colors.onSurface,
    size: Dp = dimensionResource(id = R.dimen.keypad_button_size),
    fontSize: TextUnit = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textStyle: TextStyle = MaterialTheme.typography.button,
    shape: CornerBasedShape = MaterialTheme.shapes.large,
    onClick: (String) -> Unit = {}
) {
    Button(
        modifier = modifier
            .size(size)
            .clip(shape),
        onClick = { onClick( buttonText ) }
    ) {
        Box(
            modifier  = Modifier.fillMaxSize(),
            Alignment.Center
        ){
            Text(
                text = buttonText,
                color = color,
                style = textStyle,
                textAlign = TextAlign.Center,
                fontSize = fontSize,
                fontWeight = fontWeight,
                maxLines = 1
            )
        }

    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    color: Color = MaterialTheme.colors.onSurface,
    fontSize: TextUnit = dimensionResource(id = R.dimen.action_button_font_size).value.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textStyle: TextStyle = MaterialTheme.typography.button,
    paddingValues: PaddingValues = 
        PaddingValues(
            horizontal = dimensionResource(id = R.dimen.action_button_horizontal_padding),
            vertical = dimensionResource(id = R.dimen.action_button_vertical_padding)
        ),
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.clip(shape),
        onClick =  onClick
    ) {
        Text(
            modifier = Modifier.padding(paddingValues),
            text = buttonText,
            color = color,
            style = textStyle,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewActionButton() {
    EscapeRoomTimerTheme() {
        Surface (){
            ActionButton(Modifier.padding(20.dp), buttonText = "START")
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewKeypadButton() {
    EscapeRoomTimerTheme() {
        Surface (){
            val buttonSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.keypad_button_size)}
            KeypadButton(
                size = buttonSize,
                modifier = Modifier.padding(20.dp),
                buttonText = "1"
            )
        }
    }
}
