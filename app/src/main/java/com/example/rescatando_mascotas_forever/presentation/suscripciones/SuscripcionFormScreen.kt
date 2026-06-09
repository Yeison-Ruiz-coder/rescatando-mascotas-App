package com.example.rescatando_mascotas_forever.presentation.suscripciones

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuscripcionFormScreen(
    navController: NavHostController,
    mascotaId: Int? = null,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { com.example.rescatando_mascotas_forever.data.local.SessionManager(context) }
    val user = remember { sessionManager.getUser() }
    val calendar = Calendar.getInstance()

    var monto by remember { mutableStateOf("") }
    var frecuencia by remember { mutableStateOf("mensual") }
    var fechaInicio by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val createState by viewModel.createState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(createState) {
        createState?.onSuccess {
            viewModel.resetCreateState()
            navController.popBackStack()
        }?.onFailure {
            viewModel.resetCreateState()
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF673AB7), Color(0xFF4C35A3))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Apadrinar Mascota", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF673AB7))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8F9FE))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradient)
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        "Formulario de Apoyo",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Completa los datos para iniciar tu suscripción de apoyo.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Monto
                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto Mensual (COP)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Payments, null) },
                        shape = RoundedCornerShape(12.dp),
                        prefix = { Text("$ ") }
                    )

                    Spacer(Modifier.height(16.dp))

                    // Frecuencia
                    var expandedFreq by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedFreq,
                        onExpandedChange = { expandedFreq = !expandedFreq }
                    ) {
                        OutlinedTextField(
                            value = frecuencia.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Frecuencia") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFreq) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.DateRange, null) }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedFreq,
                            onDismissRequest = { expandedFreq = false }
                        ) {
                            listOf("unica", "mensual", "trimestral", "anual").forEach { selection ->
                                DropdownMenuItem(
                                    text = { Text(selection.replaceFirstChar { it.uppercase() }) },
                                    onClick = {
                                        frecuencia = selection
                                        expandedFreq = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Fecha Inicio
                    OutlinedTextField(
                        value = fechaInicio,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha de Inicio") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { 
                            IconButton(onClick = {
                                DatePickerDialog(context, { _, y, m, d ->
                                    fechaInicio = "$y-${m+1}-$d"
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                            }) {
                                Icon(Icons.Default.CalendarToday, null)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Mensaje de Apoyo
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it },
                        label = { Text("Mensaje de Apoyo (Opcional)") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (monto.isEmpty() || fechaInicio.isEmpty()) {
                                return@Button
                            }
                            val data = mapOf(
                                "user_id" to (user?.id ?: 0),
                                "mascota_id" to (mascotaId ?: 0),
                                "monto_mensual" to (monto.toDoubleOrNull() ?: 0.0),
                                "frecuencia" to frecuencia,
                                "fecha_inicio" to fechaInicio,
                                "mensaje_apoyo" to mensaje,
                                "estado" to "activo"
                            )
                            viewModel.createSuscripcion(data)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                    ) {
                        Text("CREAR SUSCRIPCIÓN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
