package com.playlab.escaperoomtimer.util

import com.google.common.truth.Truth.assertThat
import com.playlab.escaperoomtimer.util.TimeUtil.getFormattedTimeString
import com.playlab.escaperoomtimer.util.TimeUtil.getTimeInMillis
import org.junit.Test

class TestTimeUtil {

    @Test
    fun `getTimeInMillis(23, 59, 59) should return 86399000 milliseconds` (){
        val timeInMillis = getTimeInMillis(23, 59, 59)

        assertThat(timeInMillis).isEqualTo(86399000)
    }

    @Test
    fun `getFormattedTimeString(23, 59, 59) should return 24 hour time string` (){
        val timeString: String = getFormattedTimeString(23, 59, 59)

        assertThat(timeString).isEqualTo("23:59:59")
    }

    @Test
    fun `getFormattedTimeString(86399000L) should return 24 hour time string` (){
        val timeString: String = getFormattedTimeString(86399000L)

        assertThat(timeString).isEqualTo("23:59:59")
    }
}