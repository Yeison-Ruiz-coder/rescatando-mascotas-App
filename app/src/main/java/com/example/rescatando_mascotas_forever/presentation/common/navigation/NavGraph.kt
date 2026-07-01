package com.example.rescatando_mascotas_forever.presentation.common.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.presentation.auth.login.LoginScreen
import com.example.rescatando_mascotas_forever.presentation.auth.register.RegisterScreen
import com.example.rescatando_mascotas_forever.presentation.home.HomeScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.AdopcionListScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.MascotaDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.nosotros.NosotrosScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RegistroRescatistaScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.FormularioRescateScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.FormularioAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoViewModel
import com.example.rescatando_mascotas_forever.presentation.rescates.EncuestaRescateScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.ProcesoAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.splash.SplashScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.profile.ProfileScreen
import com.example.rescatando_mascotas_forever.presentation.settings.SettingsScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminHomeScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminFormularioAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminMascotasScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminEventosScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminDonacionesScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminUsuariosScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminReportesRescateScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminSuscripcionesScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminMascotaFormScreen
import com.example.rescatando_mascotas_forever.presentation.donaciones.DonacionesScreen
import com.example.rescatando_mascotas_forever.presentation.veterinarias.VeterinariaScreen
import com.example.rescatando_mascotas_forever.presentation.home.FoundationDetailScreen
import com.example.rescatando_mascotas_forever.presentation.home.FoundationListScreen
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SubscriptionScreen
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SuscripcionFormScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationHomeScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationMascotasScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.FoundationMascotaFormScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.rescates.FoundationRescatesScreen
import com.example.rescatando_mascotas_forever.presentation.fundacion.rescates.FoundationRescateDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.veterinarias.VeterinaryHomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("home") { HomeScreen(navController = navController) }
        
        composable("admin_home") { AdminHomeScreen(navController = navController) }
        composable("foundation_home") { FoundationHomeScreen(navController = navController) }
        composable("veterinary_home") { VeterinaryHomeScreen(navController = navController) }

        // --- GESTIÓN FUNDACIÓN ---
        composable("foundation_rescates") { FoundationRescatesScreen(navController = navController) }
        composable("foundation_rescates_near") { FoundationRescatesScreen(navController = navController) }
        composable("foundation_my_rescues") { FoundationRescatesScreen(navController = navController) }
        
        composable(
            route = "foundation_rescate_detail/{rescateId}",
            arguments = listOf(navArgument("rescateId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("rescateId") ?: 0
            FoundationRescateDetalleScreen(navController = navController, rescateId = id)
        }

        composable("foundation_pets") { FoundationMascotasScreen(navController = navController) }
        
        composable(
            route = "foundation_mascota_form?mascotaId={mascotaId}&rescateId={rescateId}",
            arguments = listOf(
                navArgument("mascotaId") { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument("rescateId") { type = NavType.StringType; nullable = true; defaultValue = null }
            )
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")?.toIntOrNull()
            val rescateId = backStackEntry.arguments?.getString("rescateId")?.toIntOrNull()
            FoundationMascotaFormScreen(navController = navController, mascotaId = mascotaId, rescateId = rescateId)
        }

        // ... RESTO DE RUTAS
        composable("adopciones") { AdopcionListScreen(navController = navController) }
        composable("eventos") { backStackEntry -> 
            val viewModel: EventoViewModel = viewModel(backStackEntry)
            EventoScreen(navController = navController, viewModel = viewModel) 
        }
        composable("donaciones") { DonacionesScreen(navController = navController) }
        composable("perfil") { ProfileScreen(navController = navController) }
    }
}
