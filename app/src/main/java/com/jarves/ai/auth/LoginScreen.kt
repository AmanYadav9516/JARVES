package com.jarves.ai.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jarves.ai.components.GlassCard
import com.jarves.ai.components.GlowingAiCore
import com.jarves.ai.theme.CyanPrimary
import com.jarves.ai.theme.CyanSecondary
import com.jarves.ai.theme.TextDim

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            GlowingAiCore(size = 120.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "JARVES AI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 4.sp
            )

            Text(
                text = "Sign in to activate your AI Assistant",
                fontSize = 13.sp,
                color = TextDim,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = authViewModel.email,
                        onValueChange = { authViewModel.email = it },
                        label = { Text("Email Address", color = TextDim) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = Color(0x3300F2FE),
                            focusedLabelColor = CyanPrimary,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = authViewModel.password,
                        onValueChange = { authViewModel.password = it },
                        label = { Text("Password", color = TextDim) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = Color(0x3300F2FE),
                            focusedLabelColor = CyanPrimary,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    authViewModel.errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { authViewModel.login() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(CyanPrimary, CyanSecondary)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = "ACTIVATE JARVES",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account? ", color = TextDim, fontSize = 14.sp)
                Text(
                    text = "Sign Up",
                    color = CyanPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}
