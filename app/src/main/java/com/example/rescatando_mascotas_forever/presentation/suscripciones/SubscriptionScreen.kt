package com.example.rescatando_mascotas_forever.presentation.suscripciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion

import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavHostController,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Apadrinamientos", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                actions = {
                    // Botón temporal para que veas el Panel Admin Senior rápidamente
                    TextButton(onClick = { navController.navigate("admin_suscripciones") }) {
                        Text("Admin View", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("adopciones") },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Apadrinar más") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
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
                        Text((state as SuscripcionState.Error).message, color = Color.Gray)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.retry() }) { Text("Reintentar") }
                    }
                }
                is SuscripcionState.Success -> {
                    val suscripciones = (state as SuscripcionState.Success).suscripciones
                    if (suscripciones.isEmpty()) {
                        EmptySubscriptionsView()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(suscripciones) { suscripcion ->
                                SuscripcionItem(
                                    suscripcion = suscripcion,
                                    onDelete = { viewModel.cancelarSuscripcion(suscripcion.id ?: 0) }
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
fun EmptySubscriptionsView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        Spacer(Modifier.height(16.dp))
        Text("Aún no tienes apadrinamientos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        Text("Puedes apoyar a una mascota con una donación mensual.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}

@Composable
fun SuscripcionItem(suscripcion: Suscripcion, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Imagen de la mascota
                val fotoUrl = suscripcion.mascota?.fotoPrincipal ?: ""
                val baseUrl = com.example.rescatando_mascotas_forever.utils.Constants.BASE_URL
                val fullImageUrl = if (fotoUrl.startsWith("http")) fotoUrl 
                else "${baseUrl}storage/$fotoUrl"

                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        suscripcion.mascota?.nombre ?: "Mascota", 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    val capitalizedFrecuencia = (suscripcion.frecuencia ?: "mensual").replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    Text(
                        capitalizedFrecuencia,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, 
                        fontSize = 14.sp
                    )
                    Text(
                        "$${suscripcion.montoMensual}", 
                        color = MaterialTheme.colorScheme.primary, 
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.4f))
                }
            }

            if (!suscripcion.mensajeApoyo.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "\"${suscripcion.mensajeApoyo}\"",
                        fontSize = 13.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Desde: ${suscripcion.fechaInicio}", 
                    fontSize = 11.sp, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when(suscripcion.estado) {
                        "activo" -> Color(0xFFE8F5E9)
                        "pausado" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFF5F5F5)
                    }
                ) {
                    Text(
                        text = (suscripcion.estado?:"pendiente").uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        color = when(suscripcion.estado) {
                            "activo" -> Color(0xFF2E7D32)
                            "pausado" -> Color(0xFFEF6C00)
                            else -> Color(0.5f, 0.5f, 0.5f, 1.0f) // Color gris seguro
                        },
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
