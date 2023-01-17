package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                fontSize = dimensionResource(id = R.dimen.dialog_title_font_size).value.sp
            ) },
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
                            .padding(top = 10.dp, bottom = 20.dp, start = 10.dp, end = 35.dp)
                            .clickable { onCancelClick() },
                        text = stringResource(id = R.string.stop_timer_dialog_no_button),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1,
                        fontSize = dimensionResource(id = R.dimen.dialog_button_font_size).value.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 20.dp, start = 10.dp, end = 35.dp)
                            .clickable { onOkClick() },
                        text = stringResource(id = R.string.stop_timer_dialog_yes_button),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1,
                        fontSize = dimensionResource(id = R.dimen.dialog_button_font_size).value.sp
                    )
            }

        },
        onDismissRequest = onDismiss,
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTimerDialog() {

    EscapeRoomTimerTheme(true) {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                
                TimerDialog(
                    title = "This may cause the unexpected use of the original definition of boolean"
                )
            }
        }
    }
}