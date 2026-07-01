package com.example.rescatando_mascotas_forever.presentation.fundacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationMascotasScreen(
    navController: NavHostController,
    viewModel: FoundationMascotasViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Mascota?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadMascotas()
    }

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("foundation_mascota_form") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, "Agregar Mascota")
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (val s = state) {
                    is FoundationMascotasState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
                    }
                    is FoundationMascotasState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.ErrorOutline, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                            Text("Error: ${s.message}", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), modifier = Modifier.padding(16.dp))
                            Button(
                                onClick = { viewModel.loadMascotas() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) { Text("Reintentar") }
                        }
                    }
                    is FoundationMascotasState.Success -> {
                        if (s.mascotas.isEmpty()) {
                            EmptyPetsState()
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Text(
                                        "Gestionando ${s.mascotas.size} mascotas",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
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
    }

    if (showDeleteDialog != null) {
        DeleteMascotaDialog(
            mascotaName = showDeleteDialog?.nombre ?: "",
            onConfirm = {
                showDeleteDialog?.id?.let { viewModel.deleteMascota(it) }
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null }
        )
    }
}

@Composable
fun MascotaFoundationCard(mascota: Mascota, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(Constants.getImageUrl(mascota.fotoPrincipal))
            
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = mascota.nombre, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 17.sp, 
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${mascota.especie} • ${mascota.genero ?: "S/G"}", 
                    fontSize = 13.sp, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Spacer(Modifier.height(8.dp))
                
                val statusColor = when (mascota.estado?.lowercase()) {
                    "adoptado" -> Color(0xFF10B981)
                    "en adopcion", "en adopción" -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                }

                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = mascota.estado?.uppercase() ?: "PENDIENTE",
                        color = statusColor,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) { 
                    Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) 
                }
                IconButton(onClick = onDelete) { 
                    Icon(Icons.Default.DeleteOutline, "Eliminar", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), modifier = Modifier.size(20.dp)) 
                }
            }
        }
    }
}

@Composable
fun EmptyPetsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        ) {
            Icon(
                Icons.Default.Pets, 
                null, 
                modifier = Modifier.padding(30.dp), 
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text("Tu inventario está vacío", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
        Text(
            "Comienza registrando tu primera mascota", 
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), 
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DeleteMascotaDialog(mascotaName: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Eliminar a $mascotaName?", color = MaterialTheme.colorScheme.onSurface) },
        text = { Text("Esta acción quitará a la mascota de la base de datos permanentemente.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ELIMINAR", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onError)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}
