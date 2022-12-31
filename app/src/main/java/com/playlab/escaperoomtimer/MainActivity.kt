package com.playlab.escaperoomtimer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    var timer: CountDownTimer? = null

    val timerViewModel: TimerViewModel by viewModels()

    lateinit var preferencesDataStore: PreferencesDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesDataStore = PreferencesDataStore(this)

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

                    if (tickSoundEnabled) playSound(R.raw.beep)

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
        val context = this@MainActivity
        CoroutineScope(Dispatchers.IO).launch {
            val mp = MediaPlayer()

            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mp.setDataSource(context.resources.openRawResourceFd(resId))
                }else{
                    val uri: Uri = Uri.parse(
                        "android.resource://${context.packageName}/$resId"
                    )
                    mp.setDataSource(context, uri)
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
        timerViewModel: TimerViewModel
    ) {

        val isDefused = timerViewModel.isDefused()
        val timeUntilFinish by timerViewModel.timeUntilFinishInMillis
        val penalty by timerViewModel.penalty

        var dialogDismissed by remember { mutableStateOf( false ) }
        var showRatingDialog by remember { mutableStateOf( false ) }

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ){
            composable(ScreenRoutes.Home.name){

                val preferencesDataStore = PreferencesDataStore(LocalContext.current)

                LaunchedEffect(key1 =  timerViewModel.timeUntilFinishInMillis.value) {
                    val appOpensCount = preferencesDataStore.appOpensCount.first()
                    delay(2000)
                    if(appOpensCount % 2 != 0) showRatingDialog = true
                }

                if(showRatingDialog && dialogDismissed.not()){
                    RatingDialog(
                        title = stringResource(id = R.string.rating_dialog_title),
                        negativeButtonText = stringResource(id = R.string.rating_dialog_negative_button),
                        positiveButtonText = stringResource(id = R.string.rating_dialog_positive_button),
                        onDismiss = {
                            showRatingDialog = false
                            dialogDismissed = true
                        },
                        onOkClick = { showRatingDialog = false },
                        onCancelClick = { showRatingDialog = false}
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
                                playSound(R.raw.bomb_has_been_defused)
                            }else{
                                playSound(R.raw.error)
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



