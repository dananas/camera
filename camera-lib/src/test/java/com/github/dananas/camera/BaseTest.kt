package com.github.dananas.camera

import android.os.Handler
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.statemachine.CameraStateMachine
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal abstract class BaseTest {
    protected lateinit var stateMachine: CameraStateMachine

    @Mock
    protected lateinit var stateMachineMock: CameraStateMachine

    @Mock
    protected lateinit var openerMock: CameraOpener

    @Mock
    protected lateinit var handler: Handler

    @Mock
    protected lateinit var loggerMock: CameraLogger

    @Mock
    protected lateinit var cameraMock: Camera

    @Mock
    protected lateinit var cameraFactoryMock: CameraFactory

    @Mock
    protected lateinit var cameraSessionFactoryMock: CameraSessionFactory

    @Mock
    protected lateinit var cameraExceptionHandlerMock: CameraExceptionHandler

    @Mock
    protected lateinit var cameraSessionMock: CameraSession

    @Before
    fun `init state machine`() {
        stateMachine = CameraStateMachine(
            openerMock,
            cameraFactoryMock,
            cameraSessionFactoryMock,
            handler,
            loggerMock,
            cameraExceptionHandlerMock
        )
    }
}