package com.github.dananas.camera.statemachine.states

import android.hardware.camera2.CameraDevice
import com.github.dananas.camera.CameraSession
import com.github.dananas.camera.Camera
import com.github.dananas.camera.utils.exhaustive
import com.github.dananas.camera.statemachine.CameraAction
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class StartingPreviewState(
    private val machine: CameraStateMachine,
    private val camera: Camera,
    private val session: CameraSession
) : SessionStartedState(machine, camera, session) {

    override fun onAction(action: CameraAction) {
        when (action) {
            CameraAction.Callback.CaptureStarted -> {
                machine.state = PreviewStartedState(machine, camera, session)
            }
            else -> super.onAction(action)
        }.exhaustive
    }

    override fun onEnter() {
        val request = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        if (request != null) {
            machine.surfaces.forEach { surface ->
                request.addTarget(surface)
            }
            if (!session.setRepeatingBurst(request.build(), machine.captureListener, machine.handler)) {
                machine.state = ClosingState(machine, camera, restart = true)
            }
        } else {
            machine.state = ClosingState(machine, camera, restart = true)
        }
    }
}