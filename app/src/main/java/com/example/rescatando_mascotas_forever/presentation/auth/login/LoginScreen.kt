package com.example.rescatando_mascotas_forever.presentation.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val mainGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF673AB7), // Deep Purple
            Color(0xFF3F51B5)  // Indigo
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        // Decoración de fondo (Círculos difusos)
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Surface(
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 15.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo_foreground),
                        contentDescription = "Logo",
                        modifier = Modifier.size(90.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.login_welcome),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(R.string.login_subtitle),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Card del Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Selector de Rol Moderno
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black.copy(alpha = 0.2f))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (!isAdmin) Color.White else Color.Transparent)
                                .clickable { isAdmin = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.login_role_user),
                                color = if (!isAdmin) Color(0xFF673AB7) else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isAdmin) Color.White else Color.Transparent)
                                .clickable { isAdmin = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.login_role_admin),
                                color = if (isAdmin) Color(0xFF673AB7) else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(stringResource(R.string.login_email_hint), color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.White) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text(stringResource(R.string.login_password_hint), color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        stringResource(R.string.login_forgot_password),
                        modifier = Modifier.align(Alignment.End),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Botón Entrar con Sombra
                    Button(
                        onClick = {
                            if (isAdmin) {
                                navController.navigate("admin_home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF673AB7)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.login_btn_enter),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { /* Navegar a registro */ }
            ) {
                Text(stringResource(R.string.login_no_account), color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                Text(stringResource(R.string.login_register_here), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
