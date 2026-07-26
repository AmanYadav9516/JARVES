package com.jarves.ai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jarves.ai.auth.AuthViewModel
import com.jarves.ai.auth.LoginScreen
import com.jarves.ai.auth.SignUpScreen
import com.jarves.ai.theme.JarvesTheme
import com.jarves.ai.ui.HomeScreen
import com.jarves.ai.ui.SettingsScreen

class MainActivity : AppCompatActivity() {

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runCatching {
            requestJarvesPermissions()
        }

        setContent {
            JarvesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0D14)
                ) {
                    val authViewModel: AuthViewModel = viewModel()
                    var currentScreen by remember { mutableStateOf("home") }

                    if (!authViewModel.isLoggedIn) {
                        if (authViewModel.isSignUpMode) {
                            SignUpScreen(
                                authViewModel = authViewModel,
                                onNavigateToLogin = { authViewModel.isSignUpMode = false }
                            )
                        } else {
                            LoginScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignUp = { authViewModel.isSignUpMode = true }
                            )
                        }
                    } else {
                        when (currentScreen) {
                            "home" -> HomeScreen(
                                onNavigateToSettings = { currentScreen = "settings" }
                            )
                            "settings" -> SettingsScreen(
                                authViewModel = authViewModel,
                                onBackClick = { currentScreen = "home" }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestJarvesPermissions() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val ungranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (ungranted.isNotEmpty()) {
            requestPermissionsLauncher.launch(ungranted.toTypedArray())
        }
    }
}
