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
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.ui.theme.FoundationGradient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationHomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Gradiente Naranja Rojizo para Fundación
    val foundationGradient = FoundationGradient

    Scaffold(
        topBar = {
            MainTopBar(drawerState = drawerState, scope = scope)
        },
        containerColor = Color(0xFFFFF8F6) // Un fondo ligeramente cálido
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Sección Fundación
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(foundationGradient)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Panel de Fundación",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.Business, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                    }
                    Text(
                        text = "Gestiona tus mascotas, eventos y solicitudes",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // Resumen rápido
                Text(
                    text = "ESTADO ACTUAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF666666),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCardSmall("Mis Mascotas", "12", Icons.Default.Pets, Color(0xFFFF5722), Modifier.weight(1f))
                    StatCardSmall("Solicitudes", "5", Icons.Default.Description, Color(0xFFE64A19), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Acciones de Gestión (CRUDs)
                Text(
                    text = "GESTIÓN DE CONTENIDO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF666666),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val actions = listOf(
                    FoundationAction("Mis Mascotas", Icons.Default.Pets, "foundation_pets", "Administra las mascotas de tu fundación"),
                    FoundationAction("Mis Eventos", Icons.Default.Event, "foundation_events", "Crea y edita eventos de recaudación"),
                    FoundationAction("Solicitudes", Icons.Default.Assignment, "foundation_adoptions", "Revisa quién quiere adoptar"),
                    FoundationAction("Padrinos", Icons.Default.VolunteerActivism, "foundation_subscriptions", "Mira tus mascotas apadrinadas"),
                    FoundationAction("Mi Perfil", Icons.Default.Store, "foundation_profile", "Edita la información de tu fundación")
                )

                actions.forEach { action ->
                    FoundationActionCard(action) {
                        if (action.route.isNotEmpty()) navController.navigate(action.route)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Cerrar Sesión
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
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFF5722).copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(action.icon, null, tint = Color(0xFFFF5722), modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(action.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(action.description, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun StatCardSmall(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(title, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

data class FoundationAction(val title: String, val icon: ImageVector, val route: String, val description: String)
