package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.CameraSession
import com.github.dananas.camera.Camera
import com.github.dananas.camera.statemachine.CameraStateMachine

internal class PreviewStartedState(
    machine: CameraStateMachine,
    camera: Camera,
    session: CameraSession
) : SessionStartedState(machine, camera, session)