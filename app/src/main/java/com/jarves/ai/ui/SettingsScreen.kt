package com.jarves.ai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jarves.ai.auth.AuthViewModel
import com.jarves.ai.components.GlassCard
import com.jarves.ai.theme.CyanPrimary
import com.jarves.ai.theme.TextDim
import com.jarves.ai.utils.UpdateManager

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val updateManager = remember { UpdateManager(context) }
    var updateText by remember { mutableStateOf("Tap to check for new APK updates") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "JARVES SETTINGS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Account Profile Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = "USER PROFILE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = authViewModel.name.ifEmpty { "JARVES Master User" }, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = authViewModel.email.ifEmpty { "user@jarves.ai" }, fontSize = 13.sp, color = TextDim)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Updates & GitHub Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = "VERSION & UPDATES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = updateText, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        updateManager.checkForUpdates { status -> updateText = status }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "CHECK GITHUB FOR UPDATES")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = { authViewModel.logout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF0055)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "SIGN OUT", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}
