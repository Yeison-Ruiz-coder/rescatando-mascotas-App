package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import com.example.rescatando_mascotas_forever.presentation.admin.AdminDrawerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isAdminMode = currentRoute.startsWith("admin_")

    var nombreRescatista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var nombreMascota by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var vacunas by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var tamano by remember { mutableStateOf("") }
    var esterilizado by remember { mutableStateOf("") }
    var estadoSalud by remember { mutableStateOf("") }
    var necesidadesEspeciales by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fechaRescate by remember { mutableStateOf("") }
    var comportamiento by remember { mutableStateOf("") }
    var condicionesEncontrado by remember { mutableStateOf("") }
    var nombreReportante by remember { mutableStateOf("") }
    var numeroContacto by remember { mutableStateOf("") }
    var emailReportante by remember { mutableStateOf("") }
    var relacionMascota by remember { mutableStateOf("") }
    var otrosAnimales by remember { mutableStateOf("") }
    var signosMaltrato by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                if (isAdminMode) {
                    AdminDrawerContent(navController, drawerState, scope)
                } else {
                    com.example.rescatando_mascotas_forever.presentation.common.components.DrawerContent(navController, drawerState, scope)
                }
            }
        }
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { if (!isAdminMode) AppBottomBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { GradientHeader("Encuesta de Rescate") }

                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Assignment, 
                                contentDescription = null, 
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                "REPORTE DE RESCATE",
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            // Encabezado rápido
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                RescateFormField("Rescatista", nombreRescatista, Modifier.weight(1f)) { nombreRescatista = it }
                                Spacer(Modifier.width(8.dp))
                                RescateFormField("Fecha", fecha, Modifier.width(100.dp)) { fecha = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // Sección 1: Mascota
                            RescateSectionHeader(Icons.Default.Pets, "Información de la Mascota")
                            Row(Modifier.fillMaxWidth()) {
                                RescateFormField("Nombre", nombreMascota, Modifier.weight(1f)) { nombreMascota = it }
                                Spacer(Modifier.width(8.dp))
                                RescateFormField("Especie", especie, Modifier.weight(1f)) { especie = it }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                RescateFormField("Edad", edad, Modifier.weight(1f)) { edad = it }
                                Spacer(Modifier.width(8.dp))
                                RescateFormField("Sexo", sexo, Modifier.weight(1f)) { sexo = it }
                            }
                            Spacer(Modifier.height(8.dp))
                            RescateFormField("Estado de Salud", estadoSalud) { estadoSalud = it }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // Sección 2: Detalles Rescate
                            RescateSectionHeader(Icons.Default.LocationOn, "Detalles del Rescate")
                            RescateFormField("Ubicación del hallazgo", ubicacion) { ubicacion = it }
                            Spacer(Modifier.height(8.dp))
                            RescateFormField("Condiciones encontradas", condicionesEncontrado, Modifier.height(80.dp), singleLine = false) { condicionesEncontrado = it }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // Sección 3: Reportante
                            RescateSectionHeader(Icons.Default.ContactPhone, "Datos del Reportante")
                            RescateFormField("Nombre Reportante", nombreReportante) { nombreReportante = it }
                            Spacer(Modifier.height(8.dp))
                            RescateFormField("Teléfono contacto", numeroContacto) { numeroContacto = it }

                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                        ) {
                            Icon(Icons.Default.Save, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Guardar")
                        }
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7))
                        ) {
                            Icon(Icons.Default.Print, null, tint = Color(0xFF673AB7))
                            Spacer(Modifier.width(8.dp))
                            Text("Imprimir", color = Color(0xFF673AB7))
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun RescateSectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    ) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7), fontSize = 14.sp)
    }
}

@Composable
fun RescateFormField(label: String, value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp))
        RescateTextFieldCustom(value, onValueChange = onValueChange, singleLine = singleLine)
    }
}

@Composable
fun RescateTextFieldCustom(value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F3F4))
            .border(1.dp, Color(0xFFDADCE0), RoundedCornerShape(12.dp)),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                if (value.isEmpty()) Text("Escriba aquí...", color = Color.LightGray, fontSize = 14.sp)
                innerTextField()
            }
        }
    )
}
