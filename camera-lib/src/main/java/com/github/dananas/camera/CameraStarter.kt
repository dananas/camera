package com.github.dananas.camera

import android.view.Surface
import androidx.annotation.AnyThread

@AnyThread
interface CameraStarter {
    fun start(cameraId: String, surfaces: List<Surface>)
    fun stop()
    fun release()
}