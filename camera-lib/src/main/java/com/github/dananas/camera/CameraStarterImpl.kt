package com.github.dananas.camera

import android.os.Handler
import android.view.Surface
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class CameraStarterImpl(
    cameraOpener: CameraOpener,
    private val cameraHandler: Handler,
    private val logger: CameraLogger
) : CameraStarter {
    private val stateMachine = CameraStateMachine(cameraOpener, cameraHandler, logger)

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