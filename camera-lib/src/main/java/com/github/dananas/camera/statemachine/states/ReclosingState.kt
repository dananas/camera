package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine
import com.github.dananas.camera.utils.exhaustive

/**
 * Camera is going to open soon and we want to close it right after it opened
 */
internal class ReclosingState(private val machine: CameraStateMachine) : CameraState() {
    private var closeAfterOpened = true

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                closeAfterOpened = false
            }
            CameraAction.Stop -> {
                closeAfterOpened = true
            }
            is CameraAction.Callback.Opened -> {
                machine.state = if (closeAfterOpened) {
                    ClosingState(machine, action.camera, restart = false)
                } else {
                    StartingSessionState(machine, action.camera)
                }
            }
            CameraAction.Callback.Disconnected -> {
                machine.state = ClosedState(machine)
            }
            is CameraAction.Callback.Error -> {
                machine.state = ClosedState(machine)
            }
            is CameraAction.Callback.SessionConfigured -> machine.illegalAction(action)
            CameraAction.Callback.SessionFailed -> machine.illegalAction(action)
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
        }.exhaustive
    }
}
