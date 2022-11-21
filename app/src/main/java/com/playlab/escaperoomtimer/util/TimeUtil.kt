package com.playlab.escaperoomtimer.util

import java.util.*

object TimeUtil {
    fun getFormattedTimeString(hour: Int = 0, minute: Int = 0, second: Int = 0): String {
        val time = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hour, minute, second);
        return time
    }

    fun getTimeInMillis(hour: Int = 0, minutes: Int = 0, second: Int = 0): Long {
        val ONE_SECOND = 1_000L
        val ONE_MINUTE = 60 * ONE_SECOND
        val ONE_HOUR = 60 * ONE_MINUTE

        val hourInMillis =  hour * ONE_HOUR
        val minutesInMillis = minutes * ONE_MINUTE
        val secondsInMillis = second * ONE_SECOND

        return  hourInMillis + minutesInMillis + secondsInMillis
    }
}