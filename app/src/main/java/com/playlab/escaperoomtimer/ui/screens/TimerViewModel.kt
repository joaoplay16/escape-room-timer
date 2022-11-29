package com.playlab.escaperoomtimer.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.playlab.escaperoomtimer.util.TimeUtil

class TimerViewModel : ViewModel() {

    private var _startTimeInMillis  =  mutableStateOf(0L)
    val startTimeInMillis = _startTimeInMillis

    private var _timeUntilFinishInMillis  =  mutableStateOf(0L)
    val timeUntilFinishInMillis = _timeUntilFinishInMillis

    private var _inputCode =  mutableStateOf("")
    val inputCode = _inputCode

    private var _defuseCode =  mutableStateOf("")
    val defuseCode = _defuseCode

    private var _penalty =  mutableStateOf("")
    val penalty = _penalty

    private var _tickSoundEnabled =  mutableStateOf(true)
    val tickSoundEnabled = _tickSoundEnabled

    fun setEnabledTickSound(enableTickSound: Boolean){
        _tickSoundEnabled .value = enableTickSound
    }

    fun setInputCode(code: String){
        if(code.length <= 8) _inputCode.value = code
    }

    fun setDefuseCode(defuseCode: String){
        _defuseCode.value = defuseCode
    }

    fun setPenaltyTime(penalty: String){
        _penalty.value = penalty
    }

    fun setStartTimeInMillis(value: Long){
        _startTimeInMillis.value = value
    }

    fun setTimeUntilFinishInMillis(value: Long){
        _timeUntilFinishInMillis.value = value
    }

    fun getTimeString(): String {
        return TimeUtil.getFormattedTimeString(
            timeUntilFinishInMillis.value
        )
    }

    fun isDefused(): Boolean{
        return inputCode.value == defuseCode.value
    }

    fun resetTimer(){
        setTimeUntilFinishInMillis(0)
        setInputCode("")
    }

}