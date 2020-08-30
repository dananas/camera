package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class OpeningStateTest : BaseTest() {
    private lateinit var state: OpeningState

    @Before
    fun init() {
        state = OpeningState(stateMachineMock)
    }

    @Test
    fun `open camera on enter`() {
        val state = OpeningState(stateMachine)
        state.onEnter()
        verify(openerMock).openCamera(any(), any(), any())
    }

    @Test
    fun `goto reclosing on stop`() {
        state.onAction(CameraAction.Stop)
        verify(stateMachineMock).state = any<ReclosingState>()
    }

    @Test
    fun `goto starting session on opened`() {
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<StartingSessionState>()
    }

    @Test
    fun `goto closed on disconnect`() {
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<ClosedState>()
    }

    @Test
    fun `goto opening on error`() {
        state.onAction(CameraAction.Callback.Error(error = 4))
        verify(stateMachineMock).state = any<OpeningState>()
    }
}