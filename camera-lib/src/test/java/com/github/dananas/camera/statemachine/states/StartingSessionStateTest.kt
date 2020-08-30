package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class StartingSessionStateTest : BaseTest() {
    private lateinit var state: StartingSessionState

    @Before
    fun init() {
        state = StartingSessionState(stateMachineMock, cameraMock)
    }

    @Test
    fun `start preview on session configured`() {
        state.onAction(CameraAction.Callback.SessionConfigured(cameraSessionMock))
        verify(stateMachineMock).state = any<StartingPreviewState>()
    }

    @Test
    fun `goto reopening on session failed`() {
        state.onAction(CameraAction.Callback.SessionFailed)
        verify(stateMachineMock).state = any<ReopeningState>()
    }
}