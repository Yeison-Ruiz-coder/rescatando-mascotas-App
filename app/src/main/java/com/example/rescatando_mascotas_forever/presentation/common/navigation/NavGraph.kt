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
    }
}
