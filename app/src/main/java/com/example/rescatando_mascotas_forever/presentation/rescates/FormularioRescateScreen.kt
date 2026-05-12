package com.example.rescatando_mascotas_forever.presentation.rescates

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- ESTADOS DEL FORMULARIO ---
    var lugarRescate by remember { mutableStateOf("") }
    var descripcionEstado by remember { mutableStateOf("") }
    var clasificacion by remember { mutableStateOf("Otro") } // Urgente, Herido, Abandonado, Otro
    var fechaRescate by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Datos del Reportante
    var nombreReportante by remember { mutableStateOf("") }
    var emailReportante by remember { mutableStateOf("") }
    var telefonoReportante by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, day ->
            fechaRescate = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val mainGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A237E), Color(0xFF311B92), Color(0xFF4527A0))
    )

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(mainGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título e Icono de Huella
                    Icon(
                        Icons.Default.Pets,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Reportar un Rescate",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Tu reporte puede salvar una vida. Completa los detalles del animal en situación de emergencia.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Card del Formulario
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // SECCIÓN 1: FOTO DE LA MASCOTA
                            Text(
                                "Foto de la mascota *",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFFF3E5F5))
                                    .clickable { launcher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageUri == null) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.AddAPhoto, null, tint = Color(0xFF673AB7), modifier = Modifier.size(40.dp))
                                        Text("Subir foto", color = Color(0xFF673AB7), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SECCIÓN 2: LUGAR Y DESCRIPCIÓN
                            RescueFormField("Lugar del rescate *", "Ejemplo: Parque Central, calle 123...", lugarRescate) { lugarRescate = it }
                            
                            Text(
                                "Descripción del estado del animal *",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = descripcionEstado,
                                onValueChange = { descripcionEstado = it },
                                placeholder = { Text("Describe su estado físico, heridas, comportamiento...", fontSize = 14.sp) },
                                modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF673AB7),
                                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // SECCIÓN 3: CLASIFICACIÓN
                            Text(
                                "Clasificar manualmente (opcional)",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ClassificationChip("Urgente", Icons.Default.PriorityHigh, clasificacion == "Urgente", Color(0xFFE53935)) { clasificacion = "Urgente" }
                                ClassificationChip("Herido", Icons.Default.MedicalServices, clasificacion == "Herido", Color(0xFFFB8C00)) { clasificacion = "Herido" }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ClassificationChip("Abandonado", Icons.Default.Home, clasificacion == "Abandonado", Color(0xFF1E88E5)) { clasificacion = "Abandonado" }
                                ClassificationChip("Otro", Icons.Default.Info, clasificacion == "Otro", Color(0xFF757575)) { clasificacion = "Otro" }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SECCIÓN 4: FECHA Y MAPA
                            Text(
                                "Fecha del rescate *",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = fechaRescate,
                                onValueChange = { },
                                readOnly = true,
                                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF673AB7)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { datePickerDialog.show() },
                                enabled = false,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                    disabledTextColor = Color.Black,
                                    disabledLabelColor = Color.Gray
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Placeholder de Mapa (Ubicación exacta)
                            Text(
                                "Ubicación exacta en el mapa",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFEEEEEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.LocationOn, null, tint = Color.Red, modifier = Modifier.size(32.dp))
                                    Text("Simulación de Mapa", color = Color.Gray, fontSize = 12.sp)
                                    Button(
                                        onClick = { /* Lógica GPS */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Usar mi ubicación actual", fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            
                            // SECCIÓN 5: DATOS DEL REPORTANTE (Opcional)
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "👤 Tus datos (opcionales para contacto)",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            RescueFormField("Tu nombre", "Ejemplo: Juan Pérez", nombreReportante) { nombreReportante = it }
                            RescueFormField("Correo electrónico", "juan@ejemplo.com", emailReportante) { emailReportante = it }
                            RescueFormField("Teléfono de contacto", "3001234567", telefonoReportante) { telefonoReportante = it }

                            Spacer(modifier = Modifier.height(40.dp))

                            // BOTONES FINALES
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE57373))
                                ) {
                                    Text("CANCELAR", color = Color(0xFFE57373), fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { /* Lógica Railway */ },
                                    modifier = Modifier.weight(1.5f).height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                                ) {
                                    Icon(Icons.Default.Send, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("REPORTAR RESCATE", fontWeight = FontWeight.ExtraBold)
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
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF673AB7),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun RowScope.ClassificationChip(label: String, icon: ImageVector, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.weight(1f).height(50.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.1f) else Color.White,
        border = BorderStroke(1.dp, if (isSelected) color else Color.LightGray.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = if (isSelected) color else Color.Gray, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) color else Color.Gray)
        }
    }
}
