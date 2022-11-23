package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.TimeUtil.getLeftPaddedNumberString

@Composable
fun TimeSpinner(
    modifier: Modifier = Modifier,
    timeRange: IntRange = 0 .. 59,
    expanded: Boolean ,
    onExpand: (Boolean) -> Unit,
    selectedValue:  Int?,
    onValueSelected: (Int) -> Unit
) {

    Card {
        TimeInput(
            onClick =  {
                onExpand(!expanded)
            },
            text = if (selectedValue == null) "" else getLeftPaddedNumberString(selectedValue),
            placeholder = "00",
            readOnly = true,
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpand(false) },
            modifier = modifier.fillMaxHeight(0.38f)
        ) {
            timeRange.forEach { entry ->
                DropdownMenuItem(
                    onClick = {
                        onValueSelected(entry)
                        onExpand(false)
                    }
                ){
                    Text(
                        text = getLeftPaddedNumberString(entry),
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTimerSpinner() {
    EscapeRoomTimerTheme() {
        Surface {
            var currentValue by remember { mutableStateOf<Int?>(null) }
            var expanded by remember { mutableStateOf(false) } // initial value

            TimeSpinner(
                timeRange = 0 .. 59,
                expanded = expanded,
                onExpand = { expanded = it},
                selectedValue = currentValue,
                onValueSelected = { currentValue = it},
            )
        }
    }
}