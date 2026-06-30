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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoState
import com.example.rescatando_mascotas_forever.presentation.eventos.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEventosScreen(
    navController: NavHostController,
    viewModel: EventoViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var eventoEditando by remember { mutableStateOf<Evento?>(null) }

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
                        Text(stringResource(R.string.admin_action_events_title), color = MaterialTheme.colorScheme.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.admin_action_events_desc), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                when (val currentState = state) {
                    is EventoState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is EventoState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(currentState.eventos) { evento ->
                                EventoAdminCard(
                                    evento = evento,
                                    onEdit = {
                                        eventoEditando = evento
                                        showDialog = true
                                    },
                                    onDelete = { /* Implementar lógica de eliminación si es necesario */ }
                                )
                            }
                        }
                    }
                    is EventoState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Error: ${currentState.message}", color = Color.Red)
                                Button(onClick = { viewModel.getEventos() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        EventoDialogStepByStep(
            evento = eventoEditando,
            onDismiss = { showDialog = false },
            onConfirm = { /* Implementar lógica para guardar o actualizar a través de la API */
                showDialog = false
            }
        )
    }
}

@Composable
fun EventoDialogStepByStep(evento: Evento?, onDismiss: () -> Unit, onConfirm: (Evento) -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 2

    var titulo by remember { mutableStateOf(evento?.nombre ?: "") }
    var fecha by remember { mutableStateOf(evento?.fecha ?: "") }
    var ubicacion by remember { mutableStateOf(evento?.lugar ?: "") }
    var tipo by remember { mutableStateOf(evento?.tipo ?: "NORMAL") }
    var url by remember { mutableStateOf(evento?.imagenUrl ?: "") }

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
        containerColor = MaterialTheme.colorScheme.primary,
        title = {
            Column {
                Text(
                    if (evento == null) stringResource(R.string.admin_events_new) else stringResource(R.string.admin_events_edit),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.rescue_survey_step, currentStep, totalSteps),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
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
                        value = tipo,
                        onValueChange = { tipo = it },
                        label = { Text("Tipo de Evento", fontWeight = FontWeight.Bold) },
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(stringResource(R.string.btn_next), fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            // Aquí se crearía el objeto Evento real para enviar a la API
                            onConfirm(Evento(
                                id = evento?.id ?: 0,
                                nombre = titulo,
                                lugar = ubicacion,
                                descripcion = evento?.descripcion,
                                fecha = fecha,
                                imagenUrl = url,
                                imagenPublicId = evento?.imagenPublicId,
                                fundacionId = evento?.fundacionId,
                                tipo = tipo,
                                likes = evento?.likes,
                                createdAt = evento?.createdAt,
                                updatedAt = evento?.updatedAt,
                                deletedAt = evento?.deletedAt
                            ))
                        },
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
                        Text(stringResource(R.string.btn_previous), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.btn_cancel_upper), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun EventoAdminCard(evento: Evento, onEdit: () -> Unit, onDelete: () -> Unit) {
    // Lógica de fallback para imagenUrl o imagenPublicId
    val imagePath = if (!evento.imagenUrl.isNullOrEmpty()) evento.imagenUrl else evento.imagenPublicId
    val fullImageUrl = com.example.rescatando_mascotas_forever.utils.Constants.getImageUrl(imagePath)

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
                painter = rememberAsyncImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(evento.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, color = MaterialTheme.colorScheme.onSurface)
                Text("${evento.fecha} • ${evento.lugar}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, maxLines = 1)

                Spacer(Modifier.height(4.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = evento.tipo ?: "EVENTO",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, null, tint = Color.Red)
                }
            }
        }
    }
}
