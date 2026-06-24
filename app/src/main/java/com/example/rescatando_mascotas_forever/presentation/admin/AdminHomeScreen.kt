package com.example.rescatando_mascotas_forever.presentation.admin

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import kotlinx.coroutines.launch

// Definición de la clase de acción para el panel
data class AdminAction(
    val title: String, 
    val icon: ImageVector, 
    val route: String, 
    val description: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavHostController,
    viewModel: AdminHomeViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val adminGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF673AB7), Color(0xFF512DA8))
    )

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
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
                                text = "Panel de Administración",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                        Text(
                            text = "Gestiona la plataforma y consulta reportes en tiempo real",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                when (val state = uiState) {
                    is AdminHomeState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF673AB7))
                        }
                    }
                    is AdminHomeState.Success -> {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Resumen de Estadísticas
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
                                StatCard("Mascotas", state.stats.totalMascotas.toString(), Icons.Default.Pets, Color(0xFF4CAF50), Modifier.weight(1f))
                                StatCard("Rescates", state.stats.totalRescates.toString(), Icons.Default.Warning, Color(0xFFF44336), Modifier.weight(1f))
                                StatCard("Adopciones", state.stats.totalAdopciones.toString(), Icons.Default.CheckCircle, Color(0xFF2196F3), Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SECCIÓN DE REPORTES
                            Text(
                                text = "EXTRACCIÓN DE REPORTES",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF444444),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Assessment, null, tint = Color(0xFF673AB7))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Generar Reportes Ejecutivos", fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text("Descarga los datos consolidados de la plataforma en formato PDF o CSV.", fontSize = 12.sp, color = Color.Gray)
                                    Spacer(Modifier.height(16.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { Toast.makeText(context, "Generando PDF de Adopciones...", Toast.LENGTH_SHORT).show() },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Adopciones PDF", fontSize = 10.sp, color = Color.White)
                                        }
                                        Button(
                                            onClick = { Toast.makeText(context, "Generando CSV de Usuarios...", Toast.LENGTH_SHORT).show() },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Usuarios CSV", fontSize = 10.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Acciones de Gestión
                            Text(
                                text = "GESTIÓN DE CONTENIDO",
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
                                            if (action.route.isNotEmpty()) navController.navigate(action.route)
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedButton(
                                onClick = { navController.navigate("home") },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7))
                            ) {
                                Icon(Icons.Default.RemoveRedEye, null, tint = Color(0xFF673AB7))
                                Spacer(Modifier.width(8.dp))
                                Text("Vista Previa como Usuario", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    is AdminHomeState.Error -> {
                        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Error al cargar datos", color = Color.Red, fontWeight = FontWeight.Bold)
                                Text(state.message, color = Color.Gray, fontSize = 12.sp)
                                Button(onClick = { viewModel.fetchDashboardData() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
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
                color = Color(0xFF222222)
            )
            Text(
                text = action.description ?: "",
                fontSize = 11.sp,
                color = Color(0xFF555555),
                lineHeight = 14.sp
            )
        }
    }
}
