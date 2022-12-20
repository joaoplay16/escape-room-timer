package com.example.testcompose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun DefaultTimerPicker(
    modifier: Modifier = Modifier,
    startHour: Int = 0,
    startMinute: Int = 0,
    startSecond: Int = 0,
    size: DpSize = DpSize(228.dp, 128.dp),
    textStyle: TextStyle = MaterialTheme.typography.body1,
    onTimeSelected: (hour: Int, minute: Int, second: Int) -> Unit,
) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0 ) }
    var selectedSecond by remember { mutableStateOf( 0 ) }

    Box(contentAlignment = Alignment.Center){
        Surface(
            modifier = Modifier.size(size.width,size.height / 3),
            shape = RoundedCornerShape(20.dp),
            color = Color(0x4E34851D),
            border = BorderStroke(2.dp, Color(0x5058C394))
        ) {}
        Row(
            modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelTimePicker(
                count = 24,
                startIndex = startHour,
                onScrollFinished = {
                    selectedHour = it
                    onTimeSelected(it, selectedMinute, selectedSecond)
                }
            ) { index ->
                Text(
                    style = textStyle,
                    text = index.toString().padStart(2, '0')
                )
            }

            Text(
                style = textStyle,
                text = ":"
            )

            WheelTimePicker(
                count = 60,
                startIndex = startMinute,
                onScrollFinished = {
                    selectedMinute = it
                    onTimeSelected(selectedHour, it, selectedSecond)
                }
            ) { index ->
                Text(
                    style = textStyle,
                    text = index.toString().padStart(2, '0')
                )
            }

            Text(
                style = textStyle,
                text = ":"
            )

            WheelTimePicker(
                count = 60,
                startIndex = startSecond,
                onScrollFinished = {
                    selectedSecond = it
                    onTimeSelected(selectedHour, selectedMinute, it)
                }
            ) { index ->
                Text(
                    style = textStyle,
                    text = index.toString().padStart(2, '0')
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun PreviewDefaultTimerPicker(){
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DefaultTimerPicker(
                startHour = 12,
                startMinute = 30,
                startSecond = 47,
                textStyle = TextStyle(fontSize = 24.sp),
                onTimeSelected = { hour, minute, second ->
                    Log.d("FLING", "$hour:$minute:$second")
                }
            )
        }
    }
}