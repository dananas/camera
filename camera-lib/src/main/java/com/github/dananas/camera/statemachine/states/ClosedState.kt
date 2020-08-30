package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class ClosedState(private val machine: CameraStateMachine) : CameraState() {
    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                machine.state = OpeningState(machine)
            }
            CameraAction.Stop -> {
                /* ignore */
            }
            is CameraAction.Callback.Opened -> {
                machine.illegalAction(action)
                machine.state = ClosingState(machine, action.camera, restart = true)
            }
            CameraAction.Callback.Disconnected -> {
                /* ignore */
            }
            is CameraAction.Callback.Error -> {
                /* ignore */
            }
            is CameraAction.Callback.SessionConfigured -> {
                /* ignore */
            }
            CameraAction.Callback.SessionFailed -> {
                /* ignore */
            }
            CameraAction.Callback.CaptureStarted -> {
                /* ignore */
            }
        }.exhaustive
    }
}
