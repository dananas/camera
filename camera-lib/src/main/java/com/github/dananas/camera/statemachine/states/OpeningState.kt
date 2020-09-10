package com.github.dananas.camera.statemachine.states

import android.hardware.camera2.CameraAccessException
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraState
import com.github.dananas.camera.statemachine.CameraStateMachine
import java.lang.Exception

internal class OpeningState(
    private val machine: CameraStateMachine
) : CameraState() {

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Start -> {
                /* ignore */
            }
            CameraAction.Stop -> {
                machine.state = ReclosingState(machine)
            }
            is CameraAction.Callback.Opened -> {
                machine.state = StartingSessionState(machine, action.camera)
            }
            CameraAction.Callback.Disconnected -> {
                machine.state = ClosedState(machine)
            }
            is CameraAction.Callback.Error -> {
                machine.state = OpeningState(machine)
            }
            is CameraAction.Callback.SessionConfigured -> machine.illegalAction(action)
            CameraAction.Callback.SessionFailed -> machine.illegalAction(action)
            CameraAction.Callback.CaptureStarted -> machine.illegalAction(action)
        }.exhaustive
    }

    override fun onEnter() {
        val exceptionHandler = machine.exceptionHandler
        try {
            machine.openCamera()
        } catch (e: SecurityException) {
            machine.state = ClosedState(machine)
            exceptionHandler.cameraException(e)
        } catch (e: CameraAccessException) {
            exceptionHandler.cameraException(e)
            machine.state = OpeningState(machine)
        } catch (e: Exception) {
            exceptionHandler.cameraException(e)
            machine.state = OpeningState(machine)
        }
    }
}
