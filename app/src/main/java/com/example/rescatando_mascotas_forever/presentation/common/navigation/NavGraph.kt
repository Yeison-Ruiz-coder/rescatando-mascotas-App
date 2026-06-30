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
import com.example.rescatando_mascotas_forever.presentation.rescates.RescateScreen
import com.example.rescatando_mascotas_forever.presentation.nosotros.NosotrosScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.RegistroRescatistaScreen
import com.example.rescatando_mascotas_forever.presentation.rescates.FormularioRescateScreen
import com.example.rescatando_mascotas_forever.presentation.adopciones.FormularioAdopcionScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoDetalleScreen
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoViewModel
import com.example.rescatando_mascotas_forever.presentation.rescatistas.RescatistaContactosScreen
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

@Composable
fun LoginRequiredScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Función exclusiva",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Para realizar esta acción debes tener una cuenta.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver atrás", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(
            route = "admin_mascota_form?mascotaId={mascotaId}",
            arguments = listOf(navArgument("mascotaId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: -1
            AdminMascotaFormScreen(
                navController = navController,
                mascotaId = if (id == -1) null else id
            )
        }
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("admin_home") {
            AdminHomeScreen(navController = navController)
        }
        composable("foundation_home") {
            FoundationHomeScreen(navController = navController)
        }

        // --- RUTAS GESTIÓN FUNDACIÓN ---
        composable("foundation_pets") {
            FoundationMascotasScreen(navController = navController)
        }
        composable(
            route = "foundation_mascota_form?mascotaId={mascotaId}",
            arguments = listOf(navArgument("mascotaId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("mascotaId")
            FoundationMascotaFormScreen(navController = navController, mascotaId = idStr?.toIntOrNull())
        }

        // --- RUTA LISTA DE FUNDACIONES ---
        composable("fundaciones") {
            FoundationListScreen(navController = navController)
        }

        // --- RUTA DETALLE FUNDACIÓN ---
        composable(
            route = "foundation_detail/{foundationName}",
            arguments = listOf(navArgument("foundationName") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("foundationName") ?: ""
            FoundationDetailScreen(foundationName = name, navController = navController)
        }

        // RUTAS PÚBLICAS
        composable("adopciones") {
            AdopcionListScreen(navController = navController)
        }
        composable(
            route = "mascota_detalle/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            MascotaDetalleScreen(navController = navController, mascotaId = mascotaId)
        }
        composable("ultimos_rescates") {
            RescateScreen(navController = navController)
        }
        composable("registro_rescatista") {
            if (sessionManager.isLoggedIn()) {
                RegistroRescatistaScreen(navController = navController)
            } else {
                LoginRequiredScreen(navController)
            }
        }
        composable("formulario_rescate") {
            if (sessionManager.isLoggedIn()) {
                FormularioRescateScreen(navController = navController)
            } else {
                LoginRequiredScreen(navController)
            }
        }
        composable(
            route = "formulario_adopcion?mascotaId={mascotaId}",
            arguments = listOf(navArgument("mascotaId") { 
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: -1
            if (sessionManager.isLoggedIn()) {
                FormularioAdopcionScreen(navController = navController, mascotaId = mascotaId)
            } else {
                LoginRequiredScreen(navController)
            }
        }
        composable("donaciones") {
            DonacionesScreen(navController = navController)
        }
        composable("suscripciones") {
            SubscriptionScreen(navController = navController)
        }
        composable(
            route = "suscripcion_form/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getInt("mascotaId")
            if (sessionManager.isLoggedIn()) {
                SuscripcionFormScreen(navController = navController, mascotaId = mascotaId)
            } else {
                LoginRequiredScreen(navController)
            }
        }

        // --- BLOQUE EVENTOS ---
        composable("eventos") { backStackEntry ->
            val viewModel: EventoViewModel = viewModel(backStackEntry)
            EventoScreen(navController = navController, viewModel = viewModel)
        }
        
        composable(
            route = "eventos/{eventoId}",
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventoId = backStackEntry.arguments?.getInt("eventoId") ?: 0
            
            // Buscamos si existe la pantalla de "eventos" en el historial
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry("eventos")
                } catch (e: Exception) {
                    null
                }
            }

            // Si existe la pantalla previa, compartimos el ViewModel. Si no (venimos del Home), creamos uno nuevo.
            val viewModel: EventoViewModel = if (parentEntry != null) {
                viewModel(parentEntry)
            } else {
                viewModel()
            }

            EventoDetalleScreen(navController = navController, eventoId = eventoId, viewModel = viewModel)
        }

        composable("rescatista_contactos") {
            RescatistaContactosScreen(navController = navController)
        }
        composable("encuesta_rescate") {
            if (sessionManager.isLoggedIn()) {
                EncuestaRescateScreen(navController = navController)
            } else {
                LoginRequiredScreen(navController)
            }
        }
        composable("nosotros") {
            NosotrosScreen(navController = navController)
        }
        composable("perfil") {
            if (sessionManager.isLoggedIn()) {
                ProfileScreen(navController = navController)
            } else {
                LoginRequiredScreen(navController)
            }
        }
        composable("configuracion") {
            SettingsScreen(navController = navController)
        }
        composable("veterinarias") {
            VeterinariaScreen(navController = navController)
        }
        composable("proceso_adopcion") {
            if (sessionManager.isLoggedIn()) {
                ProcesoAdopcionScreen(navController = navController)
            } else {
                LoginRequiredScreen(navController)
            }
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
        composable("admin_suscripciones") {
            AdminSuscripcionesScreen(navController = navController)
        }
    }
}
