package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.TypedValue
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun SettingsCheckBox(
    modifier: Modifier = Modifier,
    scale: Float = 0f,
    checked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    val typedValue = TypedValue()
    context.resources.getValue(R.dimen.check_box_scale, typedValue, true)

    val dimenScale = typedValue.float

    val checkBoxScale = if (scale > 0) scale else dimenScale

    Checkbox(
        modifier = modifier.scale(checkBoxScale),
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