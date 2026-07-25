package com.jarves.ai.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.jarves.ai.JarvesApplication
import com.jarves.ai.components.GlowingAiCore
import com.jarves.ai.core.engine.JarvesExecutionPipeline
import com.jarves.ai.core.engine.JarvesIntentParser
import com.jarves.ai.core.engine.JarvesVoiceManager
import com.jarves.ai.theme.JarvesTheme

class JarvesOverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private lateinit var voiceManager: JarvesVoiceManager
    private lateinit var intentParser: JarvesIntentParser
    private lateinit var executionPipeline: JarvesExecutionPipeline

    override fun onCreate() {
        super.onCreate()
        startForegroundService()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        intentParser = JarvesIntentParser()
        executionPipeline = JarvesExecutionPipeline(this)

        voiceManager = JarvesVoiceManager(
            context = this,
            onResult = { speech ->
                val actions = intentParser.parseCommand(speech)
                executionPipeline.executeActions(actions)
            },
            onError = { error -> }
        )

        setupFloatingOverlay()
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, JarvesApplication.CHANNEL_ID_OVERLAY)
            .setContentTitle("JARVES AI Active")
            .setContentText("Listening for wake word 'Jarves'")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()

        startForeground(1001, notification)
    }

    private fun setupFloatingOverlay() {
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            y = 120
        }

        val composeView = ComposeView(this).apply {
            setContent {
                JarvesTheme {
                    GlowingAiCore(
                        isListening = true,
                        onClick = {
                            voiceManager.startListening()
                        }
                    )
                }
            }
        }

        overlayView = composeView
        try {
            windowManager?.addView(overlayView, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            windowManager?.removeView(overlayView)
        }
        voiceManager.destroy()
        executionPipeline.shutdown()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
