package com.example.rescatando_mascotas_forever.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.example.rescatando_mascotas_forever.presentation.splash.SplashScreen
import com.example.rescatando_mascotas_forever.presentation.profile.ProfileScreen
import com.example.rescatando_mascotas_forever.presentation.settings.SettingsScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminHomeScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminFormularioAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminMascotasScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminEventosScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminDonacionesScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminUsuariosScreen
import com.example.rescatando_mascotas_forever.presentation.admin.AdminReportesRescateScreen
import com.example.rescatando_mascotas_forever.presentation.donaciones.DonacionesScreen
import com.example.rescatando_mascotas_forever.presentation.tienda.TiendaScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("admin_home") {
            AdminHomeScreen(navController = navController)
        }
        
        // RUTAS PÚBLICAS
        composable(
            route = "adopciones?especie={especie}",
            arguments = listOf(navArgument("especie") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null 
            })
        ) { backStackEntry ->
            val especie = backStackEntry.arguments?.getString("especie")
            AdopcionListScreen(navController = navController, especie = especie)
        }

        composable("tienda") {
            TiendaScreen(navController = navController)
        }

        composable("ultimos_rescates") {
            RescateScreen(navController = navController)
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
        composable("donaciones") {
            DonacionesScreen(navController = navController)
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
        composable("perfil") {
            ProfileScreen(navController = navController)
        }
        composable("configuracion") {
            SettingsScreen(navController = navController)
        }
        composable("proceso_adopcion") {
            ProcesoAdopcionScreen(navController = navController)
        }
        composable("nosotros") {
            NosotrosScreen(navController = navController)
        }

        // RUTAS EXCLUSIVAS ADMIN
        composable("admin_registro_rescatista") {
            RegistroRescatistaScreen(navController = navController)
        }
        composable("admin_encuesta_rescate") {
            EncuestaRescateScreen(navController = navController)
        }
        composable("admin_formulario_rescate") {
            FormularioRescateScreen(navController = navController)
        }
        composable("admin_formulario_adopcion") {
            AdminFormularioAdopcionScreen(navController = navController)
        }
        composable("admin_mascotas") {
            AdminMascotasScreen(navController = navController)
        }
        composable("admin_eventos") {
            AdminEventosScreen(navController = navController)
        }
        composable("admin_donaciones") {
            AdminDonacionesScreen(navController = navController)
        }
        composable("admin_usuarios") {
            AdminUsuariosScreen(navController = navController)
        }
        composable("admin_reportes_rescate") {
            AdminReportesRescateScreen(navController = navController)
        }
    }
}
