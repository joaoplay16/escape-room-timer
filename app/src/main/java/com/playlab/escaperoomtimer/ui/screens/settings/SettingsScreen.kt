package com.playlab.escaperoomtimer.ui.screens.settings

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.DevicesPreviews
import com.playlab.escaperoomtimer.ui.components.*
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.TimeUtil.getLeftPaddedNumberString
import com.playlab.escaperoomtimer.util.TimeUtil.getTimeInMillis

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    onArrowBackPressed: ()  -> Unit = {},
    onStopTimerClick: () -> Unit = {},
    startCountDownTimer: () -> Unit = {},
) {
    val labelVerticalPadding = dimensionResource(id = R.dimen.text_label_vertical_padding)
    val labelHorizontalPadding = dimensionResource(id = R.dimen.text_label_horizontal_padding)
    val labelTopPadding = dimensionResource(id = R.dimen.text_label_top_padding)
    val labelBottomPadding = dimensionResource(id = R.dimen.text_label_bottom_padding)

    val orientation = LocalConfiguration.current.orientation
    val context = LocalContext.current

    var showDialog by remember{ mutableStateOf(false) }

    val timeUntilFinish by timerViewModel.timeUntilFinishInMillis
    val startTimeInMillis by timerViewModel.startTimeInMillis
    val fullTimerText = timerViewModel.getTimeString()
    val penalty by timerViewModel.penalty
    val defuseCode by timerViewModel.defuseCode
    val tickSoundEnabled by timerViewModel.tickSoundEnabled

    var isSecondsSpinnerExpanded by remember { mutableStateOf(false) }

    var textTimerHour by remember { mutableStateOf("00") }
    var textTimerMinute by remember { mutableStateOf("00") }
    var textTimerSecond by remember { mutableStateOf("00") }

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

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextLabel(
                        modifier = Modifier.padding(end = 10.dp),
                        fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp,
                        text = fullTimerText
                    )
                    ActionButton(
                        buttonText = "stop",
                        onClick = {
                            if(timeUntilFinish > 0) showDialog = true
                        },
                        paddingValues = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    )
                }
            }
        }
    ) { paddingValues ->

        if (showDialog){
            TimerDialog(
                title = "Stop current timer?",
                onDismiss =  { showDialog = false },
                onOkClick = {
                    onStopTimerClick()
                    timerViewModel.resetTimer()
                    showDialog = false
                },
                onCancelClick =  { showDialog = false }
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
                        val second = textTimerSecond.toInt()

                       textTimerHour = getLeftPaddedNumberString(hour)
                       textTimerMinute = getLeftPaddedNumberString(minute)

                        timerViewModel.setStartTimeInMillis(
                            getTimeInMillis(
                                hour,
                                minute,
                                second
                            )
                        )
                    })

                TextLabel( Modifier.padding(vertical =labelVerticalPadding),text = "Timer")
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(
                        text = textTimerHour,
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "h")

                    TimeInput(
                        text = textTimerMinute,
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "m")

                    TimeSpinner(
                        timeRange = 0 .. 59,
                        expanded = isSecondsSpinnerExpanded,
                        onExpand = { isSecondsSpinnerExpanded = it },
                        selectedValue = textTimerSecond.toInt(),
                        onValueSelected = { second ->

                            textTimerSecond = second.toString()

                            timerViewModel
                                .setStartTimeInMillis(
                                    getTimeInMillis(
                                        textTimerHour.toInt(),
                                        textTimerMinute.toInt(),
                                        second
                                    )
                                )
                            },
                    )
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "s")
                }

                TextLabel( Modifier.padding(top = labelTopPadding, bottom = labelBottomPadding), text = "Defuse code")
                Row(verticalAlignment = Alignment.Bottom) {
                    SecretCodeInput(
                        text = defuseCode,
                        maxLength = 8,
                        onValueChange = {  timerViewModel.setDefuseCode(it) },
//                        isError = defuseCode.isEmpty()
                    )
                }

                TextLabel( Modifier.padding(top = labelTopPadding, bottom = labelBottomPadding), text = "Penalty")
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(text = penalty, onValueChange = { timerViewModel.setPenaltyTime(it) })
                    TextLabel( Modifier.padding(horizontal = labelHorizontalPadding), text = "sec")
                }

                Row(

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextLabel( Modifier.padding(top = labelTopPadding, bottom = labelHorizontalPadding), text = "Ticking sound")
                    SettingsCheckBox(

                        checked = tickSoundEnabled,
                        onCheckChanged = { timerViewModel.setEnabledTickSound(it)}
                    )
                }

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        ActionButton(buttonText = "START", onClick = {
                            if(defuseCode.isEmpty()){
                                Toast.makeText(context, "Fill defuse code!", Toast.LENGTH_LONG).show()
                                return@ActionButton
                            }
                            if(timeUntilFinish > 0) {
                                showDialog = true
                                return@ActionButton
                            }
                            timerViewModel.setTimeUntilFinishInMillis(startTimeInMillis)
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
                    ActionButton(buttonText = "START", onClick = {
                        if(defuseCode.isEmpty()){
                            Toast.makeText(context, "Fill defuse code!", Toast.LENGTH_LONG).show()
                            return@ActionButton
                        }
                        if(timeUntilFinish > 0) {
                            showDialog = true
                            return@ActionButton
                        }
                        timerViewModel.setTimeUntilFinishInMillis(startTimeInMillis)
                        startCountDownTimer()
                    })
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
            SettingsScreen(
                timerViewModel = TimerViewModel(),
            )
        }
    }
}