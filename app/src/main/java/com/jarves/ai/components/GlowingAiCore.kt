package com.jarves.ai.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlowingAiCore(
    modifier: Modifier = Modifier,
    isListening: Boolean = false,
    size: Dp = 160.dp,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = if (isListening) 1.25f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isListening) 600 else 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radius"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(size)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = this.center
            val baseRadius = (size.toPx() / 2) * 0.7f

            // Outer Glowing Aura Ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isListening) Color(0xFF00F2FE) else Color(0xFF4FACFE),
                        Color(0x337F00FF),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * pulseRadius * 1.3f
                ),
                radius = baseRadius * pulseRadius * 1.3f,
                center = center
            )

            // Dynamic Rotating Ring 1
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(Color(0xFF00F2FE), Color(0xFF7F00FF), Color(0xFF00F2FE))
                ),
                radius = baseRadius * pulseRadius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )

            // Inner Pulsing Core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        if (isListening) Color(0xFF00F2FE) else Color(0xFF4FACFE),
                        Color(0xFF0A0D14)
                    )
                ),
                radius = baseRadius * 0.55f * pulseRadius,
                center = center
            )
        }
    }
}
