package com.jarves.ai.core.engine

sealed class JarvesAction {
    data class MakeCall(val target: String) : JarvesAction()
    object OpenCamera : JarvesAction()
    object ClickPhoto : JarvesAction()
    object StartVideoRecording : JarvesAction()
    data class ToggleFlashlight(val enable: Boolean) : JarvesAction()
    object GetBatteryStatus : JarvesAction()
    data class PlayYouTube(val query: String) : JarvesAction()
    data class SetAlarm(val hour: Int, val minute: Int, val label: String) : JarvesAction()
    data class ScheduleSms(val contact: String, val message: String, val delayMinutes: Long) : JarvesAction()
    data class SetReminder(val message: String, val delayMinutes: Long) : JarvesAction()
    data class LaunchApp(val appName: String) : JarvesAction()
    data class GeneralQuery(val prompt: String) : JarvesAction()
}
