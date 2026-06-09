package com.example.rescatando_mascotas_forever.presentation.auth.register

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.example.rescatando_mascotas_forever.data.service.RetrofitClient
import com.example.rescatando_mascotas_forever.data.service.LoginRequest
import com.example.rescatando_mascotas_forever.utils.TokenManager
import com.example.rescatando_mascotas_forever.data.service.RegisterRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
        // Decoración de fondo
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 150.dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Crear Cuenta",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Únete a nuestra comunidad de rescate",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

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
                    // Campo Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text(
                                "Nombre completo",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                "Correo electrónico",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
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
                        placeholder = {
                            Text(
                                "Contraseña",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmar Contraseña
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = {
                            Text(
                                "Confirmar contraseña",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    Button(

                        onClick = {
                            Log.d("REGISTER_DEBUG", "email=$email password=$password")
                            scope.launch {

                                try {

                                    val api = RetrofitClient.create(context)

                                    val response = api.register(
                                        RegisterRequest(
                                            name = name,
                                            email = email,
                                            password = password,
                                            password_confirmation = confirmPassword
                                        )
                                    )

                                    if (response.isSuccessful) {

                                        val token = response.body()?.token

                                        if (token != null) {

                                            val tokenManager = TokenManager(context)
                                            tokenManager.saveToken(token)

                                            Toast.makeText(
                                                context,
                                                "Login exitoso",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            navController.navigate("home")
                                        }

                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Error de login",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } catch (e: Exception) {

                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
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

                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp
                        )

                    ) {

                        Text(
                            text = "REGISTRARME",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    ///hasta aqui
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { navController.popBackStack() }
            ) {
                Text("¿Ya tienes cuenta? ", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                Text("Inicia sesión", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
