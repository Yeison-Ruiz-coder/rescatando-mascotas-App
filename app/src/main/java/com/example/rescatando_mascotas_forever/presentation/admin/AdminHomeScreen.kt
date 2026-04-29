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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*
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
                                text = stringResource(R.string.admin_home_title),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                        Text(
                            text = stringResource(R.string.admin_home_subtitle),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Resumen de Estadísticas
                    Text(
                        text = stringResource(R.string.admin_home_stat_title),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444), // Gris oscuro para mejor contraste
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(stringResource(R.string.admin_home_stat_pets), "24", Icons.Default.Pets, Color(0xFF4CAF50), Modifier.weight(1f))
                        StatCard(stringResource(R.string.admin_home_stat_rescues), "12", Icons.Default.Warning, Color(0xFFF44336), Modifier.weight(1f))
                        StatCard(stringResource(R.string.admin_home_stat_adoptions), "8", Icons.Default.CheckCircle, Color(0xFF2196F3), Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Acciones Rápidas (Panel de Control)
                    Text(
                        text = stringResource(R.string.admin_home_actions_title),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val adminActions = listOf(
                        AdminAction(stringResource(R.string.admin_action_pets_title), Icons.Default.Favorite, "admin_mascotas", stringResource(R.string.admin_action_pets_desc)),
                        AdminAction(stringResource(R.string.admin_action_adop_title), Icons.Default.Description, "admin_formulario_adopcion", stringResource(R.string.admin_action_adop_desc)),
                        AdminAction(stringResource(R.string.admin_action_reports_title), Icons.Default.LocationOn, "admin_reportes_rescate", stringResource(R.string.admin_action_reports_desc)),
                        AdminAction(stringResource(R.string.admin_action_events_title), Icons.Default.Event, "admin_eventos", stringResource(R.string.admin_action_events_desc)),
                        AdminAction(stringResource(R.string.admin_action_users_title), Icons.Default.Group, "admin_usuarios", stringResource(R.string.admin_action_users_desc)),
                        AdminAction(stringResource(R.string.admin_action_donations_title), Icons.Default.Payments, "admin_donaciones", stringResource(R.string.admin_action_donations_desc))
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
                    
                    // Botón de Modo Usuario para Vista Previa
                    OutlinedButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7))
                    ) {
                        Icon(Icons.Default.RemoveRedEye, null, tint = Color(0xFF673AB7))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.admin_home_btn_preview), color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedAdminCard(action: AdminAction, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF673AB7).copy(alpha = 0.1f),
                modifier = Modifier.size(45.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                        tint = Color(0xFF673AB7),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = action.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color(0xFF222222) // Negro mucho más legible
            )
            Text(
                text = action.description ?: "",
                fontSize = 11.sp,
                color = Color(0xFF555555), // Gris fuerte legible
                lineHeight = 14.sp
            )
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF673AB7), Color(0xFF512DA8))))
                .padding(24.dp)
                .padding(top = 24.dp)
        ) {
            Column {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.admin_drawer_mode), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("admin@rescatando.com", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.admin_drawer_nav), modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
        DrawerMenuItem(stringResource(R.string.admin_drawer_dashboard), Icons.Default.Dashboard, isSelected = currentRoute == "admin_home") { navigateAndClose("admin_home") }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
        
        Text(stringResource(R.string.admin_drawer_forms), modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
        DrawerMenuItem(stringResource(R.string.admin_drawer_adop_form), Icons.Default.Description, isSelected = currentRoute == "admin_formulario_adopcion") { navigateAndClose("admin_formulario_adopcion") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_rescuer_reg), Icons.Default.Badge, isSelected = currentRoute == "admin_registro_rescatista") { navigateAndClose("admin_registro_rescatista") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_rescue_enc), Icons.Default.Quiz, isSelected = currentRoute == "admin_encuesta_rescate") { navigateAndClose("admin_encuesta_rescate") }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
        
        Text(stringResource(R.string.admin_drawer_management), modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
        DrawerMenuItem(stringResource(R.string.admin_action_pets_title), Icons.Default.Pets, isSelected = currentRoute == "admin_mascotas") { navigateAndClose("admin_mascotas") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_events), Icons.Default.Event, isSelected = currentRoute == "admin_eventos") { navigateAndClose("admin_eventos") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_rescue_reports), Icons.Default.Warning, isSelected = currentRoute == "admin_reportes_rescate") { navigateAndClose("admin_reportes_rescate") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_donations), Icons.Default.Payments, isSelected = currentRoute == "admin_donaciones") { navigateAndClose("admin_donaciones") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_users), Icons.Default.Group, isSelected = currentRoute == "admin_usuarios") { navigateAndClose("admin_usuarios") }

        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        DrawerMenuItem(stringResource(R.string.admin_drawer_exit), Icons.AutoMirrored.Filled.ExitToApp, isSelected = false, color = Color.Red) {
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
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text(text = title, fontSize = 12.sp, color = Color(0xFF555555)) // Gris más oscuro que Color.Gray
        }
    }
}

data class AdminAction(val title: String, val icon: ImageVector, val route: String, val description: String? = null)
