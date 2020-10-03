package com.github.dananas.camera

import android.os.Handler
import android.view.Surface
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.statemachine.CameraStateMachine
import java.util.concurrent.atomic.AtomicBoolean

internal class CameraStarterImpl(
    cameraOpener: CameraOpener,
    cameraFactory: CameraFactory,
    cameraSessionFactory: CameraSessionFactory,
    private val cameraHandler: Handler,
    private val logger: CameraLogger,
    cameraExceptionHandler: CameraExceptionHandler
) : CameraStarter {

    private val isReleased = AtomicBoolean(false)

    private val stateMachine = CameraStateMachine(
        cameraOpener,
        cameraFactory,
        cameraSessionFactory,
        cameraHandler,
        logger,
        cameraExceptionHandler
    )

    @AnyThread
    override fun start(cameraId: String, surfaces: List<Surface>) {
        if (!isReleased.get()) {
            logger.debug { "start" }
            cameraHandler.post {
                stateMachine.start(cameraId, surfaces)
            }
        } else {
            logger.warn { "start called in released state" }
        }
    }

    @AnyThread
    override fun stop() {
        if (!isReleased.get()) {
            logger.debug { "stop" }
            cameraHandler.post {
                stateMachine.stop()
            }
        } else {
            logger.warn { "stop called in released state" }
        }
    }

    override fun release() {
        logger.debug { "release" }
        if (isReleased.compareAndSet(false, true)) {
            cameraHandler.post {
                stateMachine.stop()
                cameraHandler.removeCallbacksAndMessages(null)
                cameraHandler.looper.quitSafely()
            }
        }
    }
}