package com.jarves.ai.core.controllers

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

class AppLauncherController(private val context: Context) {

    fun launchApp(appName: String): String {
        val name = appName.trim().lowercase()

        return when {
            name.contains("setting") || name.contains("सेटिंग") -> {
                openSettings()
            }
            name.contains("gallery") || name.contains("photo") || name.contains("गैलरी") -> {
                openGallery()
            }
            name.contains("file") || name.contains("manager") || name.contains("फाइल") -> {
                openFileManager()
            }
            name.contains("calculator") || name.contains("कैलकुलेटर") -> {
                openCalculator()
            }
            else -> {
                launchInstalledAppByName(appName)
            }
        }
    }

    fun openSettings(): String {
        val intent = Intent(Settings.ACTION_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Opening System Settings..."
        } catch (e: Exception) {
            "Failed to open Settings: ${e.localizedMessage}"
        }
    }

    fun openGallery(): String {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Opening Gallery..."
        } catch (e: Exception) {
            "Failed to open Gallery"
        }
    }

    fun openFileManager(): String {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Opening File Manager..."
        } catch (e: Exception) {
            "Failed to open File Manager"
        }
    }

    fun openCalculator(): String {
        val packages = listOf(
            "com.google.android.calculator",
            "com.android.calculator2",
            "com.sec.android.app.popupcalculator",
            "com.miui.calculator"
        )
        for (pkg in packages) {
            val intent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (intent != null) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                return "Opening Calculator..."
            }
        }
        return "Calculator app not found"
    }

    private fun launchInstalledAppByName(appName: String): String {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        val targetApp = packages.firstOrNull { app ->
            val label = pm.getApplicationLabel(app).toString().lowercase()
            label.contains(appName.lowercase())
        }

        return if (targetApp != null) {
            val launchIntent = pm.getLaunchIntentForPackage(targetApp.packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (launchIntent != null) {
                context.startActivity(launchIntent)
                "Opening ${pm.getApplicationLabel(targetApp)}..."
            } else {
                "Unable to launch $appName"
            }
        } else {
            "App '$appName' not found on device"
        }
    }
}
