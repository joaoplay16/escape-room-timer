package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun RatingDialog(
    modifier: Modifier = Modifier,
    title: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onOkClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDismiss: () -> Unit = {},

) {
    AlertDialog(
        title = {
            Text(
                style = MaterialTheme.typography.h6,
                text = title
            )
        },
        text = {
            Text(
            modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6,
                text = "\uD83D\uDCA3 \uD83D\uDCA3 \uD83D\uDCA3 \uD83D\uDCA3 \uD83D\uDCA3 ",
                textAlign = TextAlign.Center
            )
        },
        buttons = {
            Row(
                modifier = modifier
                    .padding(top = 0.dp),
                verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, bottom = 15.dp, start = 25.dp, end = 15.dp)
                            .clickable { onCancelClick() },
                        text = negativeButtonText,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, bottom = 15.dp, start = 10.dp, end = 25.dp)
                            .clickable { onOkClick() },
                        text = positiveButtonText,
                        color = MaterialTheme.colors.onPrimary.copy(0.9f),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.End
                    )
            }

        },
        onDismissRequest = onDismiss,
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewRatingDialog() {

    EscapeRoomTimerTheme(true) {
        Surface {
            RatingDialog(
                title = stringResource(id = R.string.rating_dialog_title),
                modifier = Modifier.fillMaxWidth(),
                negativeButtonText = stringResource(id = R.string.rating_dialog_negative_button),
                positiveButtonText = stringResource(id = R.string.rating_dialog_positive_button)

            )
        }
    }
}