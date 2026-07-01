package com.example.rescatando_mascotas_forever.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rescatando_mascotas_forever.presentation.auth.login.LoginScreen
import com.example.rescatando_mascotas_forever.presentation.auth.register.RegisterScreen
import com.example.rescatando_mascotas_forever.presentation.home.HomeScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.AdopcionListScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.MascotaDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.nosotros.NosotrosScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RescateScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RegistroRescatistaScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.FormularioRescateScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoViewModel
import com.example.rescatando_mascotas_forever.presentation.splash.SplashScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.profile.ProfileScreen
import com.example.rescatando_mascotas_forever.presentation.settings.SettingsScreen
import com.example.rescatando_mascotas_forever.presentation.admin.*
import com.example.rescatando_mascotas_forever.presentation.donaciones.DonacionesScreen
import com.example.rescatando_mascotas_forever.presentation.veterinarias.VeterinariaScreen
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SubscriptionScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationHomeScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationMascotasScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationMascotaFormScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.rescates.FoundationRescatesScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.rescates.FoundationRescateDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.veterinarias.VeterinaryHomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("home") { HomeScreen(navController = navController) }
        
        // --- ADMIN ---
        composable("admin_home") { AdminHomeScreen(navController = navController) }
        composable("admin_usuarios") { AdminUsuariosScreen(navController = navController) }
        composable("admin_mascotas") { AdminMascotasScreen(navController = navController) }
        composable("admin_suscripciones") { AdminSuscripcionesScreen(navController = navController) }
        composable("admin_reportes_rescate") { AdminReportesRescateScreen(navController = navController) }
        composable("admin_eventos") { AdminEventosScreen(navController = navController) }

        // --- FUNDACIÓN ---
        composable("foundation_home") { FoundationHomeScreen(navController = navController) }
        composable("foundation_rescates_near") { FoundationRescatesScreen(navController = navController) }
        composable("foundation_my_rescues") { FoundationRescatesScreen(navController = navController) }
        composable("foundation_pets") { FoundationMascotasScreen(navController = navController) }
        composable("foundation_mascota_form") { FoundationMascotaFormScreen(navController = navController) }
        
        composable("foundation_adoptions") { AdopcionListScreen(navController) }
        composable("foundation_adoptions_approved") { AdopcionListScreen(navController) }
        composable("foundation_adoptions_followup") { AdopcionListScreen(navController) }
        composable("foundation_events") { val vm: EventoViewModel = viewModel(); EventoScreen(navController, vm) }
        composable("foundation_subscriptions") { SubscriptionScreen(navController) }

        // --- USUARIO / GENERAL ---
        composable("perfil") { ProfileScreen(navController = navController) }
        composable("adopciones") { AdopcionListScreen(navController = navController) }
        composable("suscripciones") { SubscriptionScreen(navController = navController) }
        composable("donaciones") { DonacionesScreen(navController = navController) }
        
        composable("eventos") { backStackEntry -> 
            val vm: EventoViewModel = viewModel(backStackEntry)
            EventoScreen(navController, vm) 
        }

        composable(
            route = "evento_detalle/{eventoId}",
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("eventoId") ?: 0
            val viewModel: EventoViewModel = viewModel(backStackEntry)
            EventoDetalleScreen(navController = navController, eventoId = id, viewModel = viewModel)
        }

        composable("veterinarias") { VeterinariaScreen(navController = navController) }
        composable("nosotros") { NosotrosScreen(navController = navController) }
        composable("settings") { SettingsScreen(navController = navController) }
        composable("formulario_rescate") { FormularioRescateScreen(navController = navController) }
        
        composable(
            route = "mascota_detalle/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            MascotaDetalleScreen(navController = navController, mascotaId = id)
        }
    }
}
