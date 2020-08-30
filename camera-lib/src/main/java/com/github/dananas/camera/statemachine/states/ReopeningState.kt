package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.Camera
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine
import com.github.dananas.camera.utils.exhaustive

/**
 * Camera is going to close soon and we want to open it right after disconnect
 */
internal class ReopeningState(
    private val machine: CameraStateMachine,
    private val camera: Camera
) : CameraState() {
    private var openAfterClose = true

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                openAfterClose = true
            }
            CameraAction.Stop -> {
                openAfterClose = false
            }
            is CameraAction.Callback.Opened -> {
                machine.illegalAction(action)
            }
            CameraAction.Callback.Disconnected -> {
                machine.state = if (openAfterClose) {
                    OpeningState(machine)
                } else {
                    ClosedState(machine)
                }
            }
            is CameraAction.Callback.Error -> {
                /* ignore */
            }
            is CameraAction.Callback.SessionConfigured -> machine.illegalAction(action)
            CameraAction.Callback.SessionFailed -> {
                /* ignored */
            }
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
        }.exhaustive
    }

    override fun onEnter() {
        if (!camera.close()) {
            machine.state = OpeningState(machine)
        }
    }
}
