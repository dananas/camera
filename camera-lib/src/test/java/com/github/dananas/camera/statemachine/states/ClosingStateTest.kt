package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class ClosingStateTest : BaseTest() {
    private lateinit var state: ClosingState

    @Before
    fun init() {
        state = ClosingState(stateMachineMock, cameraMock, restart = true)
    }

    @Test
    fun `goto reopening on start`() {
        state.onAction(CameraAction.Start)
        verify(stateMachineMock).state = any<ReopeningState>()
    }

    @Test
    fun `stop restarting on stop`() {
        state.onAction(CameraAction.Stop)
        Assert.assertFalse(state.restart)
    }

    @Test
    fun `goto closing on opened`() {
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<ClosingState>()
    }

    @Test
    fun `goto opening on disconnected`() {
        val state = ClosingState(stateMachineMock, cameraMock, restart = true)
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<OpeningState>()
    }

    @Test
    fun `goto closed on disconnected`() {
        val state = ClosingState(stateMachineMock, cameraMock, restart = false)
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<ClosedState>()
    }

    @Test
    fun `goto reopening on error`() {
        val state = ClosingState(stateMachineMock, cameraMock, restart = true)
        state.onAction(CameraAction.Callback.Error(error = 4))
        verify(stateMachineMock).state = any<ReopeningState>()
    }

    @Test
    fun `goto closed on error`() {
        val state = ClosingState(stateMachineMock, cameraMock, restart = false)
        state.onAction(CameraAction.Callback.Error(error = 4))
        verify(stateMachineMock).state = any<ClosedState>()
    }
}