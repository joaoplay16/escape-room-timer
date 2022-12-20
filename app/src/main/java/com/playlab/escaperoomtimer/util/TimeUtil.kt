package com.playlab.escaperoomtimer.util

import com.playlab.escaperoomtimer.util.Constants.ONE_HOUR
import com.playlab.escaperoomtimer.util.Constants.ONE_MINUTE
import com.playlab.escaperoomtimer.util.Constants.ONE_SECOND
import java.util.*
import kotlin.math.floor

object TimeUtil {
    fun getFormattedTimeString(hour: Int = 0, minute: Int = 0, second: Int = 0): String {
        val time = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hour, minute, second)
        return time
    }

    fun getLeftPaddedNumberString(number: Int = 0): String {
        return number.toString().padStart(2, '0')
    }

    fun getTimeInMillis(hour: Int = 0, minutes: Int = 0, second: Int = 0): Long {
        val hourInMillis =  hour * ONE_HOUR
        val minutesInMillis = minutes * ONE_MINUTE
        val secondsInMillis = second * ONE_SECOND

        return  hourInMillis + minutesInMillis + secondsInMillis
    }

    fun getFormattedTimeString(millis: Long): String{
        val hour = floor((millis.toDouble() / (1000 * 60 * 60)) % 24).toLong()
        val minute = floor((millis.toDouble() / (1000 * 60)) % 60).toLong()
        val second = floor((millis.toDouble() / 1000) % 60).toLong()

        return getFormattedTimeString(hour.toInt(), minute.toInt(), second.toInt())
    }
}