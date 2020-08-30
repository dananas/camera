package com.github.dananas.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import androidx.annotation.CheckResult
import com.github.dananas.camera.statemachine.CameraStateMachine
import java.lang.Exception

internal interface CameraSession {
    @CheckResult
    fun setRepeatingBurst(
        request: CaptureRequest,
        listener: CameraCaptureSession.CaptureCallback,
        handler: Handler
    ): Boolean

    @CheckResult
    fun close(): Boolean
}

internal class CameraSessionWrapper(
    private val machine: CameraStateMachine,
    private val session: CameraCaptureSession
) : CameraSession {
    override fun setRepeatingBurst(
        request: CaptureRequest,
        listener: CameraCaptureSession.CaptureCallback,
        handler: Handler
    ): Boolean = invokeSafe {
        session.setRepeatingBurst(listOf(request), listener, handler)
    } != null

    override fun close(): Boolean = invokeSafe {
        session.close()
    } != null

    private inline fun <T> invokeSafe(block: () -> T): T? {
        return try {
            block()
        } catch (e: Exception) {
            machine.sessionException(e)
            null
        }
    }
}