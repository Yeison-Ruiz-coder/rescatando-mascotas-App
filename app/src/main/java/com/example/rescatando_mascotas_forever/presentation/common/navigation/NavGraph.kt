package com.example.rescatando_mascotas_forever.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rescatando_mascotas_forever.presentation.auth.login.LoginScreen
import com.example.rescatando_mascotas_forever.presentation.home.HomeScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.AdopcionListScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RescateScreen
import com.example.rescatando_mascotas_forever.presentation.nosotros.NosotrosScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RegistroRescatistaScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.FormularioRescateScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.FormularioAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoScreen
import com.example.rescatando_mascotas_forever.presentation.rescatistas.RescatistaContactosScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.EncuestaRescateScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.ProcesoAdopcionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home" // Cambiado a home para facilitar tus pruebas
    ) {
        composable("login") {
            LoginScreen()
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("adopciones") {
            AdopcionListScreen(navController = navController)
        }
        composable("ultimos_rescates") {
            RescateScreen(navController = navController)
        }
        composable("nosotros") {
            NosotrosScreen(navController = navController)
        }
        composable("registro_rescatista") {
            RegistroRescatistaScreen(navController = navController)
        }
        composable("formulario_rescate") {
            FormularioRescateScreen(navController = navController)
        }
        composable("formulario_adopcion") {
            FormularioAdopcionScreen(navController = navController)
        }
        composable("eventos") {
            EventoScreen(navController = navController)
        }
        composable("rescatista_contactos") {
            RescatistaContactosScreen(navController = navController)
        }
        composable("encuesta_rescate") {
            EncuestaRescateScreen(navController = navController)
        }
        composable("proceso_adopcion") {
            ProcesoAdopcionScreen(navController = navController)
        }
    }
}
