package com.example.rescatando_mascotas_forever.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.local.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            val user = (state as LoginState.Success).user
            when (user.tipo?.lowercase()) {
                "admin" -> {
                    navController.navigate("admin_home") { popUpTo("login") { inclusive = true } }
                }
                "fundacion" -> {
                    navController.navigate("foundation_home") { popUpTo("login") { inclusive = true } }
                }
                "veterinaria" -> {
                    navController.navigate("veterinary_home") { popUpTo("login") { inclusive = true } }
                }
                else -> {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                }
            }
        } else if (state is LoginState.Error) {
            loginError = (state as LoginState.Error).message
            snackbarHostState.showSnackbar(loginError ?: "Error desconocido")
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ... (resto del fondo)
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=1000&auto=format&fit=crop"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(modifier = Modifier.fillMaxSize()) {
            // 2. Contenido Superior (Formulario)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Entrar",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Campo Email estilo Glass
                TransparentInput(
                    value = email,
                    onValueChange = { 
                        email = it
                        loginError = null 
                    },
                    label = "CORREO ELECTRÓNICO",
                    icon = Icons.Default.Email,
                    isError = loginError?.contains("Correo", ignoreCase = true) == true || loginError?.contains("email", ignoreCase = true) == true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Contraseña estilo Glass
                TransparentInput(
                    value = password,
                    onValueChange = { 
                        password = it
                        loginError = null
                    },
                    label = "CONTRASEÑA",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onTogglePassword = { passwordVisible = !passwordVisible },
                    isError = loginError?.contains("contraseña", ignoreCase = true) == true || loginError?.contains("password", ignoreCase = true) == true || loginError?.contains("Credenciales", ignoreCase = true) == true
                )

                if (loginError != null) {
                    Text(
                        text = loginError!!,
                        color = Color(0xFFFF5252),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                    )
                }

                Text(
                    text = "¿LA OLVIDASTE?",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 10.dp, end = 4.dp)
                        .clickable { /* Accion */ }
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Botón Principal
                Button(
                    onClick = { viewModel.login(email, password, sessionManager) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (state is LoginState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("INICIAR SESIÓN", fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divisor "O entra con"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
                    Text(
                        text = "O ENTRA CON",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Google
                OutlinedButton(
                    onClick = { viewModel.loginWithGoogle(context, sessionManager) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Icono representativo (Podrías usar una imagen de Google después)
                        Icon(
                            imageVector = Icons.Default.AccountCircle, 
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("CONTINUAR CON GOOGLE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón Invitado
                TextButton(
                    onClick = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("CONTINUAR COMO INVITADO", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // 3. Tarjeta Inferior de Registro
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clickable { navController.navigate("register") },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Barrita superior estética (Handle)
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    )
                    
                    Row(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¿Eres nuevo?",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Regístrate",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.padding(10.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
        
        // Snackbar Host en la parte superior para visibilidad
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 130.dp)
        )
    }
}

@Composable
fun TransparentInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
    isError: Boolean = false
) {
    Column {
        Text(
            text = label,
            color = if (isError) Color(0xFFFF5252) else Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            leadingIcon = { Icon(icon, null, tint = if (isError) Color(0xFFFF5252) else Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp)) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onTogglePassword) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = if (isError) Color(0xFFFF5252) else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                focusedBorderColor = if (isError) Color(0xFFFF5252) else Color.White.copy(alpha = 0.3f),
                unfocusedBorderColor = if (isError) Color(0xFFFF5252).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
                errorBorderColor = Color(0xFFFF5252),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}
