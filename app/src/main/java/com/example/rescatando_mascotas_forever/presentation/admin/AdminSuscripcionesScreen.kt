package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SuscripcionState
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSuscripcionesScreen(
    navController: NavHostController,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllSuscripciones()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Suscripciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadAllSuscripciones() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            when (state) {
                is SuscripcionState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF673AB7))
                    }
                }
                is SuscripcionState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(Modifier.height(16.dp))
                        Text((state as SuscripcionState.Error).message, color = Color.Gray)
                        Button(onClick = { viewModel.loadAllSuscripciones() }, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Reintentar")
                        }
                    }
                }
                is SuscripcionState.Success -> {
                    val suscripciones = (state as SuscripcionState.Success).suscripciones
                    if (suscripciones.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay suscripciones registradas", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(suscripciones) { suscripcion ->
                                AdminSuscripcionCard(
                                    suscripcion = suscripcion,
                                    onUpdateStatus = { status ->
                                        viewModel.updateStatus(suscripcion.id ?: 0, status)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSuscripcionCard(suscripcion: Suscripcion, onUpdateStatus: (String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Suscripción #${suscripcion.id}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = suscripcion.mascota?.nombre ?: "Sin Mascota",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E1A7A)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (suscripcion.estado) {
                        "activo" -> Color(0xFFE8F5E9)
                        "pausado" -> Color(0xFFFFF3E0)
                        "cancelado" -> Color(0xFFFFEBEE)
                        else -> Color(0xFFF5F5F5)
                    }
                ) {
                    Text(
                        text = suscripcion.estado.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (suscripcion.estado) {
                            "activo" -> Color(0xFF2E7D32)
                            "pausado" -> Color(0xFFEF6C00)
                            "cancelado" -> Color(0xFFC62828)
                            else -> Color.Gray
                        }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoIconText(Icons.Default.Person, "Usuario: ${suscripcion.user?.nombre ?: "ID: ${suscripcion.userId}"}", Modifier.weight(1f))
                InfoIconText(Icons.Default.Payments, "$${suscripcion.montoMensual}", Modifier.weight(1f))
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoIconText(Icons.Default.CalendarToday, "Inicio: ${suscripcion.fechaInicio}", Modifier.weight(1f))
                InfoIconText(Icons.Default.Update, "Frecuencia: ${suscripcion.frecuencia}", Modifier.weight(1f))
            }

            if (!suscripcion.mensajeApoyo.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Mensaje: \"${suscripcion.mensajeApoyo}\"",
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.DarkGray
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = { showMenu = true },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar Estado", fontSize = 12.sp)
                }

                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text("Activar") }, onClick = { onUpdateStatus("activo"); showMenu = false })
                    DropdownMenuItem(text = { Text("Pausar") }, onClick = { onUpdateStatus("pausado"); showMenu = false })
                    DropdownMenuItem(text = { Text("Cancelar") }, onClick = { onUpdateStatus("cancelado"); showMenu = false })
                    DropdownMenuItem(text = { Text("Finalizar") }, onClick = { onUpdateStatus("finalizado"); showMenu = false })
                }
            }
        }
    }
}

@Composable
fun InfoIconText(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 12.sp, color = Color.DarkGray)
    }
}
