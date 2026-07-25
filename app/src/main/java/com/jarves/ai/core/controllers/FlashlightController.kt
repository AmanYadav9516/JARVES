package com.jarves.ai.core.controllers

import android.content.Context
import android.hardware.camera2.CameraManager

class FlashlightController(private val context: Context) {

    private var isFlashOn = false

    fun toggleFlashlight(on: Boolean): String {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        return try {
            val cameraId = cameraManager?.cameraIdList?.firstOrNull()
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, on)
                isFlashOn = on
                if (on) "Flashlight Turned ON" else "Flashlight Turned OFF"
            } else {
                "Flashlight hardware not found"
            }
        } catch (e: Exception) {
            "Flashlight error: ${e.localizedMessage}"
        }
    }
}
