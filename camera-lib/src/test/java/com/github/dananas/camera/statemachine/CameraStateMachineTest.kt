package com.github.dananas.camera.statemachine

import com.github.dananas.camera.BaseTest
import com.github.dananas.camera.statemachine.states.ClosedState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertTrue
import org.junit.Test

private const val CAMERA_ID = "0"

internal class CameraStateMachineTest : BaseTest() {
    @Test
    fun `state machine starts in ClosedState`() {
        assertTrue(stateMachine.state is ClosedState)
    }

    @Test
    fun `open camera on start`() {
        stateMachine.start(CAMERA_ID, emptyList())
        verify(openerMock).openCamera(any(), any(), any())
    }

    @Test
    fun `no camera permission throws no exceptions and closes`() {
        val securityException = SecurityException()
        doThrow(securityException).`when`(openerMock).openCamera(any(), any(), any())
        stateMachine.start(CAMERA_ID, emptyList())
        verify(openerMock).openCamera(any(), any(), any())
        verify(loggerMock).error(eq(securityException), any())
        assertTrue(stateMachine.state is ClosedState)
    }
}