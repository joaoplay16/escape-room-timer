package com.playlab.escaperoomtimer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
                        timerViewModel = timerViewModel,
                        preferencesDataStore = preferencesDataStore
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ScreenRoutes.Home.name,
    timerViewModel: TimerViewModel,
    preferencesDataStore: PreferencesDataStore
) {

    val isFinished = timerViewModel.isFinished()
    var dialogDismissed by remember { mutableStateOf( false ) }
    var showRatingDialog by remember { mutableStateOf( false ) }
    var isRateButtonClicked by remember { mutableStateOf( false ) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
                && isFinished ){

                RatingDialog(
                    title = stringResource(id = R.string.rating_dialog_title),
                    negativeButtonText = stringResource(id = R.string.rating_dialog_negative_button),
                    positiveButtonText = stringResource(id = R.string.rating_dialog_positive_button),
                    onDismiss = {
                        showRatingDialog = false
                        dialogDismissed = true
                    },
                    onOkClick = {
                        coroutineScope.launch {
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
                            context.startActivity(marketIntent)
                        }catch (e: ActivityNotFoundException){
                            context.startActivity(browserIntent)
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
                onSettingsClick = {
                    navController.navigate(ScreenRoutes.Settings.name)
                },
            )
        }

        composable(ScreenRoutes.Settings.name){
            SettingsScreen(
                timerViewModel = timerViewModel,
                startCountDownTimer = {
                    navController.popBackStack()
                },
                onArrowBackPressed = { navController.popBackStack() },
            )
        }
    }
}


