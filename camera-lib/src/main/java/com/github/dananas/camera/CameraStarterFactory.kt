package com.github.dananas.camera

import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.logger.CameraLoggerImpl

object CameraStarterFactory {

    @AnyThread
    fun create(
        cameraManager: CameraManager,
        handler: Handler? = null,
        logger: CameraLogger? = null
    ): CameraStarter {
        val cameraHandler = handler ?: run {
            val handlerThread = HandlerThread("camera-thread")
            handlerThread.start()
            Handler(handlerThread.looper)
        }
        val cameraLogger = logger ?: run {
            CameraLoggerImpl(enableDebug = BuildConfig.DEBUG)
        }
        val opener = CameraOpenerImpl(cameraManager)
        return CameraStarterImpl(opener, cameraHandler, cameraLogger)
    }
}