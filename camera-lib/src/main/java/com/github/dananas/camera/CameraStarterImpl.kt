package com.github.dananas.camera

import android.os.Handler
import android.view.Surface
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class CameraStarterImpl(
    cameraOpener: CameraOpener,
    cameraFactory: CameraFactory,
    cameraSessionFactory: CameraSessionFactory,
    private val cameraHandler: Handler,
    private val logger: CameraLogger,
    cameraExceptionHandler: CameraExceptionHandler
) : CameraStarter {

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
        logger.debug { "start" }
        cameraHandler.post {
            stateMachine.start(cameraId, surfaces)
        }
    }

    @AnyThread
    override fun stop() {
        logger.debug { "stop" }
        cameraHandler.post {
            stateMachine.stop()
        }
    }
}