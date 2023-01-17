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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.animations.RatingAnimation
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun RatingDialog(
    modifier: Modifier = Modifier,
    title: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,

) {
    AlertDialog(
        title = {
            Text(
                style = MaterialTheme.typography.h6,
                text = title,
                fontSize = dimensionResource(id = R.dimen.dialog_title_font_size).value.sp
            )
        },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                RatingAnimation(travelDistance = 14.dp)
            }
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
                            .padding( bottom = 15.dp, start = 25.dp, end = 15.dp)
                            .clickable { onCancelClick() },
                        text = negativeButtonText,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.body2,
                        fontSize = dimensionResource(id = R.dimen.dialog_button_font_size).value.sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding( bottom = 15.dp, start = 10.dp, end = 25.dp)
                            .clickable { onOkClick() },
                        text = positiveButtonText,
                        color = MaterialTheme.colors.onPrimary.copy(0.9f),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.End,
                        fontSize = dimensionResource(id = R.dimen.dialog_button_font_size).value.sp,
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
                positiveButtonText = stringResource(id = R.string.rating_dialog_positive_button),
                onDismiss = { },
                onOkClick = { },
                onCancelClick = { }
            )
        }
    }
}