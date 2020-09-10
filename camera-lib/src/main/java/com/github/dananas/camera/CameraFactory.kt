package com.github.dananas.camera

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.view.WindowManager
import com.github.dananas.camera.logger.CameraLogger

internal class CameraFactory(
    private val cameraManager: CameraManager,
    private val windowManager: WindowManager,
    private val logger: CameraLogger,
    private val exceptionHandler: CameraExceptionHandler
) {

    fun createCamera(cameraDevice: CameraDevice): Camera {
        val cameraInfoProvider = CameraInfoProviderImpl(
            cameraDevice.id,
            cameraManager,
            logger,
            windowManager.defaultDisplay.rotation
        )
        return CameraWrapper(cameraInfoProvider, exceptionHandler, cameraDevice)
    }
}