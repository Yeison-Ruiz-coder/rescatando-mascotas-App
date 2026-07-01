package com.example.rescatando_mascotas_forever.presentation.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Animaciones de entrada
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "alpha"
    )

    // Animación de pulso continuo para el logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)

        try {
            if (sessionManager.isLoggedIn()) {
                val token = sessionManager.getToken()
                RetrofitClient.setToken(token)

                val user = sessionManager.getUser()
                val destino = if (user?.tipo == "admin") "admin_home" else "home"

                navController.navigate(destino) {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        } catch (e: Exception) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WebPrimary,
                        WebSecondary,
                        BrandPurpleDark
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decoración de fondo (Círculos difusos para profundidad)
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-150).dp)
                .alpha(0.1f)
                .background(Color.White, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 150.dp, y = 200.dp)
                .alpha(0.05f)
                .background(Color.White, CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Contenedor del Logo con fondo blanco y logo con zoom para mayor tamaño
            Surface(
                modifier = Modifier
                    .size(210.dp)
                    .scale(scale * pulseScale)
                    .alpha(alpha),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 15.dp,
                tonalElevation = 5.dp
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.5f), // Aumentamos el zoom al 50% para que el logo sea aún más grande
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Títulos con animación de entrada
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(alpha)
                    .offset(y = if (startAnimation) 0.dp else 20.dp)
            ) {
                Text(
                    text = "RESCATANDO MASCOTAS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                
                Text(
                    text = "FOREVER",
                    color = WebAccent, // Color de acento para resaltar
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sanando su historia",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }

        // Indicador de carga sutil en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .alpha(alpha)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = Color.White.copy(alpha = 0.5f),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Cargando esperanza...",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
