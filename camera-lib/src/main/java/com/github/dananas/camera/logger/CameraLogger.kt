package com.github.dananas.camera.logger

import android.util.Log

interface CameraLogger {
    fun debug(messageProvider: () -> String)
    fun warn(messageProvider: () -> String)
    fun error(throwable: Throwable, messageProvider: () -> String)
    fun error(messageProvider: () -> String)
}

internal class CameraLoggerImpl(private val enableDebug: Boolean) : CameraLogger {
    override fun debug(messageProvider: () -> String) {
        if (enableDebug) {
            Log.d(TAG, messageProvider())
        }
    }

    override fun warn(messageProvider: () -> String) {
        Log.w(TAG, messageProvider())
    }

    override fun error(throwable: Throwable, messageProvider: () -> String) {
        Log.e(TAG, messageProvider(), throwable)
    }

    override fun error(messageProvider: () -> String) {
        Log.e(TAG, messageProvider())
    }

    private companion object {
        private const val TAG = "Camera"
    }
}