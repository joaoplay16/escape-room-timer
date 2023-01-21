package com.playlab.escaperoomtimer.ui.screens.settings

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.DevicesPreviews
import com.playlab.escaperoomtimer.ui.components.*
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.SoundEffects
import com.playlab.escaperoomtimer.util.TimeUtil.getLeftPaddedNumberString
import com.playlab.escaperoomtimer.util.TimeUtil.getTimeInMillis

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    onArrowBackPressed: ()  -> Unit = {},
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
    var tickSoundEnabled by timerViewModel.tickSoundEnabled
    val isFinished = timerViewModel.isFinished()
    val isDefused = timerViewModel.isDefused()

    var timerHour by remember { mutableStateOf(0) }
    var timerMinute by remember { mutableStateOf(0) }
    var timerSecond by remember { mutableStateOf(0) }

    val defuseCodeRequiredMessage = stringResource(id = R.string.defuse_code_required_message)
    val startButtonText = stringResource(id = R.string.start_button_text).uppercase()

    val sfx = remember { SoundEffects(context) }

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
                        contentDescription = stringResource(id = R.string.arrow_back_icon_content_description)
                    )
                    TextLabel(
                        text = stringResource(id = R.string.settings_screen_title),
                        fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp)
                }

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    TextLabel(
//                        modifier = Modifier.padding(end = 10.dp),
//                        fontSize = dimensionResource(id = R.dimen.small_timer_font_size).value.sp,
//                        text = fullTimerText
//                    )
                    ActionButton(
                        buttonText = fullTimerText,
                        onClick = {
                            if (isFinished.not()) showDialog = true
                        },
                        fontSize = dimensionResource(id = R.dimen.small_timer_font_size).value.sp,
                        paddingValues = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(50.dp),
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    ) { paddingValues ->

        val onStartButtonClick:  () -> Unit = onStartButtonClick@{
            if(defuseCode.isEmpty()){
                Toast.makeText(context, defuseCodeRequiredMessage, Toast.LENGTH_LONG).show()
                return@onStartButtonClick
            }
            if(timeUntilFinish > 0) {
                showDialog = true
                return@onStartButtonClick
            }
            timerViewModel.setTimeUntilFinishInMillis(startTimeInMillis)
            timerViewModel.startTimer(
                action = {
                    if(tickSoundEnabled) sfx.playSound(R.raw.beep)
                },
                onFinish = {
                    if(isDefused.not()) sfx.playSound(R.raw.bomb_explosion)
                    timerViewModel.stopTimer()
                }
            )
            startCountDownTimer()
        }

        if (showDialog){
            TimerDialog(
                title = stringResource(id = R.string.stop_timer_message),
                onDismiss =  { showDialog = false },
                onOkClick = {
                    timerViewModel.stopTimer()
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
                    initialHour = timerHour,
                    initialMinute = timerMinute,
                    initialSecond = timerSecond,
                    modifier = Modifier.size(320.dp, 180.dp),
                    showDialog = showTimePickerDialog,
                    onDismissRequest = { showTimePickerDialog = false},
                    onTimeSelected = { hour, minute, second ->
                        timerHour = hour
                        timerMinute = minute
                        timerSecond = second

                        timerViewModel.setStartTimeInMillis(
                            getTimeInMillis(
                                hour,
                                minute,
                                second
                            )
                        )
                    })

                TextLabel( Modifier
                    .padding(vertical =labelVerticalPadding),
                    text = stringResource(id = R.string.time_picker_label)
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(
                        text = getLeftPaddedNumberString(timerHour),
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel(
                        Modifier.padding(
                            horizontal = labelHorizontalPadding),
                        text = stringResource(id = R.string.time_picker_input_hour_label))
                    TimeInput(
                        text = getLeftPaddedNumberString(timerMinute),
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel(
                        Modifier.padding(
                            horizontal = labelHorizontalPadding),
                        text = stringResource(id = R.string.time_picker_input_minute_label)
                    )

                    TimeInput(
                        text = getLeftPaddedNumberString(timerSecond),
                        readOnly = true,
                        enabled = false,
                        onClick = {
                            showTimePickerDialog = true
                        }
                    )
                    TextLabel(
                        Modifier.padding(
                            horizontal = labelHorizontalPadding),
                        text = stringResource(id = R.string.time_picker_input_second_label)
                    )
                }

                TextLabel(
                    Modifier.padding(
                        top = labelTopPadding,
                        bottom = labelBottomPadding),
                    text = stringResource(id = R.string.defuse_code_input_label))
                Row(verticalAlignment = Alignment.Bottom) {
                    SecretCodeInput(
                        text = defuseCode,
                        maxLength = 8,
                        onValueChange = {  timerViewModel.setDefuseCode(it) },
                    )
                }

                TextLabel(
                    Modifier.padding(
                        top = labelTopPadding,
                        bottom = labelBottomPadding),
                    text = stringResource(id = R.string.penalty_label)
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    TimeInput(
                        text = if(penalty > 0) penalty.toString() else "",
                        onValueChange = {
                          timerViewModel.setPenaltyTime( if( it.isNotBlank() ) it.toInt() else 0 )
                        }
                    )
                    TextLabel(
                        Modifier.padding(horizontal = labelHorizontalPadding),
                        text = stringResource(id = R.string.penalty_input_seconds_label)
                    )
                }

                Row(

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextLabel(
                        Modifier
                            .padding(
                                top = labelTopPadding,
                                bottom = labelHorizontalPadding
                            )
                            .clickable {
                                tickSoundEnabled = !tickSoundEnabled
                            },
                        text = stringResource(id = R.string.ticking_sound_checkbox_label)
                    )
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

                        ActionButton(buttonText = startButtonText, onClick = onStartButtonClick)
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
                    ActionButton(buttonText = startButtonText, onClick = onStartButtonClick)
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