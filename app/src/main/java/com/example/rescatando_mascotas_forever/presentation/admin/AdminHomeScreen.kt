package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.DrawerMenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val adminGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF673AB7), Color(0xFF512DA8))
    )

    // Usamos el ModalNavigationDrawer directamente para poder pasar el AdminDrawerContent personalizado
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                AdminDrawerContent(navController, drawerState, scope)
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            containerColor = Color(0xFFF8F9FE)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Sección
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(adminGradient)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Panel de Control",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                        Text(
                            text = "Bienvenido, Administrador",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "ESTADÍSTICAS GENERALES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard("Mascotas", "24", Icons.Default.Pets, Color(0xFF4CAF50), Modifier.weight(1f))
                        StatCard("Rescates", "12", Icons.Default.Warning, Color(0xFFF44336), Modifier.weight(1f))
                        StatCard("Adopciones", "8", Icons.Default.CheckCircle, Color(0xFF2196F3), Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "GESTIÓN DE MÓDULOS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val adminActions = listOf(
                        AdminAction("Mascotas", Icons.Default.Favorite, "admin_mascotas", "Inventario y estados"),
                        AdminAction("Adopciones", Icons.Default.Description, "admin_formulario_adopcion", "Gestionar solicitudes"),
                        AdminAction("Reportes", Icons.Default.LocationOn, "admin_reportes_rescate", "Mapa y emergencias"),
                        AdminAction("Eventos", Icons.Default.Event, "admin_eventos", "Organizar actividades"),
                        AdminAction("Suscripciones", Icons.Default.CardMembership, "admin_suscripciones", "Planes y socios premium"),
                        AdminAction("Usuarios", Icons.Default.Group, "admin_usuarios", "Control de accesos")
                    )

                    adminActions.chunked(2).forEach { rowActions ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowActions.forEach { action ->
                                EnhancedAdminCard(action, Modifier.weight(1f)) {
                                    if (action.route != "") navController.navigate(action.route)
                                }
                            }
                            if (rowActions.size < 2) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7))
                    ) {
                        Icon(Icons.Default.RemoveRedEye, null, tint = Color(0xFF673AB7))
                        Spacer(Modifier.width(8.dp))
                        Text("IR A VISTA USUARIO", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF673AB7), Color(0xFF512DA8)))).padding(24.dp).padding(top = 24.dp)
        ) {
            Column {
                Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f)) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("MODO ADMINISTRADOR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("admin@rescatandomascotas.com", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        DrawerMenuItem("Dashboard", Icons.Default.Dashboard, currentRoute == "admin_home") { navigateAndClose("admin_home") }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
        
        DrawerMenuItem("Mascotas", Icons.Default.Pets, currentRoute == "admin_mascotas") { navigateAndClose("admin_mascotas") }
        DrawerMenuItem("Suscripciones", Icons.Default.CardMembership, currentRoute == "admin_suscripciones") { navigateAndClose("admin_suscripciones") }
        DrawerMenuItem("Eventos", Icons.Default.Event, currentRoute == "admin_eventos") { navigateAndClose("admin_eventos") }
        DrawerMenuItem("Usuarios", Icons.Default.Group, currentRoute == "admin_usuarios") { navigateAndClose("admin_usuarios") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) {
            scope.launch {
                drawerState.close()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun EnhancedAdminCard(action: AdminAction, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF673AB7).copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(action.icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(action.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            action.description?.let { Text(it, fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp) }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text(title, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

data class AdminAction(val title: String, val icon: ImageVector, val route: String, val description: String? = null)
