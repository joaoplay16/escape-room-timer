package com.playlab.escaperoomtimer.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.ui.DevicesPreviews
import com.playlab.escaperoomtimer.ui.components.BigSecretCodeInput
import com.playlab.escaperoomtimer.ui.components.CountDownTimer
import com.playlab.escaperoomtimer.ui.components.Keypad
import com.playlab.escaperoomtimer.ui.components.TextLabel
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    timeUntilFinish: Long = 0,
    timerText: String = "00:00:00",
    code: String = "",
    isDefused: Boolean = false,
    onCodeChange: (String) -> Unit = {},
    onKeypadDelete: () -> Unit ={},
    onKeypadOk: () -> Unit ={}
) {
    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            onSettingsClick()
                        },
                    imageVector = Icons.Default.Settings,
                    contentDescription = ""
                )
            }
        }
    ) { paddingValues ->

        val orientation = LocalConfiguration.current.orientation

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top =  dimensionResource(id = R.dimen.big_timer_top_padding),
                        bottom = dimensionResource(id = R.dimen.big_timer_bottom_padding)
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CountDownTimer(
                    time = timerText,
                    color = if (timeUntilFinish <= 10_000L) Color.Red else MaterialTheme.colors.onPrimary
                )

                BigSecretCodeInput(
                    text = code.replace(".".toRegex(), "*"),
                    readOnly = true,
                    maxLength = 8
                )

                if(isDefused){
                    TextLabel(
                        text = "Defused",
                        fontSize = dimensionResource(id = R.dimen.is_defused_text_font_size).value.sp,
                        textColor = MaterialTheme.colors.onPrimary,
                        textStyle = MaterialTheme.typography.h1
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.keypad_row_gap)))

                if (orientation == Configuration.ORIENTATION_PORTRAIT){

                    Keypad(
                        onDigitClick = { digit ->
                            onCodeChange(digit)
                        },
                        onOkClick = onKeypadOk,
                        onDeleteClick = onKeypadDelete
                    )
                }
            }

            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top =  dimensionResource(id = R.dimen.big_timer_top_padding))
                        .weight(1f),
                    verticalArrangement = Arrangement.Top,
                    ) {
                    Keypad(
                        onDigitClick = { digit ->
                            onCodeChange(digit)
                        },
                        onOkClick = onKeypadOk,
                        onDeleteClick = onKeypadDelete
                    )
                }
            }
        }

    }
}

@DevicesPreviews
@Composable
fun PreviewHomeScreen() {
    EscapeRoomTimerTheme() {
        Surface {
            HomeScreen(isDefused = true)
        }
    }
}