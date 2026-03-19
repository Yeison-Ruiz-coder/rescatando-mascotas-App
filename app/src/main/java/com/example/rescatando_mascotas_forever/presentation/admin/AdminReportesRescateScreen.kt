package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.*

data class ReporteRescateAdmin(
    val id: Int,
    val mascota: String,
    val especie: String,
    val estado: String, // Pendiente, En Camino, Rescatado
    val ubicacion: String,
    val fecha: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportesRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val reportes = remember {
        mutableStateListOf(
            ReporteRescateAdmin(1, "Desconocido", "Perro", "Pendiente", "Calle 45 #12-30", "12/03/2025"),
            ReporteRescateAdmin(2, "Gatito herido", "Gato", "En Camino", "Parque Nacional", "11/03/2025"),
            ReporteRescateAdmin(3, "Canino abandonado", "Perro", "Rescatado", "Av. Boyacá con 80", "10/03/2025")
        )
    }

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
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                // Header Admin
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF673AB7))
                        .padding(24.dp)
                ) {
                    Column {
                        Text("Reportes de Rescate", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Monitorea las emergencias reportadas", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reportes) { reporte ->
                        ReporteRescateCard(reporte)
                    }
                }
            }
        }
    }
}

@Composable
fun ReporteRescateCard(reporte: ReporteRescateAdmin) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = when(reporte.estado) {
                        "Pendiente" -> Color(0xFFF44336).copy(alpha = 0.1f)
                        "En Camino" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        else -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = when(reporte.estado) {
                                "Pendiente" -> Color(0xFFF44336)
                                "En Camino" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Spacer(Modifier.width(12.dp))
                
                Column(Modifier.weight(1f)) {
                    Text(reporte.mascota, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${reporte.especie} • ${reporte.fecha}", color = Color.Gray, fontSize = 12.sp)
                }
                
                Surface(
                    color = when(reporte.estado) {
                        "Pendiente" -> Color(0xFFF44336).copy(alpha = 0.1f)
                        "En Camino" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        else -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = reporte.estado,
                        color = when(reporte.estado) {
                            "Pendiente" -> Color(0xFFF44336)
                            "En Camino" -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        },
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(reporte.ubicacion, fontSize = 13.sp, color = Color.DarkGray)
            }
            
            Spacer(Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ver Detalles", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7))
                ) {
                    Text("Cambiar Estado", fontSize = 12.sp, color = Color(0xFF673AB7))
                }
            }
        }
    }
}
