package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.presentation.common.components.*

data class MascotaAdmin(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: String,
    val estado: String, // Adoptado, Disponible, En Proceso
    val imagenUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMascotasScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    var showDialog by remember { mutableStateOf(false) }
    var mascotaEditando by remember { mutableStateOf<MascotaAdmin?>(null) }

    // Lista de prueba
    val mascotas = remember {
        mutableStateListOf(
            MascotaAdmin(1, "Firulais", "Perro", "2 años", "Disponible", "https://images.unsplash.com/photo-1543466835-00a7907e9de1"),
            MascotaAdmin(2, "Michi", "Gato", "6 meses", "En Proceso", "https://images.unsplash.com/photo-1514888286872-01d6d87f1c65"),
            MascotaAdmin(3, "Luna", "Perro", "4 años", "Adoptado", "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8")
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
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { 
                        mascotaEditando = null
                        showDialog = true 
                    },
                    containerColor = Color(0xFF673AB7),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Agregar Mascota")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF673AB7))
                        .padding(20.dp)
                ) {
                    Column {
                        Text("Gestión de Mascotas", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Administra el inventario de peluditos", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                // Listado
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(mascotas) { mascota ->
                        MascotaAdminCard(
                            mascota = mascota,
                            onEdit = { 
                                mascotaEditando = mascota
                                showDialog = true 
                            },
                            onDelete = { mascotas.remove(mascota) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        MascotaDialog(
            mascota = mascotaEditando,
            onDismiss = { showDialog = false },
            onConfirm = { nuevaMascota ->
                if (mascotaEditando != null) {
                    val index = mascotas.indexOfFirst { it.id == mascotaEditando!!.id }
                    if (index != -1) mascotas[index] = nuevaMascota
                } else {
                    mascotas.add(nuevaMascota.copy(id = (mascotas.maxByOrNull { it.id }?.id ?: 0) + 1))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun MascotaAdminCard(mascota: MascotaAdmin, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(mascota.imagenUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(Modifier.width(16.dp))
            
            Column(Modifier.weight(1f)) {
                Text(mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${mascota.especie} • ${mascota.edad}", color = Color.Gray, fontSize = 13.sp)
                
                Spacer(Modifier.height(4.dp))
                
                val statusColor = when(mascota.estado) {
                    "Disponible" -> Color(0xFF4CAF50)
                    "En Proceso" -> Color(0xFFFF9800)
                    else -> Color(0xFF2196F3)
                }
                
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = mascota.estado,
                        color = statusColor,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Editar", tint = Color(0xFF673AB7))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun MascotaDialog(mascota: MascotaAdmin?, onDismiss: () -> Unit, onConfirm: (MascotaAdmin) -> Unit) {
    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var especie by remember { mutableStateOf(mascota?.especie ?: "Perro") }
    var edad by remember { mutableStateOf(mascota?.edad ?: "") }
    var estado by remember { mutableStateOf(mascota?.estado ?: "Disponible") }
    var url by remember { mutableStateOf(mascota?.imagenUrl ?: "https://") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (mascota == null) "Nueva Mascota" else "Editar Mascota") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = especie, onValueChange = { especie = it }, label = { Text("Especie") })
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL Imagen") })
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    onConfirm(MascotaAdmin(mascota?.id ?: 0, nombre, especie, edad, estado, url))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
