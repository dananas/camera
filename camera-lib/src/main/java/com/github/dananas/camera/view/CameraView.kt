package com.github.dananas.camera.view

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.github.dananas.camera.CameraStarter
import com.github.dananas.camera.CameraStarterFactory
import com.github.dananas.camera.R

/**
 * Renders camera on itself.
 */
class CameraView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs) {
    private var cameraStarter: CameraStarter? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CameraView)
        val cameraId: String
        try {
            cameraId = attributes.getInt(R.styleable.CameraView_cameraId, DEFAULT_CAMERA_ID).toString()
        } finally {
            attributes.recycle()
        }
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val cameraStarter = CameraStarterFactory.create(context)
                cameraStarter.start(cameraId, listOf(holder.surface))
                this@CameraView.cameraStarter = cameraStarter
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraStarter?.release()
            }
        })
    }

    private companion object {
        private const val DEFAULT_CAMERA_ID = 0
    }
}