package com.playlab.escaperoomtimer.ui.components

import android.util.Log
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onTimeSelected: (hour: Int, minute: Int, second: Int) -> Unit,
    initialHour: Int = 0,
    initialMinute: Int = 0,
    initialSecond: Int = 0,
) {

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
        ) {
            Card(
                modifier = modifier,
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 4.dp
            ) {
              DefaultTimerPicker(
                  startHour = initialHour,
                  startMinute = initialMinute,
                  startSecond = initialSecond,
                  onTimeSelected = onTimeSelected
              )
            }
        }
    }


}


@Preview
@Composable
fun PreviewSettingsScreen() {
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface {
            TimePickerDialog(
                showDialog = true,
                initialHour =  12,
                onTimeSelected = { hour, minute, second ->
                    Log.d("TIMER", "$hour:$minute:$second")
                }
            )
        }
    }
}