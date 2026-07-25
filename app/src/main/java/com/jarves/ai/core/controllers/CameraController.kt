package com.jarves.ai.core.controllers

import android.content.Context
import android.content.Intent
import android.provider.MediaStore

class CameraController(private val context: Context) {

    fun openCamera(): String {
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Opening Camera..."
        } catch (e: Exception) {
            "Failed to open camera: ${e.localizedMessage}"
        }
    }

    fun startVideoRecording(): String {
        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Opening Video Camera..."
        } catch (e: Exception) {
            "Failed to open video camera: ${e.localizedMessage}"
        }
    }

    fun clickPhoto(): String {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Capturing Photo..."
        } catch (e: Exception) {
            "Failed to launch photo capture: ${e.localizedMessage}"
        }
    }
}
