package com.jarves.ai.core.controllers

import android.content.Context
import android.content.Intent
import android.net.Uri

class YouTubeController(private val context: Context) {

    fun playOnYouTube(query: String): String {
        val encodedQuery = Uri.encode(query)
        val appUri = Uri.parse("vnd.youtube://results?q=$encodedQuery")
        val webUri = Uri.parse("https://www.youtube.com/results?search_query=$encodedQuery")

        val appIntent = Intent(Intent.ACTION_VIEW, appUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return try {
            context.startActivity(appIntent)
            "Playing '$query' on YouTube..."
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
            "Opening YouTube for '$query'..."
        }
    }
}
