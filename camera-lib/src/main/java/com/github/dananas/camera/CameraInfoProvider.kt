package com.github.dananas.camera

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.view.Surface
import androidx.annotation.AnyThread
import com.github.dananas.camera.logger.CameraLogger

@AnyThread
internal class CameraInfoProviderImpl(
    private val cameraId: String,
    manager: CameraManager,
    private val logger: CameraLogger,
    private val deviceOrientation: Int
) : CameraInfoProvider {

    private val characteristics = manager.getCameraCharacteristics(cameraId)

    override fun getJpegOrientation(): Int {
        val surfaceRotation = orientationDegrees.getValue(deviceOrientation)
        return (surfaceRotation + getSensorOrientation() + 270) % 360
    }

    override fun getSensorOrientation(): Int {
        val orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        return orientation ?: kotlin.run {
            logger.warn { "SENSOR_ORIENTATION property does not present for cameraId$cameraId" }
            0
        }
    }

    private companion object {
        private val orientationDegrees = HashMap<Int, Int>(4)

        init {
            orientationDegrees[Surface.ROTATION_0] = 90
            orientationDegrees[Surface.ROTATION_90] = 0
            orientationDegrees[Surface.ROTATION_180] = 270
            orientationDegrees[Surface.ROTATION_270] = 180
        }
    }
}