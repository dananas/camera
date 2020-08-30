package com.github.dananas.camera.statemachine.states

import androidx.annotation.VisibleForTesting
import com.github.dananas.camera.Camera
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class ClosingState(
    private val machine: CameraStateMachine,
    private val camera: Camera,
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var restart: Boolean
) : CameraState() {

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                machine.state = ReopeningState(machine, camera)
            }
            CameraAction.Stop -> {
                restart = false
            }
            is CameraAction.Callback.Opened -> {
                machine.state = ClosingState(machine, camera, restart)
            }
            CameraAction.Callback.Disconnected -> {
                machine.state = if (restart) {
                    OpeningState(machine)
                } else {
                    ClosedState(machine)
                }
            }
            is CameraAction.Callback.Error -> {
                machine.state = if (restart) {
                    ReopeningState(machine, camera)
                } else {
                    ClosedState(machine)
                }
            }
            is CameraAction.Callback.SessionConfigured -> {
                /* ignore */
            }
            CameraAction.Callback.SessionFailed ->  {
                /* ignore */
            }
            CameraAction.Callback.CaptureStarted ->  {
                /* ignore */
            }
        }.exhaustive
    }

    override fun onEnter() {
        if (!camera.close()) {
            machine.state = if (restart) {
                OpeningState(machine)
            } else {
                ClosedState(machine)
            }
        }
    }
}