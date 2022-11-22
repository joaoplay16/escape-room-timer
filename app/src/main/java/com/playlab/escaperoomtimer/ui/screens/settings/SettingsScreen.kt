package com.playlab.escaperoomtimer.ui.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.DevicesPreviews
import com.playlab.escaperoomtimer.ui.components.*
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    timerHour: String,
    timerMinute: String,
    timerSecond: String,
    defuseCode: String,
    penalty: String,
    onArrowBackPressed: ()  -> Unit = {},
    enableTickingSound: Boolean = true,
    onEnableTickingSoundChange: (Boolean) -> Unit = {},
    onPenaltyChange: (String) -> Unit = {},
    onCodeChange: (String) -> Unit,
    onTimerChange: (Int, Int, Int) -> Unit,
    startCountDownTimer: () -> Unit = {},
    showDialog: Boolean = false,
    onDialogOkClick: () -> Unit = {},
    onDialogCancelClick: () -> Unit = {},
    onDialogDismiss: () -> Unit = {},
    countDownComposable: @Composable RowScope.() -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                onArrowBackPressed()
                            },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "BACK"
                    )
                    TextLabel(
                        text = "Settings",
                        fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp)
                }

                    countDownComposable()
            }
        }
    ) { paddingValues ->

        val labelVerticalPadding = dimensionResource(id = R.dimen.text_label_vertical_padding)
        val labelHorizontalPadding = dimensionResource(id = R.dimen.text_label_horizontal_padding)
        val labelTopPadding = dimensionResource(id = R.dimen.text_label_top_padding)
        val labelBottomPadding = dimensionResource(id = R.dimen.text_label_bottom_padding)

        val orientation = LocalConfiguration.current.orientation


        if (showDialog){
            TimerDialog(
                title = "Stop current timer?",
                onDismiss =  onDialogDismiss ,
                onOkClick = onDialogOkClick,
                onCancelClick =  onDialogCancelClick
            )
        }


        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {

                var showTimePickerDialog by remember{ mutableStateOf(false) }

                TimePickerDialog(
                    showDialog = showTimePickerDialog,
                    onDismissRequest = { showTimePickerDialog = false},
                    onTimeSelected = { hour, minute ->
                        onTimerChange(hour, minute, 0)
                    })

                TextLabel( Modifier.padding(vertical =labelVerticalPadding),text = "Timer")
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(
                        text = timerHour,
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "h")
                    TimeInput(
                        text = timerMinute,
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "m")
                    TimeInput(
                        text = timerSecond,
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "s")
                }

                TextLabel( Modifier.padding(top = labelTopPadding, bottom = labelBottomPadding), text = "Defuse code")
                Row(verticalAlignment = Alignment.Bottom) {
                    SecretCodeInput(
                        text = defuseCode,
                        maxLength = 8,
                        onValueChange = { onCodeChange(it) },
//                        isError = defuseCode.isEmpty()
                    )
                }

                TextLabel( Modifier.padding(top = labelTopPadding, bottom = labelBottomPadding), text = "Penalty")
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(text = penalty, onValueChange = onPenaltyChange)
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "sec")
                }

                Row(
                    Modifier.padding(
                        top = labelTopPadding,
                        bottom = labelHorizontalPadding
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextLabel(text = "Ticking sound")
                    SettingsCheckBox(

                        checked = enableTickingSound,
                        onCheckChanged = onEnableTickingSoundChange
                    )
                }

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        ActionButton(buttonText = "START", onClick = {

                            startCountDownTimer()
                        })
                    }
                }
            }

            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 90.dp)
                        .weight(1f),

                ) {
                    ActionButton(buttonText = "START", onClick = startCountDownTimer)
                }
            }
        }



    }
}

@DevicesPreviews
@Composable
fun PreviewSettingsScreen() {
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface {
            var timerHour by remember { mutableStateOf("") }
            var timerMinute by remember { mutableStateOf("") }
            var timerSecond by remember { mutableStateOf("") }

            SettingsScreen(
                timerHour = timerHour,
                timerMinute = timerMinute,
                enableTickingSound = true,
                onEnableTickingSoundChange = {},
                penalty = "",
                onPenaltyChange = {},
                defuseCode = "",
                onCodeChange = { },
                timerSecond = timerSecond,
                onTimerChange = { hour, minute, secont ->
                    timerHour = hour.toString()
                    timerMinute = minute.toString()
                    timerSecond = "00"
                },
                countDownComposable = {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextLabel(
                            modifier = Modifier.padding(end = 10.dp),
                            fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp,
                            text = "00:00:00"
                        )
                        ActionButton(
                            buttonText = "stop",
                            paddingValues = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                        )
                    }

                }
            )
        }
    }
}