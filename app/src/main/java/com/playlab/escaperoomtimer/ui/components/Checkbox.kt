package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun SettingsCheckBox(
    modifier: Modifier = Modifier,
    scale: Float = 1.3f,
    checked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit = {},
) {
    Checkbox(
        modifier = modifier.scale(scale),
        checked = checked,
        onCheckedChange = {
            onCheckChanged(it)
        })
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewCheckBox() {

    var checked by remember { mutableStateOf(false) }

    EscapeRoomTimerTheme {
        Surface {
            SettingsCheckBox(
                checked = checked,
                onCheckChanged = {
                    checked = it
                }
            )
        }
    }
}