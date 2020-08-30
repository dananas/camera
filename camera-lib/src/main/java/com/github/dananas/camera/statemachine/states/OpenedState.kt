package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.Camera
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine

internal open class OpenedState(
    private val machine: CameraStateMachine,
    private val camera: Camera
) : CameraState() {

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                /* ignore */
            }
            CameraAction.Stop -> {
                machine.state = ClosingState(machine, camera, restart = false)
            }
            is CameraAction.Callback.Opened -> {
                machine.illegalAction(action)
                machine.state = ClosingState(machine, camera, restart = true)
            }
            CameraAction.Callback.Disconnected -> {
                machine.state = ClosedState(machine)
            }
            is CameraAction.Callback.Error -> {
                machine.state = ReopeningState(machine, camera)
            }
            is CameraAction.Callback.SessionConfigured -> machine.illegalAction(action)
            CameraAction.Callback.SessionFailed -> machine.illegalAction(action)
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
        }.exhaustive
    }
}
