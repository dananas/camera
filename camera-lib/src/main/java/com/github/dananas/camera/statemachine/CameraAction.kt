package com.github.dananas.camera.statemachine

import com.github.dananas.camera.CameraSession
import com.github.dananas.camera.Camera

internal sealed class CameraAction {
    object Start : CameraAction()
    object Stop : CameraAction()

    sealed class Callback : CameraAction() {
        class Opened(val camera: Camera) : Callback()
        object Disconnected : Callback()
        class Error(error: Int) : Callback()
        class SessionConfigured(val session: CameraSession) : Callback()
        object SessionFailed : Callback()
        object CaptureStarted : Callback()
    }

    override fun toString(): String {
        return javaClass.simpleName
    }
}