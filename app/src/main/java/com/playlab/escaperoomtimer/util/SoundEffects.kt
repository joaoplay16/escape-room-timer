package com.playlab.escaperoomtimer.util

import android.content.Context
import android.media.SoundPool

class SoundEffects(private val context: Context) {

    private var soundPool: SoundPool? = null
    private var soundId: Int? = null
    private var streamId: Int? = null

    fun playSound(resId: Int) {

        if(soundPool == null){
            soundPool = SoundPool.Builder().setMaxStreams(5).build()
        }

        soundPool?.let { sp ->
            soundId = sp.load(context, resId, 1)

            sp.setOnLoadCompleteListener { _, _, _ ->
               streamId = soundPool!!.play(soundId!!, 1f, 1f, 0, 0, 1f)
            }
        }
    }

    fun stopSound() {
        streamId?.let {
            soundPool?.stop(it)
            streamId = null
        }
    }

    fun releaseSoundPool() {
        soundPool?.release()
        soundPool = null
        soundId = null
    }
}