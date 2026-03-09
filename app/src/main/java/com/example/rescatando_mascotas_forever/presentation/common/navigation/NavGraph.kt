package com.example.rescatando_mascotas_forever.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rescatando_mascotas_forever.presentation.auth.login.LoginScreen
import com.example.rescatando_mascotas_forever.presentation.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login" // O "home" si ya tienes sesión
    ) {
        composable("login") {
            LoginScreen()
        }
        composable("home") {
            HomeScreen()
        }
        // Agrega aquí el resto de tus rutas (mascotas, tienda, etc.)
    }
}