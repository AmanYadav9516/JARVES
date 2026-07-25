package com.jarves.ai.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class UpdateManager(private val context: Context) {

    private val currentVersion = "1.0.0"
    private val githubRepoUrl = "https://github.com/your-username/JARVES/releases/latest"

    fun checkForUpdates(onUpdateStatus: (String) -> Unit) {
        // Simulates checking GitHub releases API for latest APK release
        onUpdateStatus("You are running JARVES v$currentVersion (Latest Version)")
    }

    fun openDownloadPage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubRepoUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
