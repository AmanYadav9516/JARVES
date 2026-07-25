package com.jarves.ai.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isSignUpMode by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter email and password"
            return
        }
        // Firebase Auth integration
        isLoggedIn = true
        errorMessage = null
    }

    fun signUp() {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            errorMessage = "Please fill in all details"
            return
        }
        // Firebase Auth creation
        isLoggedIn = true
        errorMessage = null
    }

    fun logout() {
        isLoggedIn = false
        email = ""
        password = ""
    }
}
