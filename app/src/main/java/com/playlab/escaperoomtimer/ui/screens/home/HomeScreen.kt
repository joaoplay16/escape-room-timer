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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.DevicesPreviews
import com.playlab.escaperoomtimer.ui.components.BigSecretCodeInput
import com.playlab.escaperoomtimer.ui.components.CountDownTimer
import com.playlab.escaperoomtimer.ui.components.Keypad
import com.playlab.escaperoomtimer.ui.components.TextLabel
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.SoundEffects

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    onSettingsClick: () -> Unit = {},
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
                    contentDescription = stringResource(id = R.string.settings_icon_content_description)
                )
            }
        }
    ) { paddingValues ->

        val orientation = LocalConfiguration.current.orientation
        val context = LocalContext.current
        val sfx = remember { SoundEffects(context) }

        var isDefused by remember{ mutableStateOf(false) }
        var isFinished by remember{ mutableStateOf(false) }
        val timerText = timerViewModel.getTimeString()
        val timeUntilFinish by timerViewModel.timeUntilFinishInMillis

        val code by timerViewModel.inputCode

        val onKeypadOkClick: () -> Unit =  {
            isDefused = timerViewModel.isDefused()
            isFinished = timerViewModel.isFinished()

            if(isDefused){
                if(isFinished.not()) sfx.playSound( R.raw.bomb_has_been_defused)
                timerViewModel.stopTimer()
            }else{
                if(isFinished.not()) sfx.playSound(R.raw.error)
                timerViewModel.setInputCode("")
                timerViewModel.penalize()
            }
        }

        val onKeypadDigitClick: (String) -> Unit = { digit ->
            val c = StringBuilder(code).append(digit).toString()
            timerViewModel.setInputCode(c)
        }

        val onKeypadDeleteClick: () -> Unit = {
            timerViewModel.setInputCode(code.dropLast(1))
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top = dimensionResource(id = R.dimen.big_timer_top_padding),
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
                    placeholder = "********",
                    readOnly = true,
                    maxLength = 8
                )

                if(isDefused && isFinished.not()){
                    TextLabel(
                        text = stringResource(id = R.string.defuse_text),
                        fontSize = dimensionResource(id = R.dimen.is_defused_text_font_size).value.sp,
                        textColor = MaterialTheme.colors.onPrimary,
                        textStyle = MaterialTheme.typography.h1
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.keypad_row_gap)))

                if (orientation == Configuration.ORIENTATION_PORTRAIT){
                    Keypad(
                        onDigitClick = onKeypadDigitClick,
                        onOkClick = onKeypadOkClick,
                        onDeleteClick = onKeypadDeleteClick
                    )
                }
            }

            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Keypad(
                        onDigitClick = onKeypadDigitClick,
                        onOkClick = onKeypadOkClick,
                        onDeleteClick = onKeypadDeleteClick
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
            HomeScreen(timerViewModel = TimerViewModel())
        }
    }
}