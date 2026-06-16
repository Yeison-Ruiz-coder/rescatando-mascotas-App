package com.example.rescatando_mascotas_forever.presentation.auth.register

import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("user") } // Nuevo: tipo de usuario
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(state) {
        if (state is RegisterState.Success) {
            snackbarHostState.showSnackbar("¡Registro exitoso!")
            navController.navigate("home") {
                popUpTo("register") { inclusive = true }
            }
        } else if (state is RegisterState.Error) {
            snackbarHostState.showSnackbar((state as RegisterState.Error).message)
            viewModel.resetState()
        }
    }

    val mainGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Text(
                    text = "Crear Cuenta",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Selector de Tipo de Usuario (Nuevo)
                        Text("Tipo de perfil:", color = Color.White, fontSize = 14.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("user" to "Usuario", "fundacion" to "Fundación", "veterinaria" to "Vet").forEach { (value, label) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = tipo == value,
                                        onClick = { tipo = value },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color.White, unselectedColor = Color.White.copy(alpha = 0.6f))
                                    )
                                    Text(label, color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Nombre
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre Completo", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.White) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White)
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confirmar Password
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar Contraseña", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                                viewModel.register(
                                    nombre = name,
                                    email = email,
                                    password = password,
                                    confirmPass = confirmPassword,
                                    tipo = tipo, // Enviamos el tipo seleccionado
                                    sessionManager = sessionManager
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            enabled = state !is RegisterState.Loading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF673AB7)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            if (state is RegisterState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF673AB7))
                            } else {
                                Text("REGISTRARME", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
