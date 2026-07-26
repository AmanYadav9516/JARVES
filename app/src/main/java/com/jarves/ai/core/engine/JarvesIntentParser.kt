package com.jarves.ai.core.engine

import java.util.regex.Pattern

class JarvesIntentParser {

    fun parseCommand(userSpeech: String): List<JarvesAction> {
        val actions = mutableListOf<JarvesAction>()
        val text = userSpeech.trim().lowercase()

        // Handle compound multitasking sentences split by "और", "उसके बाद", "then", "and then", "and"
        val segments = text.split(Regex("और उसके बाद|और फिर|उसके बाद|और|then|and then"))

        for (segment in segments) {
            val cmd = segment.trim()
            if (cmd.isEmpty()) continue

            when {
                // Call Command
                cmd.contains("call") || cmd.contains("कॉल") -> {
                    val contact = extractContact(cmd)
                    actions.add(JarvesAction.MakeCall(contact))
                }

                // Camera & Photo/Video
                cmd.contains("video recording") || cmd.contains("वीडियो रिकॉर्डिंग") -> {
                    actions.add(JarvesAction.StartVideoRecording)
                }
                cmd.contains("click a photo") || cmd.contains("photo click") || cmd.contains("फोटो खींचो") -> {
                    actions.add(JarvesAction.ClickPhoto)
                }
                cmd.contains("camera") || cmd.contains("कैमरा") -> {
                    actions.add(JarvesAction.OpenCamera)
                }

                // Flashlight
                cmd.contains("flashlight") || cmd.contains("लाइट") || cmd.contains("torch") -> {
                    val enable = !cmd.contains("off") && !cmd.contains("बंद")
                    actions.add(JarvesAction.ToggleFlashlight(enable))
                }

                // Battery Status
                cmd.contains("battery") || cmd.contains("बैटरी") -> {
                    actions.add(JarvesAction.GetBatteryStatus)
                }

                // YouTube
                cmd.contains("youtube") || cmd.contains("गाने चलाओ") || cmd.contains("play song") || cmd.contains("संगीत") -> {
                    val query = extractYouTubeQuery(cmd)
                    actions.add(JarvesAction.PlayYouTube(query))
                }

                // Alarm
                cmd.contains("alarm") || cmd.contains("अलार्म") -> {
                    val (hour, min) = parseTime(cmd)
                    actions.add(JarvesAction.SetAlarm(hour, min, "JARVES Alarm"))
                }

                // Scheduled SMS
                cmd.contains("sms") || cmd.contains("मैसेज") -> {
                    val minutes = extractMinutes(cmd)
                    val contact = extractContact(cmd)
                    val message = extractSmsBody(cmd)
                    actions.add(JarvesAction.ScheduleSms(contact, message, minutes))
                }

                // Reminder
                cmd.contains("reminder") || cmd.contains("याद दिलाना") -> {
                    val minutes = extractMinutes(cmd)
                    val textMsg = extractReminderText(cmd)
                    actions.add(JarvesAction.SetReminder(textMsg, minutes))
                }

                // App Launchers (Settings, Gallery, File Manager, WhatsApp, etc.)
                cmd.contains("open") || cmd.contains("खोलो") || cmd.contains("चलाओ") -> {
                    val appName = extractAppName(cmd)
                    actions.add(JarvesAction.LaunchApp(appName))
                }

                else -> {
                    actions.add(JarvesAction.GeneralQuery(cmd))
                }
            }
        }

        return actions
    }

    private fun extractContact(text: String): String {
        return when {
            text.contains("mummy") || text.contains("मम्मी") -> "Mummy"
            text.contains("papa") || text.contains("पापा") -> "Papa"
            text.contains("bhai") || text.contains("भाई") -> "Bhai"
            else -> "Mummy"
        }
    }

    private fun extractYouTubeQuery(text: String): String {
        return text.replace("youtube पर", "")
            .replace("youtube", "")
            .replace("चलाओ", "")
            .replace("play", "")
            .replace("song", "")
            .replace("music", "")
            .replace("गाने", "")
            .trim()
            .ifEmpty { "Arijit Singh songs" }
    }

    private fun extractAppName(text: String): String {
        return text.replace("open", "")
            .replace("खोलो", "")
            .replace("ऐप", "")
            .replace("app", "")
            .trim()
            .ifEmpty { "settings" }
    }

    private fun extractMinutes(text: String): Long {
        val pattern = Pattern.compile("(\\d+)\\s*(mintus|minutes|min|मिनट)")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            matcher.group(1)?.toLongOrNull() ?: 30L
        } else {
            30L
        }
    }

    private fun parseTime(text: String): Pair<Int, Int> {
        val pattern = Pattern.compile("(\\d{1,2})")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            val hour = matcher.group(1)?.toIntOrNull() ?: 6
            Pair(hour, 0)
        } else {
            Pair(6, 0)
        }
    }

    private fun extractSmsBody(text: String): String {
        val pattern = Pattern.compile("['\"]([^'\"]+)['\"]")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            matcher.group(1) ?: "HII"
        } else {
            "HII"
        }
    }

    private fun extractReminderText(text: String): String {
        return text.replace("reminder me", "")
            .replace("remind me", "")
            .replace("after 30 mintus", "")
            .trim()
            .ifEmpty { "JARVES Reminder" }
    }
}
