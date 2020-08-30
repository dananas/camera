package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.CameraSession
import com.github.dananas.camera.Camera
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraStateMachine

internal open class SessionStartedState(
    private val machine: CameraStateMachine,
    private val camera: Camera,
    private val session: CameraSession
) : OpenedState(machine, camera) {

    override fun onAction(action: CameraAction) {
        when (action) {
            is CameraAction.Callback.SessionConfigured -> {
                session.close()
                machine.state = StartingSessionState(machine, camera)
            }
            CameraAction.Callback.SessionFailed -> {
                machine.state = ClosingState(machine, camera, restart = true)
            }
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
            else -> super.onAction(action)
        }.exhaustive
    }
}