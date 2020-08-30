package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

internal class ClosedStateTest : BaseTest() {
    @Test
    fun `goto opening on start`() {
        val state = ClosedState(stateMachineMock)
        state.onAction(CameraAction.Start)
        verify(stateMachineMock).state = any<OpeningState>()
    }

    @Test
    fun `goto closing on opened`() {
        val state = ClosedState(stateMachineMock)
        state.onAction(CameraAction.Callback.Opened(cameraMock))
        verify(stateMachineMock).state = any<ClosingState>()
    }
}