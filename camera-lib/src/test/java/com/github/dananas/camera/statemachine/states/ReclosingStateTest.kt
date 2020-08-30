package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class ReclosingStateTest : BaseTest() {
    private lateinit var state: ReclosingState

    @Before
    fun init() {
        state = ReclosingState(stateMachineMock)
    }

    @Test
    fun `goto starting session on opened if was started`() {
        state.onAction(CameraAction.Start)
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<StartingSessionState>()
    }

    @Test
    fun `goto closing on opened`() {
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<ClosingState>()
    }

    @Test
    fun `goto closed on error`() {
        state.onAction(CameraAction.Callback.Error(error = 4))
        verify(stateMachineMock).state = any<ClosedState>()
    }

    @Test
    fun `goto closed on disconnect`() {
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<ClosedState>()
    }
}