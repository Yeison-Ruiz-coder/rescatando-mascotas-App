package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.network.models.toSafeDouble
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SuscripcionState
import com.example.rescatando_mascotas_forever.presentation.suscripciones.SubscriptionViewModel
import java.util.Locale

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
                title = { Text("Gestión de Suscripciones", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadAllSuscripciones() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
        ) {
            when (val s = state) {
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
                        Text(s.message, color = Color.Gray)
                        Button(onClick = { viewModel.loadAllSuscripciones() }, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Reintentar")
                        }
                    }
                }
                is SuscripcionState.Success -> {
                    if (s.suscripciones.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay suscripciones registradas", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(s.suscripciones) { suscripcion ->
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen de la mascota
                val fotoUrl = suscripcion.mascota?.fotoPrincipal ?: ""
                val fullImageUrl = if (fotoUrl.startsWith("http")) fotoUrl 
                                 else "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/$fotoUrl"

                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.2f)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Suscripción #${suscripcion.id}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = suscripcion.mascota?.nombre ?: "Sin Mascota",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (suscripcion.estado?.lowercase()) {
                        "activo" -> Color(0xFFE8F5E9)
                        "pausado" -> Color(0xFFFFF3E0)
                        "cancelado" -> Color(0xFFFFEBEE)
                        else -> Color(0xFFF5F5F5)
                    }
                ) {
                    Text(
                        text = (suscripcion.estado ?: "pendiente").uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (suscripcion.estado?.lowercase()) {
                            "activo" -> Color(0xFF2E7D32)
                            "pausado" -> Color(0xFFEF6C00)
                            "cancelado" -> Color(0xFFC62828)
                            else -> Color.Gray
                        }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.3f))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoIconText(Icons.Default.Person, "Donante: ${suscripcion.user?.nombre ?: "N/A"}", Modifier.weight(1.2f))
                
                val monto = suscripcion.montoMensual.toSafeDouble()
                InfoIconText(Icons.Default.Payments, "$" + String.format(Locale.US, "%,.0f", monto), Modifier.weight(0.8f))
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoIconText(Icons.Default.CalendarToday, "Inicio: ${suscripcion.fechaInicio?.take(10) ?: "N/A"}", Modifier.weight(1.2f))
                InfoIconText(Icons.Default.Update, suscripcion.frecuencia ?: "mensual", Modifier.weight(0.8f))
            }

            if (!suscripcion.mensajeApoyo.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = Color(0xFFF3F1FF),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${suscripcion.mensajeApoyo}\"",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = Color(0xFF4C35A3)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { showMenu = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Gestionar", fontSize = 13.sp)
                }

                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Activar") }, 
                        leadingIcon = { Icon(Icons.Default.CheckCircle, null, tint = Color.Green) },
                        onClick = { onUpdateStatus("activo"); showMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Pausar") }, 
                        leadingIcon = { Icon(Icons.Default.Pause, null, tint = Color(0xFFEF6C00)) },
                        onClick = { onUpdateStatus("pausado"); showMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Cancelar") }, 
                        leadingIcon = { Icon(Icons.Default.Cancel, null, tint = Color.Red) },
                        onClick = { onUpdateStatus("cancelado"); showMenu = false }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoIconText(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 12.sp, color = Color.DarkGray, maxLines = 1)
    }
}
