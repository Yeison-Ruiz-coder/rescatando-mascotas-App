package com.example.rescatando_mascotas_forever.presentation.fundacion

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
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationHomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Gradiente dinámico que usa los colores del tema actual
    val mainGradient = AppMainGradient

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header con Gradiente de Marca
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mainGradient)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Panel de Gestión",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.Business, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                        }
                        Text(
                            text = "Centro de control de tu fundación",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "RESUMEN OPERATIVO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCardSmall("Mis Mascotas", "---", Icons.Default.Pets, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                        StatCardSmall("Solicitudes", "---", Icons.Default.Description, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "ACCESOS RÁPIDOS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val actions = listOf(
                        FoundationAction("Mascotas", Icons.Default.Pets, "foundation_pets", "Gestionar catálogo"),
                        FoundationAction("Solicitudes", Icons.Default.Favorite, "foundation_adoptions", "Ver quién quiere adoptar"),
                        FoundationAction("Eventos", Icons.Default.Event, "foundation_events", "Organizar recaudaciones"),
                        FoundationAction("Configuración", Icons.Default.Settings, "configuracion", "Ajustes de la cuenta")
                    )

                    actions.forEach { action ->
                        FoundationActionCard(action) {
                            if (action.route.isNotEmpty()) navController.navigate(action.route)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient.setToken(null)
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FoundationActionCard(action: FoundationAction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(action.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(action.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(action.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun StatCardSmall(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

data class FoundationAction(val title: String, val icon: ImageVector, val route: String, val description: String)
