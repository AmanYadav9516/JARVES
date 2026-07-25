package com.jarves.ai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class JarvesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_OVERLAY,
                "JARVES AI Voice Listener",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows active JARVES voice assistant overlay status"
            }

            val taskChannel = NotificationChannel(
                CHANNEL_ID_TASKS,
                "JARVES Delayed Tasks & Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for scheduled SMS, alarms, and reminders"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            notificationManager?.createNotificationChannel(taskChannel)
        }
    }

    companion object {
        const val CHANNEL_ID_OVERLAY = "jarves_overlay_channel"
        const val CHANNEL_ID_TASKS = "jarves_tasks_channel"
    }
}
