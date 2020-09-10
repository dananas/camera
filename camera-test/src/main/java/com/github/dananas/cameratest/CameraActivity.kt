package com.github.dananas.cameratest

import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.github.dananas.camera.CameraStarter
import com.github.dananas.camera.CameraStarterFactory
import kotlinx.android.synthetic.main.camera_activity.*

class CameraActivity : AppCompatActivity(R.layout.camera_activity) {
    private var cameraStarter: CameraStarter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraStarter = CameraStarterFactory.create(this)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                cameraStarter?.start("0", listOf(holder.surface))
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraStarter?.stop()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        val holder = surfaceView.holder
        if (!holder.isCreating) {
            cameraStarter?.start("0", listOf(holder.surface))
        }
    }

    override fun onResume() {
        super.onResume()
        cameraStarter?.stop()
    }
}