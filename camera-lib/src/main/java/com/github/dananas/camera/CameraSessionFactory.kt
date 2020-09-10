package com.github.dananas.camera

import android.hardware.camera2.CameraCaptureSession

internal class CameraSessionFactory(
    private val exceptionHandler: CameraExceptionHandler
) {

    fun createSession(session: CameraCaptureSession): CameraSession {
        return CameraSessionWrapper(exceptionHandler, session)
    }
}