package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class StartingPreviewStateTest : BaseTest() {
    private lateinit var state: StartingPreviewState

    @Before
    fun init() {
        state = StartingPreviewState(stateMachineMock, cameraMock, cameraSessionMock)
    }

    @Test
    fun `goto preview started on capture started`() {
        state.onAction(CameraAction.Callback.CaptureStarted)
        verify(stateMachineMock).state = any<PreviewStartedState>()
    }
}