package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    onDigitClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onOkClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.width(300.dp).height(300.dp),
        verticalArrangement =  Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                buttonText = "1",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "2",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "3",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                buttonText = "4",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "5",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "6",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                buttonText = "7",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "8",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
                buttonText = "9",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            KeypadButton(
                buttonText = "<",
                onClick = { onDeleteClick() }
            )
            KeypadButton(
                buttonText = "0",
                onClick = { buttonValue -> onDigitClick(buttonValue)}
            )
            KeypadButton(
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