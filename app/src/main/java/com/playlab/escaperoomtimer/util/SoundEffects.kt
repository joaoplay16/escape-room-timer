package com.playlab.escaperoomtimer.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build

object SoundEffects {

    private var mp: MediaPlayer? = null

    fun playSound(context: Context, resId: Int) {

        if (mp != null) {
            try {
                mp!!.reset()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mp!!.setDataSource(context.resources.openRawResourceFd(resId))
                }else{
                    val uri: Uri = Uri.parse(
                        "android.resource://${context.packageName}/$resId"
                    )
                    mp!!.setDataSource(context, uri)
                }

                mp!!.prepare()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            mp = MediaPlayer.create(context, resId)
        }
        mp!!.start()

    }
}


