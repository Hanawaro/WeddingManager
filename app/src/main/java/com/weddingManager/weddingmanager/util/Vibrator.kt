package com.weddingManager.weddingmanager.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator;
import androidx.core.content.ContextCompat.getSystemService


object Vibrator {

    fun getInstance(context: Context): Vibrator? {
        return getSystemService(context, Vibrator::class.java)
    }

    fun vibrate(vibrator: Vibrator?, time: Long) {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator?.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(time)
        }
    }

}