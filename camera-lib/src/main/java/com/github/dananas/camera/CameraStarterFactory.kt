package com.github.dananas.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.view.WindowManager
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.logger.CameraLoggerImpl

object CameraStarterFactory {

    @AnyThread
    fun create(
        context: Context,
        logger: CameraLogger? = null,
        exceptionHandler: CameraExceptionHandler? = null
    ): CameraStarter {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val cameraHandler = run {
            val handlerThread = HandlerThread("camera-thread")
            handlerThread.start()
            Handler(handlerThread.looper)
        }
        val cameraLogger = logger ?: run {
            CameraLoggerImpl(enableDebug = BuildConfig.DEBUG)
        }
        val cameraExceptionHandler = exceptionHandler ?: run {
            CameraExceptionHandlerImpl(cameraLogger)
        }
        val opener = CameraOpenerImpl(cameraManager)
        val cameraFactory = CameraFactory(
            cameraManager,
            windowManager,
            cameraLogger,
            cameraExceptionHandler
        )
        val cameraSessionFactory = CameraSessionFactory(cameraExceptionHandler)
        return CameraStarterImpl(
            opener,
            cameraFactory,
            cameraSessionFactory,
            cameraHandler,
            cameraLogger,
            cameraExceptionHandler
        )
    }
}