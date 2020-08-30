package com.github.dananas.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.view.Surface
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.github.dananas.camera.statemachine.CameraStateMachine
import java.lang.Exception

internal interface Camera {
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
    private val machine: CameraStateMachine,
    private val camera: CameraDevice
) : Camera {

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
            machine.cameraException(e)
            null
        }
    }
}