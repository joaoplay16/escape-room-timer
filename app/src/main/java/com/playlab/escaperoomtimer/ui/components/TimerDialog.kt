package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun TimerDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    onOkClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDismiss: () -> Unit = {},

) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
            ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 15.dp, start = 10.dp, end = 15.dp)
                            .clickable { onCancelClick() },
                        text = stringResource(id = R.string.stop_timer_dialog_no_button),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 15.dp, start = 10.dp, end = 20.dp)
                            .clickable { onOkClick() },
                        text = stringResource(id = R.string.stop_timer_dialog_yes_button),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.overline
                    )
            }

        },
        onDismissRequest = onDismiss,
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTimerDialog() {

    EscapeRoomTimerTheme() {
        Surface {
            TimerDialog(modifier = Modifier.fillMaxWidth(0.8f))
        }
    }
}