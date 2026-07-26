package com.jarves.ai.core.engine

import android.content.Context
import com.jarves.ai.core.controllers.*

class JarvesExecutionPipeline(private val context: Context) {

    private val callController = CallController(context)
    private val cameraController = CameraController(context)
    private val flashlightController = FlashlightController(context)
    private val batteryController = BatteryController(context)
    private val youtubeController = YouTubeController(context)
    private val taskScheduler = TaskScheduler(context)
    private val appLauncherController = AppLauncherController(context)
    private val ttsManager = JarvesTTSManager(context)

    fun executeActions(actions: List<JarvesAction>): String {
        val results = mutableListOf<String>()

        for (action in actions) {
            val response = when (action) {
                is JarvesAction.MakeCall -> {
                    callController.makeCall(action.target)
                }
                is JarvesAction.OpenCamera -> {
                    cameraController.openCamera()
                }
                is JarvesAction.ClickPhoto -> {
                    cameraController.clickPhoto()
                }
                is JarvesAction.StartVideoRecording -> {
                    cameraController.startVideoRecording()
                }
                is JarvesAction.ToggleFlashlight -> {
                    flashlightController.toggleFlashlight(action.enable)
                }
                is JarvesAction.GetBatteryStatus -> {
                    batteryController.getBatteryStatus()
                }
                is JarvesAction.PlayYouTube -> {
                    youtubeController.playOnYouTube(action.query)
                }
                is JarvesAction.SetAlarm -> {
                    taskScheduler.setAlarm(action.hour, action.minute, action.label)
                }
                is JarvesAction.ScheduleSms -> {
                    taskScheduler.scheduleDelayedSms(action.contact, action.message, action.delayMinutes)
                }
                is JarvesAction.SetReminder -> {
                    taskScheduler.scheduleReminder(action.message, action.delayMinutes)
                }
                is JarvesAction.LaunchApp -> {
                    appLauncherController.launchApp(action.appName)
                }
                is JarvesAction.GeneralQuery -> {
                    "Sir, processing '${action.prompt}'..."
                }
            }

            results.add(response)
            ttsManager.speak(response)
        }

        return results.joinToString("\n")
    }

    fun shutdown() {
        ttsManager.shutdown()
    }
}
