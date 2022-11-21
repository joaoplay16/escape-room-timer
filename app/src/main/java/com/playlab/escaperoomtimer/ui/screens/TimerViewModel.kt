package com.playlab.escaperoomtimer.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.playlab.escaperoomtimer.util.TimeUtil

class TimerViewModel : ViewModel() {

    private var _timerHour  =  mutableStateOf(0L)
    val timeHour = _timerHour
    private var _timerMinute = mutableStateOf(0L)
    val timeMinute = _timerMinute
    private var _timerSecond = mutableStateOf(0L)
    val timeSecond = _timerSecond

    private var _startTimeInMillis  =  mutableStateOf(0L)
    val startTimeInMillis = _startTimeInMillis

    var _timeUntilFinishInMillis  =  _startTimeInMillis
    val timeUntilFinishInMillis = _startTimeInMillis

    private var _code =  mutableStateOf("")
    val code = _code

    private var _defuseCode =  mutableStateOf("")
    val defuseCode = _defuseCode

    private var _penalty =  mutableStateOf("0")
    val penalty = _penalty

    private var _tickSoundEnabled =  mutableStateOf(true)
    val tickSoundEnabled = _tickSoundEnabled

    fun setEnabledTickSound(enableTickSound: Boolean){
        _tickSoundEnabled .value = enableTickSound
    }

    fun setCode(code: String){
        if(code.length <= 8) _code.value = code
    }

    fun setDefuseCode(defuseCode: String){
        _defuseCode.value = defuseCode
    }

    fun setPenaltyValue(penalty: String){
        _penalty.value = penalty
    }

    fun setTimeHour(value: Long){
        _timerHour.value = value
    }

    fun setTimeMinute(value: Long){
        _timerMinute.value = value
    }

    fun setTimeSecond(value: Long){
        _timerSecond.value = value
    }

    fun setStartTimeInMillis(value: Long){
        _startTimeInMillis.value = value
    }

    fun setTimeUntilFinishInMillis(value: Long){
        _startTimeInMillis.value = value
    }

    fun getTimeString(): String {
        return TimeUtil.getFormattedTimeString(
            timeHour.value.toInt(),
            timeMinute.value.toInt(),
            timeSecond.value.toInt()
        )
    }

    fun hasDefused(): Boolean{
        return code.value == defuseCode.value
    }

    fun resetTimer(){
        setStartTimeInMillis(0)
        setTimeHour(0)
        setTimeMinute(0)
        setTimeSecond(0)
        setCode("")
    }

}