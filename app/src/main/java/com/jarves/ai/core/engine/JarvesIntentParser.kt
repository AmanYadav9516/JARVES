package com.jarves.ai.core.engine

import java.util.regex.Pattern

class JarvesIntentParser {

    fun parseCommand(userSpeech: String): List<JarvesAction> {
        val actions = mutableListOf<JarvesAction>()
        val text = userSpeech.trim().lowercase()

        val segments = text.split(Regex("और उसके बाद|और फिर|उसके बाद|और|then|and then"))

        for (segment in segments) {
            val cmd = segment.trim()
            if (cmd.isEmpty()) continue

            when {
                // Money Recording & Querying
                cmd.contains("recive money") || cmd.contains("receive money") || cmd.contains("लेने हैं") || cmd.contains("दिए") -> {
                    val amount = extractAmount(cmd)
                    val person = extractPersonName(cmd)
                    actions.add(JarvesAction.RecordMoney(person, amount, isReceive = true))
                }
                cmd.contains("how much money") || cmd.contains("कितने रुपये") || cmd.contains("कितने पैसे") -> {
                    val person = extractPersonName(cmd)
                    actions.add(JarvesAction.QueryMoney(person))
                }

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

                // Reminder (Support custom minutes e.g. 45 min, 10 min, 2 hours)
                cmd.contains("reminde") || cmd.contains("remind") || cmd.contains("याद दिलाना") || cmd.contains("रिमाइंडर") -> {
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

    private fun extractAmount(text: String): Double {
        val pattern = Pattern.compile("(\\d+(\\.\\d+)?)")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            matcher.group(1)?.toDoubleOrNull() ?: 349.0
        } else {
            349.0
        }
    }

    private fun extractPersonName(text: String): String {
        return when {
            text.contains("mohan") || text.contains("मोहन") -> "Mohan"
            text.contains("sohan") || text.contains("सोहन") -> "Sohan"
            text.contains("ram") || text.contains("राम") -> "Ram"
            text.contains("mummy") || text.contains("मम्मी") -> "Mummy"
            else -> {
                val words = text.split(" ")
                val fromIndex = words.indexOf("from")
                if (fromIndex != -1 && fromIndex + 1 < words.size) {
                    words[fromIndex + 1]
                } else {
                    "Mohan"
                }
            }
        }
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
        val pattern = Pattern.compile("(\\d+)\\s*(mintus|minutes|min|minute|मिनट)")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            matcher.group(1)?.toLongOrNull() ?: 45L
        } else {
            45L
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
        return text.replace("reminde me after", "")
            .replace("remind me after", "")
            .replace("reminde me", "")
            .replace("remind me", "")
            .replace("after 45 minute", "")
            .replace("after 30 minute", "")
            .replace("minute", "")
            .replace("mintus", "")
            .replace("minutes", "")
            .replace("min", "")
            .replace(Regex("\\d+"), "")
            .replace("to go market", "Go to market")
            .trim()
            .ifEmpty { "Go to market" }
    }
}
