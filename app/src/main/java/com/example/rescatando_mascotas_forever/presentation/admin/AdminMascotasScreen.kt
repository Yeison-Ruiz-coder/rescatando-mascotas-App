package com.example.rescatando_mascotas_forever.presentation.admin

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*

// Usamos IDs de recursos para que la traducción sea automática al cambiar el idioma
data class MascotaAdmin(
    val id: Int,
    val nombre: String,
    val especieRes: Int,
    val edadValor: String,
    val estadoRes: Int,
    val imagenUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMascotasScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var mascotaEditando by remember { mutableStateOf<MascotaAdmin?>(null) }

    // Lista de mascotas usando referencias a strings.xml
    val mascotas = remember {
        mutableStateListOf(
            MascotaAdmin(1, "Firulais", R.string.species_dog, "2", R.string.status_available, "https://images.unsplash.com/photo-1543466835-00a7907e9de1"),
            MascotaAdmin(2, "Michi", R.string.species_cat, "6", R.string.status_in_process, "https://images.unsplash.com/photo-1514888286872-01d6d87f1c65"),
            MascotaAdmin(3, "Luna", R.string.species_dog, "4", R.string.status_adopted, "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8")
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, stringResource(R.string.admin_pets_add))
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(stringResource(R.string.admin_pets_title), color = MaterialTheme.colorScheme.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.admin_pets_subtitle), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

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
        MascotaDialogStepByStep(
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
fun MascotaDialogStepByStep(mascota: MascotaAdmin?, onDismiss: () -> Unit, onConfirm: (MascotaAdmin) -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 2

    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var edad by remember { mutableStateOf(mascota?.edadValor ?: "") }
    var url by remember { mutableStateOf(mascota?.imagenUrl ?: "https://") }

    // Configuración de colores dinámica
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        cursorColor = MaterialTheme.colorScheme.onPrimary
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primary, // Fondo dinámico
        title = {
            Text(
                if (mascota == null) stringResource(R.string.admin_pets_new_title) else stringResource(R.string.admin_pets_edit_title),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentStep == 1) {
                    OutlinedTextField(
                        value = nombre, 
                        onValueChange = { nombre = it }, 
                        label = { Text(stringResource(R.string.admin_pets_label_name)) }, 
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = edad, 
                        onValueChange = { edad = it }, 
                        label = { Text(stringResource(R.string.admin_pets_label_age)) }, 
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                } else {
                    OutlinedTextField(
                        value = url, 
                        onValueChange = { url = it }, 
                        label = { Text(stringResource(R.string.admin_pets_label_image)) }, 
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (currentStep == 1) currentStep = 2 
                    else onConfirm(MascotaAdmin(mascota?.id ?: 0, nombre, R.string.species_dog, edad, R.string.status_available, url)) 
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (currentStep == 1) stringResource(R.string.btn_next) else stringResource(R.string.btn_save_upper), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { 
                Text(stringResource(R.string.btn_cancel_upper), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun MascotaAdminCard(mascota: MascotaAdmin, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(mascota.imagenUrl),
                contentDescription = null,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("${stringResource(mascota.especieRes)} • ${mascota.edadValor} ${stringResource(R.string.pet_age_suffix)}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(4.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(mascota.estadoRes),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
            }
        }
    }
}
