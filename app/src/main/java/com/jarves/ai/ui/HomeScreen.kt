package com.jarves.ai.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jarves.ai.components.GlassCard
import com.jarves.ai.components.GlowingAiCore
import com.jarves.ai.components.JarvesHeader
import com.jarves.ai.core.engine.JarvesExecutionPipeline
import com.jarves.ai.core.engine.JarvesIntentParser
import com.jarves.ai.core.engine.JarvesVoiceManager
import com.jarves.ai.service.JarvesOverlayService
import com.jarves.ai.theme.CyanPrimary
import com.jarves.ai.theme.TextDim

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var isListening by remember { mutableStateOf(false) }
    var speechText by remember { mutableStateOf("Say 'Jarves' or tap the core to speak...") }
    var isOverlayEnabled by remember { mutableStateOf(false) }
    val logs = remember { mutableStateListOf<String>() }

    val intentParser = remember { JarvesIntentParser() }
    val executionPipeline = remember { JarvesExecutionPipeline(context) }

    val voiceManager = remember {
        JarvesVoiceManager(
            context = context,
            onResult = { text ->
                isListening = false
                speechText = text
                logs.add(0, "User: $text")

                val actions = intentParser.parseCommand(text)
                val result = executionPipeline.executeActions(actions)
                logs.add(0, "JARVES: $result")
            },
            onError = { err ->
                isListening = false
                speechText = "Listening error. Tap core to retry."
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
    ) {
        JarvesHeader(onSettingsClick = onNavigateToSettings)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Animated Glowing AI Core Voice Button
            GlowingAiCore(
                isListening = isListening,
                size = 170.dp,
                onClick = {
                    isListening = !isListening
                    if (isListening) {
                        speechText = "Listening (Hindi/English)..."
                        voiceManager.startListening()
                    } else {
                        voiceManager.stopListening()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Speech Transcript Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isListening) "LISTENING..." else "VOICE COMMAND",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanPrimary,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = speechText,
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Siri-Style Overlay Switch Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Siri Floating HUD Pop-Up",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Show animated JARVES core over all apps",
                            fontSize = 12.sp,
                            color = TextDim
                        )
                    }

                    Switch(
                        checked = isOverlayEnabled,
                        onCheckedChange = { enabled ->
                            isOverlayEnabled = enabled
                            if (enabled) {
                                if (Settings.canDrawOverlays(context)) {
                                    context.startService(Intent(context, JarvesOverlayService::class.java))
                                } else {
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:${context.packageName}")
                                    )
                                    context.startActivity(intent)
                                }
                            } else {
                                context.stopService(Intent(context, JarvesOverlayService::class.java))
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = CyanPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Voice Action Chips
            Text(
                text = "QUICK VOICE SHORTCUTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextDim,
                letterSpacing = 1.5.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Default.Call,
                    label = "Call Mummy",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val actions = intentParser.parseCommand("Call mummy")
                        executionPipeline.executeActions(actions)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(
                    icon = Icons.Default.CameraAlt,
                    label = "Open Camera",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val actions = intentParser.parseCommand("Open camera")
                        executionPipeline.executeActions(actions)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(
                    icon = Icons.Default.FlashOn,
                    label = "Torch On",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val actions = intentParser.parseCommand("on flashlight")
                        executionPipeline.executeActions(actions)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Voice Command Execution Log
            Text(
                text = "ACTIVITY LOG",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextDim,
                letterSpacing = 1.5.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(logs) { log ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0x22121824), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = log,
                            fontSize = 13.sp,
                            color = if (log.startsWith("User:")) CyanPrimary else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color(0x33121824), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x3300F2FE), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, tint = CyanPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}
