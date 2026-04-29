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

data class EventoAdmin(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val ubicacion: String,
    val etiqueta: String,
    val imagenUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEventosScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var eventoEditando by remember { mutableStateOf<EventoAdmin?>(null) }

    val eventos = remember {
        mutableStateListOf(
            EventoAdmin(1, "Gran Jornada de Adopción", "15 Marzo", "Parque Simón Bolívar", "DESTACADO", "https://images.unsplash.com/photo-1548199973-03cce0bbc87b"),
            EventoAdmin(2, "Taller de Adiestramiento", "22 Marzo", "Veterinaria Norte", "TALLER", "https://images.unsplash.com/photo-1587300003388-59208cc962cb"),
            EventoAdmin(3, "Feria Mascota Feliz", "05 Abril", "C.C. Gran Estación", "CONCURSO", "https://images.unsplash.com/photo-1516734212186-a967f81ad0d7")
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
                        eventoEditando = null
                        showDialog = true
                    },
                    containerColor = Color(0xFF673AB7),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, stringResource(R.string.admin_pets_add))
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF673AB7))
                        .padding(20.dp)
                ) {
                    Column {
                        // TÍTULOS DINÁMICOS
                        Text(stringResource(R.string.admin_action_events_title), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.admin_action_events_desc), color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(eventos) { evento ->
                        EventoAdminCard(
                            evento = evento,
                            onEdit = {
                                eventoEditando = evento
                                showDialog = true
                            },
                            onDelete = { eventos.remove(evento) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        EventoDialogStepByStep(
            evento = eventoEditando,
            onDismiss = { showDialog = false },
            onConfirm = { nuevoEvento ->
                if (eventoEditando != null) {
                    val index = eventos.indexOfFirst { it.id == eventoEditando!!.id }
                    if (index != -1) eventos[index] = nuevoEvento
                } else {
                    eventos.add(nuevoEvento.copy(id = (eventos.maxByOrNull { it.id }?.id ?: 0) + 1))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun EventoDialogStepByStep(evento: EventoAdmin?, onDismiss: () -> Unit, onConfirm: (EventoAdmin) -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 2

    var titulo by remember { mutableStateOf(evento?.titulo ?: "") }
    var fecha by remember { mutableStateOf(evento?.fecha ?: "") }
    var ubicacion by remember { mutableStateOf(evento?.ubicacion ?: "") }
    var etiqueta by remember { mutableStateOf(evento?.etiqueta ?: "NORMAL") }
    var url by remember { mutableStateOf(evento?.imagenUrl ?: "https://") }

    // Configuración de colores para que el texto sea BLANCO
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
        cursorColor = Color.White
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF673AB7), // Fondo púrpura para que el texto blanco resalte
        title = {
            Column {
                Text(
                    if (evento == null) stringResource(R.string.admin_events_new) else stringResource(R.string.admin_events_edit),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.rescue_survey_step, currentStep, totalSteps),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                if (currentStep == 1) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text(stringResource(R.string.admin_events_label_title), fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text(stringResource(R.string.admin_events_label_date), fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = ubicacion,
                        onValueChange = { ubicacion = it },
                        label = { Text(stringResource(R.string.admin_events_label_location), fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                } else {
                    OutlinedTextField(
                        value = etiqueta,
                        onValueChange = { etiqueta = it },
                        label = { Text("Etiqueta / Tag", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text(stringResource(R.string.admin_pets_label_image), fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (currentStep == 1) {
                    Button(
                        onClick = { currentStep = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF673AB7))
                    ) {
                        Text(stringResource(R.string.btn_next), fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { onConfirm(EventoAdmin(evento?.id ?: 0, titulo, fecha, ubicacion, etiqueta, url)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
                    ) {
                        Text(stringResource(R.string.btn_save_upper), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        dismissButton = {
            Row {
                if (currentStep == 2) {
                    TextButton(onClick = { currentStep = 1 }) {
                        Text(stringResource(R.string.btn_previous), color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.btn_cancel_upper), color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun EventoAdminCard(evento: EventoAdmin, onEdit: () -> Unit, onDelete: () -> Unit) {
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
                painter = rememberAsyncImagePainter(evento.imagenUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(evento.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, color = Color.Black)
                Text("${evento.fecha} • ${evento.ubicacion}", color = Color(0xFF333333), fontSize = 12.sp, maxLines = 1)

                Spacer(Modifier.height(4.dp))

                Surface(
                    color = Color(0xFF673AB7).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = evento.etiqueta,
                        color = Color(0xFF673AB7),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, null, tint = Color(0xFF673AB7))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, null, tint = Color.Red)
                }
            }
        }
    }
}
