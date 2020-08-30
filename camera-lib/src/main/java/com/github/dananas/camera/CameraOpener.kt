package com.github.dananas.camera

import android.Manifest
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import androidx.annotation.RequiresPermission

interface CameraOpener {
    @RequiresPermission(Manifest.permission.CAMERA)
    fun openCamera(cameraId: String, callback: CameraDevice.StateCallback, handler: Handler)
}

internal class CameraOpenerImpl(private val cameraManager: CameraManager) : CameraOpener {

    @RequiresPermission(Manifest.permission.CAMERA)
    override fun openCamera(
        cameraId: String,
        callback: CameraDevice.StateCallback,
        handler: Handler
    ) {
        cameraManager.openCamera(cameraId, callback, handler)
    }
}