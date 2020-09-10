package com.github.dananas.camera.statemachine

import android.Manifest
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.view.Surface
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import com.github.dananas.camera.*
import com.github.dananas.camera.CameraFactory
import com.github.dananas.camera.logger.CameraLogger
import com.github.dananas.camera.statemachine.states.ClosedState

internal class CameraStateMachine(
    private val cameraOpener: CameraOpener,
    private val cameraFactory: CameraFactory,
    private val cameraSessionFactory: CameraSessionFactory,
    val handler: Handler,
    private val logger: CameraLogger,
    val exceptionHandler: CameraExceptionHandler
) {
    var surfaces = mutableListOf<Surface>()
    var cameraId: String = "0"

    private val connectionListener = object : CameraDevice.StateCallback() {
        private var camera: CameraDevice? = null

        override fun onOpened(camera: CameraDevice) {
            val safeCamera = cameraFactory.createCamera(camera)
            val action = CameraAction.Callback.Opened(safeCamera)
            dispatchAction(action)
            this.camera = camera
        }

        override fun onDisconnected(camera: CameraDevice) {
            if (this.camera == camera) {
                dispatchAction(CameraAction.Callback.Disconnected)
                this.camera = null
            }
        }

        override fun onClosed(camera: CameraDevice) {
            if (this.camera == camera) {
                dispatchAction(CameraAction.Callback.Disconnected)
                this.camera = null
            }
        }

        override fun onError(camera: CameraDevice, error: Int) {
            if (this.camera == camera) {
                logger.error { "onError $error" }
                dispatchAction(CameraAction.Callback.Error(error))
            }
        }
    }

    val sessionListener = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            val safeSession = cameraSessionFactory.createSession(session)
            dispatchAction(CameraAction.Callback.SessionConfigured(safeSession))
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            logger.error { "onConfigureFailed" }
            dispatchAction(CameraAction.Callback.SessionFailed)
        }
    }

    val captureListener: CameraCaptureSession.CaptureCallback
        get() = object : CameraCaptureSession.CaptureCallback() {
            private var isFirstCapture = true

            override fun onCaptureStarted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                timestamp: Long,
                frameNumber: Long
            ) {
                if (isFirstCapture) {
                    dispatchAction(CameraAction.Callback.CaptureStarted)
                    isFirstCapture = false
                }
            }
        }

    var state: CameraState = ClosedState(this)
        set(newState) {
            field = newState
            dispatchEnterState(newState)
        }

    private var isStarted = false

    @WorkerThread
    fun start(cameraId: String, surfaces: List<Surface>) {
        if (isStarted) {
            dispatchAction(action = CameraAction.Stop)
        }
        this.cameraId = cameraId
        this.surfaces.apply {
            clear()
            addAll(surfaces)
        }
        isStarted = true
        dispatchAction(action = CameraAction.Start)
    }

    @WorkerThread
    fun stop() {
        isStarted = false
        surfaces.clear()
        dispatchAction(action = CameraAction.Stop)
    }

    @WorkerThread
    fun illegalAction(action: CameraAction) {
        logger.warn { "Illegal action:[$action] state:[$state]" }
    }

    @WorkerThread
    @RequiresPermission(Manifest.permission.CAMERA)
    fun openCamera() {
        logger.debug { "openCamera" }
        cameraOpener.openCamera(cameraId, connectionListener, handler)
    }

    private fun dispatchEnterState(newState: CameraState) {
        logger.debug { "Changing state [$state] -> [$newState]" }
        newState.onEnter()
    }

    private fun dispatchAction(action: CameraAction) {
        logger.debug { "dispatchAction $action" }
        state.onAction(action)
    }
}