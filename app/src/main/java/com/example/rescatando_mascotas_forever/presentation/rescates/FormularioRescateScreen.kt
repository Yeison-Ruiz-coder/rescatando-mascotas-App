package com.example.rescatando_mascotas_forever.presentation.rescates

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRescateScreen(
    navController: NavHostController,
    viewModel: RescateViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val reportState by viewModel.reportState.collectAsState()
    val location by viewModel.currentLocation.collectAsState()
    val galeriaUris by viewModel.galeriaUris.collectAsState()

    // --- ESTADOS DEL FORMULARIO ---
    var lugarRescate by remember { mutableStateOf("") }
    var descripcionEstado by remember { mutableStateOf("") }
    var clasificacion by remember { mutableStateOf("Otro") }
    var fechaRescate by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Datos del Reportante
    var nombreReportante by remember { mutableStateOf("") }
    var emailReportante by remember { mutableStateOf("") }
    var telefonoReportante by remember { mutableStateOf("") }

    // Permisos de Ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            viewModel.obtenerUbicacionActual(context)
        } else {
            Toast.makeText(context, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        viewModel.setGaleriaUris(uris)
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context, { _, year, month, day ->
            fechaRescate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year)
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(reportState) {
        if (reportState is RescateReportState.Success) {
            Toast.makeText(context, "Reporte enviado con éxito", Toast.LENGTH_LONG).show()
            navController.popBackStack()
            viewModel.resetState()
        } else if (reportState is RescateReportState.Error) {
            Toast.makeText(context, (reportState as RescateReportState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Pets, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Reportar un Rescate", color = MaterialTheme.colorScheme.primary, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        text = "Tu reporte puede salvar una vida. Completa los detalles del animal en emergencia.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                            // SECCIÓN 1: FOTOS
                            Text("Foto de la mascota *", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(
                                    modifier = Modifier.size(130.dp).clip(RoundedCornerShape(24.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .clickable { launcher.launch("image/*") },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (imageUri == null) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.AddAPhoto, null, tint = MaterialTheme.colorScheme.primary)
                                            Text("Principal", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Image(rememberAsyncImagePainter(imageUri), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                    }
                                }

                                Box(
                                    modifier = Modifier.size(130.dp).clip(RoundedCornerShape(24.dp))
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                        .clickable { galeriaLauncher.launch("image/*") },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Collections, null, tint = MaterialTheme.colorScheme.secondary)
                                        Text(if (galeriaUris.isEmpty()) "Galería" else "${galeriaUris.size} fotos", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SECCIÓN 2: CAMPOS
                            RescueFormField("Lugar del rescate *", "Ejemplo: Parque Central...", lugarRescate) { lugarRescate = it }

                            Text("Descripción del estado *", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = descripcionEstado, onValueChange = { descripcionEstado = it },
                                placeholder = { Text("Describe heridas, comportamiento...", fontSize = 14.sp) },
                                modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // SECCIÓN 3: CLASIFICACIÓN
                            Text("Clasificar emergencia", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClassificationChip("Urgente", Icons.Default.PriorityHigh, clasificacion == "Urgente", Color(0xFFE53935)) { clasificacion = "Urgente" }
                                ClassificationChip("Herido", Icons.Default.MedicalServices, clasificacion == "Herido", Color(0xFFFB8C00)) { clasificacion = "Herido" }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClassificationChip("Abandonado", Icons.Default.Home, clasificacion == "Abandonado", Color(0xFFFFC107)) { clasificacion = "Abandonado" }
                                ClassificationChip("Otro", Icons.Default.Info, clasificacion == "Otro", Color(0xFF4CAF50)) { clasificacion = "Otro" }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SECCIÓN 4: FECHA Y MAPA
                            Text("Fecha del rescate *", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = fechaRescate, onValueChange = {}, readOnly = true,
                                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.primary) },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { datePickerDialog.show() },
                                enabled = false,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.LocationOn, null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (location != null) "Ubicación fijada ✓" else "Usar mi ubicación actual")
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(16.dp))

                            // SECCIÓN 5: DATOS CONTACTO
                            Text("👤 Tus datos (opcionales)", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            RescueFormField("Tu nombre", "Nombre...", nombreReportante) { nombreReportante = it }
                            RescueFormField("Correo electrónico", "email@ejemplo.com", emailReportante) { emailReportante = it }
                            RescueFormField("Teléfono", "300...", telefonoReportante) { telefonoReportante = it }

                            Spacer(modifier = Modifier.height(40.dp))

                            // BOTONES FINALES AJUSTADOS
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) { 
                                    Text("CANCELAR", color = Color(0xFFE57373), fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1, softWrap = false) 
                                }

                                Button(
                                    onClick = {
                                        viewModel.reportarRescate(
                                            context, lugarRescate, descripcionEstado, fechaRescate, "Publico", clasificacion,
                                            true, true, false, nombreReportante, emailReportante, telefonoReportante,
                                            location?.first, location?.second, imageUri, {}
                                        )
                                    },
                                    modifier = Modifier.weight(2.5f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp),
                                    enabled = reportState !is RescateReportState.Loading && lugarRescate.isNotBlank()
                                ) {
                                    if (reportState is RescateReportState.Loading) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                    } else {
                                        Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("REPORTAR RESCATE", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun RescueFormField(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(16.dp), singleLine = true
        )
    }
}

@Composable
fun RowScope.ClassificationChip(label: String, icon: ImageVector, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.weight(1f).height(50.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (isSelected) color else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
