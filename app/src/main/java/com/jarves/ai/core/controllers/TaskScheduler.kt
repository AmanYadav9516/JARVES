package com.jarves.ai.core.controllers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import android.telephony.SmsManager
import androidx.work.*
import java.util.concurrent.TimeUnit

class TaskScheduler(private val context: Context) {

    fun setAlarm(hour: Int, minute: Int, message: String = "JARVES Alarm"): String {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            context.startActivity(intent)
            "Setting alarm for $hour:$minute..."
        } catch (e: Exception) {
            "Failed to set alarm: ${e.localizedMessage}"
        }
    }

    fun scheduleDelayedSms(contactOrNumber: String, messageText: String, delayMinutes: Long): String {
        val data = workDataOf(
            "KEY_CONTACT" to contactOrNumber,
            "KEY_MESSAGE" to messageText
        )

        val smsWorkRequest = OneTimeWorkRequestBuilder<SmsWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(smsWorkRequest)
        return "Scheduled SMS '$messageText' to $contactOrNumber in $delayMinutes minutes."
    }

    fun scheduleReminder(reminderText: String, delayMinutes: Long): String {
        val data = workDataOf("KEY_REMINDER" to reminderText)

        val reminderWork = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(reminderWork)
        return "Reminder set for '$reminderText' in $delayMinutes minutes."
    }
}

// Background Worker for Delayed SMS
class SmsWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val contact = inputData.getString("KEY_CONTACT") ?: return Result.failure()
        val message = inputData.getString("KEY_MESSAGE") ?: "HII"

        return try {
            val smsManager = applicationContext.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(contact, null, message, null, null)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

// Background Worker for Reminders
class ReminderWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val reminder = inputData.getString("KEY_REMINDER") ?: "Reminder Alert!"
        // Show Notification or TTS alert
        return Result.success()
    }
}
