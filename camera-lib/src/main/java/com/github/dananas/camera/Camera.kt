package com.github.dananas.camera

import android.hardware.camera2.*
import android.os.Handler
import android.view.Surface
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import java.lang.Exception

internal interface CameraInfoProvider {
    fun getSensorOrientation(): Int
    fun getJpegOrientation(): Int
}

internal interface Camera : CameraInfoProvider {
    @CheckResult
    fun createCaptureRequest(template: Int): CaptureRequest.Builder?

    @CheckResult
    fun createCaptureSession(
        surfaces: List<Surface>,
        callback: CameraCaptureSession.StateCallback,
        handler: Handler
    ): Boolean

    @CheckResult
    fun close(): Boolean
}

@WorkerThread
internal class CameraWrapper(
    cameraInfoProvider: CameraInfoProvider,
    private val exceptionHandler: CameraExceptionHandler,
    private val camera: CameraDevice
) : Camera, CameraInfoProvider by cameraInfoProvider {

    @CheckResult
    override fun createCaptureRequest(template: Int): CaptureRequest.Builder? = invokeSafe {
        camera.createCaptureRequest(template)
    }

    @CheckResult
    override fun createCaptureSession(
        surfaces: List<Surface>,
        callback: CameraCaptureSession.StateCallback,
        handler: Handler
    ): Boolean = invokeSafe {
        camera.createCaptureSession(surfaces, callback, handler)
    } != null

    @CheckResult
    override fun close(): Boolean = invokeSafe {
        camera.close()
    } != null

    private inline fun <T> invokeSafe(block: () -> T): T? {
        return try {
            block()
        } catch (e: Exception) {
            exceptionHandler.cameraException(e)
            null
        }
    }
}