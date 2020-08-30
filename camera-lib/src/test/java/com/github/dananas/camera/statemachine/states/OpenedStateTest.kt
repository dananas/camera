package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class OpenedStateTest : BaseTest() {
    private lateinit var state: OpenedState

    @Before
    fun init() {
        state = OpenedState(stateMachineMock, cameraMock)
    }

    @Test
    fun `goto closing on stop`() {
        state.onAction(CameraAction.Stop)
        verify(stateMachineMock).state = any<ClosingState>()
    }

    @Test
    fun `goto closing on opened`() {
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<ClosingState>()
    }

    @Test
    fun `goto closed on disconnect`() {
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<ClosedState>()
    }

    @Test
    fun `goto reopening on error`() {
        state.onAction(CameraAction.Callback.Error(error = 4))
        verify(stateMachineMock).state = any<ReopeningState>()
    }
}