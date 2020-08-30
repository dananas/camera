package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.Camera
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class StartingSessionState(
    private val machine: CameraStateMachine,
    private val camera: Camera
) : OpenedState(machine, camera) {

    override fun onAction(action: CameraAction) {
        when (action) {
            is CameraAction.Callback.SessionConfigured -> {
                machine.state = StartingPreviewState(machine, camera, action.session)
            }
            CameraAction.Callback.SessionFailed -> {
                machine.state = ReopeningState(machine, camera)
            }
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
            else -> super.onAction(action)
        }.exhaustive
    }

    override fun onEnter() {
        if (!camera.createCaptureSession(machine.surfaces, machine.sessionListener, machine.handler)) {
            machine.state = ReopeningState(machine, camera)
        }
    }
}