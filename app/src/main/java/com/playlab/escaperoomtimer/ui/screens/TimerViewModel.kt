package com.playlab.escaperoomtimer.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlab.escaperoomtimer.util.TimeUtil
import kotlinx.coroutines.launch
import java.util.*

class TimerViewModel : ViewModel() {
    private var timer: Timer? = null

    private var _startTimeInMillis  =  mutableStateOf(0L)
    val startTimeInMillis = _startTimeInMillis

    private var _timeUntilFinishInMillis  =  mutableStateOf(0L)
    val timeUntilFinishInMillis = _timeUntilFinishInMillis

    private var _inputCode =  mutableStateOf("")
    val inputCode = _inputCode

    private var _defuseCode =  mutableStateOf("")
    val defuseCode = _defuseCode

    private var _penalty =  mutableStateOf(0)
    val penalty = _penalty

    private var _tickSoundEnabled =  mutableStateOf(true)
    val tickSoundEnabled = _tickSoundEnabled

    fun startTimer(action: () -> Unit, onFinish: () -> Unit){
        if (timeUntilFinishInMillis.value != 0L && timer == null) {
            timer = Timer()
            viewModelScope.launch {
                timer?.schedule( object : TimerTask() {
                    override fun run() {
                        action()
                        if(timeUntilFinishInMillis.value >= 1000) {
                            setTimeUntilFinishInMillis(timeUntilFinishInMillis.value -  1000)
                        }

                        if (isFinished()) {
                            onFinish()
                            this.cancel()
                        }
                    }
                }, 0L, 1000L)
            }
        }
    }

    fun stopTimer(){
        timer?.cancel()
        timer = null
        resetTimer()
    }

    fun setEnabledTickSound(enableTickSound: Boolean){
        _tickSoundEnabled .value = enableTickSound
    }

    fun setInputCode(code: String){
        if(code.length <= 8) _inputCode.value = code
    }

    fun setDefuseCode(defuseCode: String){
        _defuseCode.value = defuseCode
    }

    fun setPenaltyTime(penalty: Int){
        _penalty.value = penalty
    }

    fun penalize(){
        val penaltyInMillis = penalty.value  * 1000
        val timeWithPenalty = timeUntilFinishInMillis.value - penaltyInMillis

        if(timeWithPenalty >= 0){
            setTimeUntilFinishInMillis(
                timeWithPenalty
            )
        }else{
            setTimeUntilFinishInMillis(
                0L
            )
        }
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

    fun isFinished(): Boolean{
        return timeUntilFinishInMillis.value == 0L
    }

    fun isDefused(): Boolean{
        return inputCode.value == defuseCode.value
    }

    fun resetTimer(){
        setTimeUntilFinishInMillis(0)
        setStartTimeInMillis(0)
        setInputCode("")
    }

}