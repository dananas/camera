package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class SessionStartedStateTest : BaseTest() {
    private lateinit var state: SessionStartedState

    @Before
    fun init() {
        state = SessionStartedState(stateMachineMock, cameraMock, cameraSessionMock)
    }

    @Test
    fun `goto session started on session configured`() {
        state.onAction(CameraAction.Callback.SessionConfigured(cameraSessionMock))
        verify(cameraSessionMock).close()
        verify(stateMachineMock).state = any<StartingSessionState>()
    }

    @Test
    fun `goto closing on session failed`() {
        state.onAction(CameraAction.Callback.SessionFailed)
        verify(stateMachineMock).state = any<ClosingState>()
    }
}