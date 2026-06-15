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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*

data class DonacionRegistro(
    val id: Int,
    val donante: String,
    val monto: String,
    val fecha: String,
    val tipo: String // Económica, Alimento, Insumos
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDonacionesScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val registros = remember {
        mutableStateListOf(
            DonacionRegistro(1, "Juan Pérez", "$50.000", "10/03/2025", "Económica"),
            DonacionRegistro(2, "María García", "10kg Alimento", "08/03/2025", "Alimento"),
            DonacionRegistro(3, "Anónimo", "$100.000", "05/03/2025", "Económica"),
            DonacionRegistro(4, "Carlos Ruiz", "Kit Primeros Auxilios", "02/03/2025", "Insumos")
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
                // Header Admin Traducido
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF673AB7))
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.admin_donations_title),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.admin_donations_subtitle),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }

                // Resumen rápido con StatCard (con texto oscuro corregido)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = stringResource(R.string.admin_donations_stat_total),
                        value = "$1.2M",
                        icon = Icons.Default.TrendingUp,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = stringResource(R.string.admin_donations_stat_donors),
                        value = "42",
                        icon = Icons.Default.Groups,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = stringResource(R.string.admin_donations_latest),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333) // Color oscuro para visibilidad
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(registros) { registro ->
                        DonacionRegistroCard(registro)
                    }
                }
            }
        }
    }
}

@Composable
fun DonacionRegistroCard(registro: DonacionRegistro) {
    // Mapeo de tipos para traducción
    val tipoTraducido = when(registro.tipo) {
        "Económica" -> stringResource(R.string.admin_donations_type_money)
        "Alimento" -> stringResource(R.string.admin_donations_type_food)
        "Insumos" -> stringResource(R.string.admin_donations_type_supplies)
        else -> registro.tipo
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = when(registro.tipo) {
                    "Económica" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    "Alimento" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                    else -> Color(0xFF2196F3).copy(alpha = 0.1f)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when(registro.tipo) {
                            "Económica" -> Icons.Default.AttachMoney
                            "Alimento" -> Icons.Default.Pets
                            else -> Icons.Default.Inventory2
                        },
                        contentDescription = null,
                        tint = when(registro.tipo) {
                            "Económica" -> Color(0xFF4CAF50)
                            "Alimento" -> Color(0xFFFF9800)
                            else -> Color(0xFF2196F3)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(text = registro.donante, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(text = "${registro.fecha} • $tipoTraducido", color = Color(0xFF555555), fontSize = 12.sp)
            }

            Text(
                text = registro.monto,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222),
                fontSize = 15.sp
            )
        }
    }
}