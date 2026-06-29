package com.example.rescatando_mascotas_forever.presentation.fundacion

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
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.ui.theme.FoundationGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationMascotasScreen(
    navController: NavHostController,
    viewModel: FoundationMascotasViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Mascota?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Mascotas", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(FoundationGradient)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("foundation_mascota_form") },
                containerColor = Color(0xFFFF5722),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Agregar Mascota")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFFF8F6))
        ) {
            when (val s = state) {
                is FoundationMascotasState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFFF5722))
                }
                is FoundationMascotasState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${s.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                        Button(onClick = { viewModel.loadMascotas() }) {
                            Text("Reintentar")
                        }
                    }
                }
                is FoundationMascotasState.Success -> {
                    if (s.mascotas.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Pets, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                            Spacer(Modifier.height(16.dp))
                            Text("No tienes mascotas registradas", color = Color.Gray, fontWeight = FontWeight.Medium)
                            Text("Toca el botón + para agregar una", color = Color.LightGray, fontSize = 12.sp)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(s.mascotas) { mascota ->
                                MascotaFoundationCard(
                                    mascota = mascota,
                                    onEdit = { 
                                        navController.navigate("foundation_mascota_form?mascotaId=${mascota.id}")
                                    },
                                    onDelete = { showDeleteDialog = mascota }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar Mascota") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${showDeleteDialog?.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog?.id?.let { viewModel.deleteMascota(it) }
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun MascotaFoundationCard(mascota: Mascota, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val fotoUrl = mascota.fotoPrincipal ?: ""
            val fullImageUrl = if (fotoUrl.startsWith("http")) fotoUrl else "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/$fotoUrl"

            Image(
                painter = rememberAsyncImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = mascota.nombre, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 18.sp, 
                    color = Color.Black
                )
                Text(
                    text = "${mascota.especie ?: "Especie"} • ${mascota.genero ?: "S/G"}", 
                    fontSize = 14.sp, 
                    color = Color.Gray
                )
                
                Spacer(Modifier.height(8.dp))
                
                Surface(
                    color = Color(0xFFFF5722).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = mascota.estado ?: "En adopción",
                        color = Color(0xFFFF5722),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEdit) { 
                    Icon(Icons.Default.Edit, "Editar", tint = Color(0xFFFF5722)) 
                }
                IconButton(onClick = onDelete) { 
                    Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red.copy(alpha = 0.6f)) 
                }
            }
        }
    }
}
