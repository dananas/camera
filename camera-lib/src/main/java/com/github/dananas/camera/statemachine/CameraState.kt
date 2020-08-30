package com.github.dananas.camera.statemachine

internal abstract class CameraState {
    open fun onEnter() {}
    open fun onAction(action: CameraAction) {}

    override fun toString(): String {
        return javaClass.simpleName
    }
}