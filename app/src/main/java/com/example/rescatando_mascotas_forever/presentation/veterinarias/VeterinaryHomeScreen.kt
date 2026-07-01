package com.example.rescatando_mascotas_forever.presentation.veterinarias

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
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinaryHomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val veterinaryGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF00BFA5), Color(0xFF00796B))
    )

    Scaffold(
        topBar = {
            MainTopBar(drawerState = drawerState, scope = scope)
        },
        containerColor = Color(0xFFF0FDFA) 
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(veterinaryGradient)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Panel Veterinaria",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.MedicalServices, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                    }
                    Text(
                        text = "Gestiona tus citas, pacientes y servicios",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "RESUMEN",
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
                    VetStatCard("Citas Hoy", "8", Icons.Default.CalendarToday, Color(0xFF00BFA5), Modifier.weight(1f))
                    VetStatCard("Pacientes", "45", Icons.Default.Pets, Color(0xFF009688), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "SERVICIOS Y GESTIÓN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF666666),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val actions = listOf(
                    VetAction("Agenda Médica", Icons.Default.Schedule, "", "Revisa tus citas programadas"),
                    VetAction("Historias Clínicas", Icons.Default.FolderShared, "", "Consulta el historial de mascotas"),
                    VetAction("Servicios", Icons.Default.MedicalInformation, "", "Administra tus precios y servicios"),
                    VetAction("Mensajes", Icons.Default.Chat, "", "Habla con dueños y rescatistas"),
                    VetAction("Perfil Profesional", Icons.Default.AccountBox, "", "Edita la información de tu clínica")
                )

                actions.forEach { action ->
                    VetActionCard(action) {
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
fun VetActionCard(action: VetAction, onClick: () -> Unit) {
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
                color = Color(0xFF00BFA5).copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(action.icon, null, tint = Color(0xFF00BFA5), modifier = Modifier.size(24.dp))
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
fun VetStatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
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

data class VetAction(val title: String, val icon: ImageVector, val route: String, val description: String)
