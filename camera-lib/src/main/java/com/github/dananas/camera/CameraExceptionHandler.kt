package com.github.dananas.camera

import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger
import java.lang.Exception

@AnyThread
interface CameraExceptionHandler {
    fun cameraException(e: Exception)
    fun sessionException(e: Exception)
}

internal class CameraExceptionHandlerImpl(
    private val logger: CameraLogger
) : CameraExceptionHandler {

    override fun cameraException(e: Exception) {
        logger.error(e) { "Camera exception" }
    }

    override fun sessionException(e: Exception) {
        logger.error(e) { "Session exception" }
    }
}