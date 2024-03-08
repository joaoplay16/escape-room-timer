package com.playlab.escaperoomtimer.ui.screens.home

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.playlab.escaperoomtimer.DefaultNavHost
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.data.preferences.PreferencesDataStore
import com.playlab.escaperoomtimer.ui.screens.TimerViewModel
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import com.playlab.escaperoomtimer.util.SoundEffects
import org.junit.Rule
import org.junit.Test

class TestHomeScreen {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Test
    fun clickSettings_navigateToSettingsScreen(){
        val timerViewModel = TimerViewModel()

        composeTestRule.setContent {
            EscapeRoomTimerTheme {
                navController = TestNavHostController(appContext)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                DefaultNavHost(
                    timerViewModel = timerViewModel,
                    preferencesDataStore = PreferencesDataStore(appContext),
                    navController = navController,
                    soundEffects = SoundEffects(context = appContext)
                )
            }
        }

        val homeScreenSettingsIconDescription =
            appContext.getString(R.string.settings_icon_content_description)

        val settingsScreenArrowBackIconDescription =
            appContext.getString(R.string.arrow_back_icon_content_description)

        composeTestRule
            .onNodeWithContentDescription(homeScreenSettingsIconDescription)
            .performClick()


        composeTestRule
            .onNodeWithContentDescription(settingsScreenArrowBackIconDescription)
            .assertExists()

        composeTestRule.onRoot().printToLog("SEMANTIC_NODES")
    }
}