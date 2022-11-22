package com.playlab.escaperoomtimer

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playlab.escaperoomtimer.ui.components.ActionButton
import com.playlab.escaperoomtimer.ui.components.TextLabel
import com.playlab.escaperoomtimer.ui.screens.ScreenRoutes
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.screens.home.HomeScreen
import com.playlab.escaperoomtimer.ui.screens.settings.SettingsScreen
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.TimeUtil.getLeftPaddedNumberString
import com.playlab.escaperoomtimer.util.TimeUtil.getTimeInMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.floor

class MainActivity : ComponentActivity() {

    var timer: CountDownTimer? = null

    val timerViewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EscapeRoomTimerTheme (darkTheme = true){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    DefaultNavHost(
                        timerViewModel = timerViewModel
                    )
                }
            }
        }

    }

    private fun startTimer(timerViewModel: TimerViewModel){

        val timeUntilFinishInMillis by timerViewModel.timeUntilFinishInMillis
        val startTimeInMillis by timerViewModel.startTimeInMillis
        val tickSoundEnabled by timerViewModel.tickSoundEnabled
        if(startTimeInMillis > 0) {
            timer = object : CountDownTimer(timeUntilFinishInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    if (tickSoundEnabled) playSound(R.raw.beep)

                    val timerHour =
                        floor((millisUntilFinished.toDouble() / (1000 * 60 * 60)) % 24).toLong()
                    val timerMinute =
                        floor((millisUntilFinished.toDouble() / (1000 * 60)) % 60).toLong()
                    val timerSecond = floor((millisUntilFinished.toDouble() / 1000) % 60).toLong()

                    timerViewModel.setTimeHour(timerHour)
                    timerViewModel.setTimeMinute(timerMinute)
                    timerViewModel.setTimeSecond(timerSecond)

                    timerViewModel.setTimeUntilFinishInMillis(millisUntilFinished)
                }

                override fun onFinish() {
                    timerViewModel.resetTimer()
                    timerViewModel.setStartTimeInMillis(0L)
                    playSound(R.raw.bomb_explosion)
                    cancel()
                }
            }.start()
        }
    }

    private fun stopTimer(){
        timer?.cancel()
        timer = null
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
    }

    override fun onResume() {
        super.onResume()
        startTimer(timerViewModel)
    }

    fun playSound(resId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val mp = MediaPlayer()

            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mp.setDataSource(this@MainActivity.resources.openRawResourceFd(resId))
                }
                mp.prepare()
                mp.start()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    @Composable
    fun DefaultNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = ScreenRoutes.Home.name,
        timerViewModel: TimerViewModel = viewModel()
    ) {


        var isDefused by remember{ mutableStateOf(false) }
        val defuseCode by timerViewModel.defuseCode
        val tickSoundEnabled by timerViewModel.tickSoundEnabled
        val timeString = timerViewModel.getTimeString()
        val timeUntilFinish by timerViewModel.timeUntilFinishInMillis
        val startTimeInMillis by timerViewModel.startTimeInMillis
        val penalty by timerViewModel.penalty

        val code by timerViewModel.code

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ){
            composable(ScreenRoutes.Home.name){
                HomeScreen(
                    timerText = timeString,
                    timeUntilFinish = timeUntilFinish,
                    isDefused = isDefused,
                    code = code,
                    onCodeChange = { digit ->
                        val c = StringBuilder(code).append(digit).toString()
                        timerViewModel.setCode(c)
                                   },
                    onKeypadDelete = {
                        timerViewModel.setCode(code.dropLast(1))
                    },
                    onKeypadOk = {
                        val hasDefused = timerViewModel.hasDefused()
                        isDefused = hasDefused
                        if(timeUntilFinish > 0){
                            if(hasDefused){
                                stopTimer()
                                timerViewModel.resetTimer()
                                playSound(R.raw.bomb_has_been_defused)
                            }else{
                                playSound(R.raw.error)
                                stopTimer()
                                timerViewModel.setCode("")

                                var timeWithPenalty = timeUntilFinish - (penalty.ifEmpty { "0" }.toInt() * 1000L)
                                timeWithPenalty = if( timeWithPenalty >= 0) timeWithPenalty else 0
                                timerViewModel.setTimeUntilFinishInMillis(timeWithPenalty)
                                startTimer(timerViewModel)
                            }
                        }

                    },
                    onSettingsClick = {
                        navController.navigate(ScreenRoutes.Settings.name)
                    },
                )
            }

            composable(ScreenRoutes.Settings.name){
                var textTimerHour by remember { mutableStateOf("") }
                var textTimerMinute by remember { mutableStateOf("") }
                var textTimerSecond by remember { mutableStateOf("") }

                val context = LocalContext.current

                var showDialog by remember{ mutableStateOf(false) }

                SettingsScreen(
                    timerHour = getLeftPaddedNumberString(textTimerHour.ifEmpty { "0" }.toInt()),
                    timerMinute = getLeftPaddedNumberString(textTimerMinute.ifEmpty { "0" }.toInt()),
                    timerSecond = getLeftPaddedNumberString((textTimerSecond).ifEmpty { "0" }.toInt()),
                    enableTickingSound = tickSoundEnabled,
                    onEnableTickingSoundChange = { timerViewModel.setEnabledTickSound(it) },
                    penalty = penalty,
                    onPenaltyChange = {
                        timerViewModel.setPenaltyValue(it)
                    },
                    defuseCode = defuseCode,
                    onCodeChange = {

                        timerViewModel.setDefuseCode(it)
                                   },
                    onTimerChange = { hour, minute, second ->
                        textTimerHour = hour.toString()
                        textTimerMinute = minute.toString()
                        textTimerSecond = "00"

                        timerViewModel.setStartTimeInMillis(getTimeInMillis(hour, minute, second))
                    },
                    startCountDownTimer = {
                        isDefused = false
                        if(defuseCode.isEmpty()){
                            Toast.makeText(context, "Fill defuse code!", Toast.LENGTH_LONG).show()
                            return@SettingsScreen
                        }
                        if(timeUntilFinish > 0) {
                            showDialog = true
                            return@SettingsScreen
                        }
//                        stopTimer()
                        timerViewModel.setTimeUntilFinishInMillis(startTimeInMillis)
                        startTimer(timerViewModel)
                        navController.popBackStack()
                    },
                    onArrowBackPressed = { navController.popBackStack() },
                    showDialog = showDialog,
                    onDialogOkClick = {
                        timerViewModel.resetTimer()
                        stopTimer()
                        showDialog = false
                    },
                    onDialogCancelClick = { showDialog = false },
                    onDialogDismiss = { showDialog = false},
                    countDownComposable = {

                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextLabel(
                                modifier = Modifier.padding(end = 10.dp),
                                fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp,
                                text = timeString
                            )
                            ActionButton(
                                buttonText = "stop",
                                onClick = {
                                      if(timeUntilFinish > 0) showDialog = true
//                                    timerViewModel.resetTimer()
//                                    stopTimer()
                                },
                                paddingValues = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}



