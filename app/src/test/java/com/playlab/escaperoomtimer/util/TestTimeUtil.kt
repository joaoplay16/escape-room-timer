package com.playlab.escaperoomtimer.util

import com.google.common.truth.Truth.assertThat
import com.playlab.escaperoomtimer.util.TimeUtil.getTimeInMillis
import org.junit.Test

class TestTimeUtil {

    @Test
    fun `getTimeInMillis(23, 59, 59) should return 86399000 milliseconds` (){
        val timeInMillis = getTimeInMillis(23, 59, 59)

        assertThat(timeInMillis).isEqualTo(86399000)
    }
}