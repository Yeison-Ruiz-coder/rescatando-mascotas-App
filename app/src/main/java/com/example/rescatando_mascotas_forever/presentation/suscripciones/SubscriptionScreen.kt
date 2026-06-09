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
                title = { Text("Mis Apadrinamientos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("adopciones") },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Apadrinar más") },
                containerColor = Color(0xFF673AB7),
                contentColor = Color.White
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FE))
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
        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFFE91E63).copy(alpha = 0.2f))
        Spacer(Modifier.height(16.dp))
        Text("Aún no tienes apadrinamientos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Puedes apoyar a una mascota con una donación mensual.", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun SuscripcionItem(suscripcion: Suscripcion, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la mascota
            val fotoUrl = suscripcion.mascota?.fotoPrincipal ?: ""
            val fullImageUrl = if (fotoUrl.startsWith("http")) fotoUrl else "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/$fotoUrl"

            Image(
                painter = rememberAsyncImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier.size(70.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                val capitalizedFrecuencia = suscripcion.frecuencia.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
                Text(suscripcion.mascota?.nombre ?: "Mascota", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$capitalizedFrecuencia • $${suscripcion.montoMensual}", color = Color(0xFF673AB7), fontWeight = FontWeight.Medium)
                Text("Desde: ${suscripcion.fechaInicio}", fontSize = 12.sp, color = Color.Gray)
                
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when(suscripcion.estado) {
                        "activo" -> Color(0xFFE8F5E9)
                        else -> Color(0xFFF5F5F5)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = suscripcion.estado.uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = when(suscripcion.estado) {
                            "activo" -> Color(0xFF2E7D32)
                            else -> Color.Gray
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}
