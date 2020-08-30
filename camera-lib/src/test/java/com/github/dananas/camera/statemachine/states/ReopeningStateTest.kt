package com.github.dananas.camera.statemachine.states

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.CameraAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

internal class ReopeningStateTest : BaseTest() {
    private lateinit var state: ReopeningState

    @Before
    fun init() {
        state = ReopeningState(stateMachineMock, cameraMock)
    }

    @Test
    fun `close camera on enter and goto opening`() {
        doReturn(false).`when`(cameraMock).close()
        state.onEnter()
        verify(cameraMock).close()
        verify(stateMachineMock).state = any<OpeningState>()
    }

    @Test
    fun `close camera on enter stay in reopening`() {
        doReturn(true).`when`(cameraMock).close()
        state.onEnter()
        verify(cameraMock).close()
        verify(stateMachineMock, never()).state = any<OpeningState>()
    }

    @Test
    fun `goto opening on disconnected`() {
        state.onAction(CameraAction.Start)
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<OpeningState>()
    }

    @Test
    fun `goto closing on disconnected`() {
        state.onAction(CameraAction.Stop)
        state.onAction(CameraAction.Callback.Disconnected)
        verify(stateMachineMock).state = any<ClosedState>()
    }
}