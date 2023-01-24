package com.playlab.escaperoomtimer.ui.screens

import com.google.common.truth.Truth.assertThat
import com.playlab.escaperoomtimer.util.Constants.ONE_MINUTE
import com.playlab.escaperoomtimer.util.Constants.ONE_SECOND
import org.junit.Test

class TestTimerViewModel {

    @Test
    fun shouldReturnReducedTimeWhenPenalize() {
        val timerViewModel = TimerViewModel()

        with(timerViewModel){
            setTimeUntilFinishInMillis(ONE_MINUTE)
            setPenaltyTime(30)
            penalize()

            assertThat(timeUntilFinishInMillis.value).isLessThan(ONE_MINUTE)
            assertThat(timeUntilFinishInMillis.value).isEqualTo(30 * ONE_SECOND)
        }
    }
}