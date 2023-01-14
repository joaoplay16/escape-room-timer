package com.playlab.escaperoomtimer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playlab.escaperoomtimer.ui.components.RatingDialog
import com.playlab.escaperoomtimer.ui.data.preferences.PreferencesDataStore
import com.playlab.escaperoomtimer.ui.screens.ScreenRoutes
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.screens.home.HomeScreen
import com.playlab.escaperoomtimer.ui.screens.settings.SettingsScreen
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.SoundEffects
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull


class MainActivity : ComponentActivity() {

    var timer: CountDownTimer? = null

    val timerViewModel: TimerViewModel by viewModels()

    lateinit var preferencesDataStore: PreferencesDataStore

    lateinit var soundEffects: SoundEffects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesDataStore = PreferencesDataStore(this)
        soundEffects = SoundEffects(this)

        CoroutineScope(Dispatchers.Main).launch{
            val appOpensCount = preferencesDataStore.appOpensCount.firstOrNull()
            appOpensCount?.let {
                preferencesDataStore.setAppOpensCount(appOpensCount + 1)
            }
        }

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

                    if (tickSoundEnabled)  soundEffects.playSound(R.raw.beep)

                    timerViewModel.setTimeUntilFinishInMillis(millisUntilFinished)
                }

                override fun onFinish() {
                    timerViewModel.resetTimer()
                    timerViewModel.setStartTimeInMillis(0L)
                    soundEffects.playSound(R.raw.bomb_explosion)
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

    @Composable
    fun DefaultNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = ScreenRoutes.Home.name,
        timerViewModel: TimerViewModel
    ) {

        val isDefused = timerViewModel.isDefused()
        val timeUntilFinish by timerViewModel.timeUntilFinishInMillis
        val penalty by timerViewModel.penalty

        var dialogDismissed by remember { mutableStateOf( false ) }
        var showRatingDialog by remember { mutableStateOf( false ) }
        var isRateButtonClicked by remember { mutableStateOf( false ) }

        val context = LocalContext.current
        val sfx = remember { SoundEffects(context) }

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ){
            composable(ScreenRoutes.Home.name){

                LaunchedEffect(key1 =  null) {
                    val appOpensCount = preferencesDataStore.appOpensCount.first()
                    isRateButtonClicked = preferencesDataStore.isRateButtonClicked.first()
                    delay(2000)
                    if(appOpensCount % 2 != 0) showRatingDialog = true
                }

                if( showRatingDialog
                    && dialogDismissed.not()
                    && isRateButtonClicked.not()
                    && timeUntilFinish == 0L ){

                    RatingDialog(
                        title = stringResource(id = R.string.rating_dialog_title),
                        negativeButtonText = stringResource(id = R.string.rating_dialog_negative_button),
                        positiveButtonText = stringResource(id = R.string.rating_dialog_positive_button),
                        onDismiss = {
                            showRatingDialog = false
                            dialogDismissed = true
                        },
                        onOkClick = {

                            CoroutineScope(Dispatchers.IO).launch {
                                preferencesDataStore.setRateButtonClicked(true)
                            }

                            showRatingDialog = false
                            dialogDismissed = true

                            val appPackageName =  BuildConfig.APPLICATION_ID
                            val marketIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=${appPackageName}")
                            )
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${appPackageName}")
                            )

                            try {
                                startActivity(marketIntent)
                            }catch (e: ActivityNotFoundException){
                                startActivity(browserIntent)
                            }
                        },
                        onCancelClick = {
                            showRatingDialog = false
                            dialogDismissed = true
                        }
                    )
                }

                HomeScreen(
                    timerViewModel = timerViewModel,
                    onKeypadOk = {
                        if(timeUntilFinish > 0){
                            if(isDefused){
                                stopTimer()
                                timerViewModel.resetTimer()
                                timerViewModel.setStartTimeInMillis(0)
                                sfx.playSound( R.raw.bomb_has_been_defused)
                            }else{
                                sfx.playSound(R.raw.error)
                                stopTimer()
                                timerViewModel.setInputCode("")

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
                SettingsScreen(
                    timerViewModel = timerViewModel,
                    startCountDownTimer = {
                        startTimer(timerViewModel)
                        navController.popBackStack()
                    },
                    onArrowBackPressed = { navController.popBackStack() },
                    onStopTimerClick = {
                        stopTimer()
                    }
                )
            }
        }
    }
}



