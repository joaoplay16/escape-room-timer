package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    onDigitClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onOkClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement =  Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "1",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "2",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(

                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "3",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }
        Spacer(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.keypad_row_gap)))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "4",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "5",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "6",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }
        Spacer(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.keypad_row_gap)))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "7",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "8",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "9",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }
        Spacer(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.keypad_row_gap)))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "<",
                onClick = { onDeleteClick() }
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_font_size).value.sp,
                buttonText = "0",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.keypad_column_gap)))
            KeypadButton(
                fontSize = dimensionResource(id = R.dimen.keypad_button_ok_font_size).value.sp,
                buttonText = "OK",
                onClick = { onOkClick() }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewKeypad() {
    EscapeRoomTimerTheme() {
        Surface {
            Keypad()
        }
    }
}