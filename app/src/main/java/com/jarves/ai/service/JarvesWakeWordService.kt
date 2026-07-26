package com.jarves.ai.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.jarves.ai.JarvesApplication

class JarvesWakeWordService : Service() {

    override fun onCreate() {
        super.onCreate()
        runCatching {
            startForegroundService()
        }
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, JarvesApplication.CHANNEL_ID_OVERLAY)
            .setContentTitle("JARVES Wake-Word Listener")
            .setContentText("Listening for 'Jarves'...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(1002, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
        } else {
            startForeground(1002, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
