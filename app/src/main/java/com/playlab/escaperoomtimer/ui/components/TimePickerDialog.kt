package com.playlab.escaperoomtimer.ui.components

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onTimeSelected: (Int, Int) -> Unit,
    initialHour: Int = 0,
    initialMinute: Int = 0,
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
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { context ->

                        val timePickerLayout = LayoutInflater
                            .from(context)
                            .inflate(R.layout.timer_picker_dialog, null, false)
                                as TimePicker

                       timePickerLayout.setIs24HourView(true)
                       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                           timePickerLayout.hour = initialHour
                           timePickerLayout.minute = initialMinute
                       }else{
                           timePickerLayout.currentHour = initialHour
                           timePickerLayout.currentMinute = initialMinute
                       }

                       timePickerLayout.setOnTimeChangedListener { _, hour, minute ->
                          onTimeSelected(hour, minute)
                       }

                        timePickerLayout
                    })
            }
        }
    }


}


@Preview
@Composable
fun PreviewSettingsScreen() {
    EscapeRoomTimerTheme() {
        Surface {
            TimePickerDialog(
                onTimeSelected = { hour, minute ->
                    Log.d("TIMER", "$hour:$minute")
                }
            )
        }
    }
}