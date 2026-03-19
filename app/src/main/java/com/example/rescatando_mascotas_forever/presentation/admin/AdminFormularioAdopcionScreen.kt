package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isAdminMode = currentRoute.startsWith("admin_")

    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var ocupacion by remember { mutableStateOf("") }
    
    var tipoVivienda by remember { mutableStateOf("Casa") }
    var tienePatio by remember { mutableStateOf(false) }
    var tieneProtecciones by remember { mutableStateOf(false) }
    
    var integrantes by remember { mutableStateOf("") }
    var hayNinos by remember { mutableStateOf(false) }
    var estanDeAcuerdo by remember { mutableStateOf(true) }
    
    var tieneOtrasMascotas by remember { mutableStateOf(false) }
    var experienciaPrevia by remember { mutableStateOf("") }
    
    var tiempoDiario by remember { mutableStateOf("") }
    var presupuestoVeterinario by remember { mutableStateOf(true) }

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
            bottomBar = { if (!isAdminMode) AppBottomBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { GradientHeader("Solicitud de Adopción") }

                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Pets, 
                                contentDescription = null, 
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                "FORMULARIO COMPLETO DE ADOPCIÓN",
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            // 1. DATOS DEL SOLICITANTE
                            AdopcionSectionHeader(Icons.Default.Person, "Datos del Solicitante")
                            AdopcionFormField("Nombre completo", nombre) { nombre = it }
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("DNI / Cédula", dni, Modifier.weight(1f)) { dni = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Edad", edad, Modifier.weight(0.5f)) { edad = it }
                            }
                            AdopcionFormField("Ocupación / Profesión", ocupacion) { ocupacion = it }
                            AdopcionFormField("Teléfono de contacto", telefono) { telefono = it }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // 2. ENTORNO Y VIVIENDA
                            AdopcionSectionHeader(Icons.Default.Home, "Entorno y Vivienda")
                            Text("¿En qué tipo de vivienda habitará la mascota?", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth())
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(selected = tipoVivienda == "Casa", onClick = { tipoVivienda = "Casa" }, label = { Text("Casa") })
                                FilterChip(selected = tipoVivienda == "Apartamento", onClick = { tipoVivienda = "Apartamento" }, label = { Text("Apto") })
                                FilterChip(selected = tipoVivienda == "Finca", onClick = { tipoVivienda = "Finca" }, label = { Text("Finca") })
                            }
                            
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Checkbox(checked = tienePatio, onCheckedChange = { tienePatio = it })
                                Text("Cuenta con patio o balcón seguro", fontSize = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Checkbox(checked = tieneProtecciones, onCheckedChange = { tieneProtecciones = it })
                                Text("Tiene protecciones (mallas/muros)", fontSize = 13.sp)
                            }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // 3. NÚCLEO FAMILIAR
                            AdopcionSectionHeader(Icons.Default.FamilyRestroom, "Núcleo Familiar")
                            AdopcionFormField("¿Cuántas personas viven en casa?", integrantes) { integrantes = it }
                            
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Switch(checked = hayNinos, onCheckedChange = { hayNinos = it })
                                Spacer(Modifier.width(12.dp))
                                Text("¿Viven niños en el hogar?", fontSize = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Switch(checked = estanDeAcuerdo, onCheckedChange = { estanDeAcuerdo = it })
                                Spacer(Modifier.width(12.dp))
                                Text("¿Todos están de acuerdo con adoptar?", fontSize = 13.sp)
                            }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // 4. EXPERIENCIA Y MASCOTAS
                            AdopcionSectionHeader(Icons.Default.History, "Experiencia y Mascotas")
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Checkbox(checked = tieneOtrasMascotas, onCheckedChange = { tieneOtrasMascotas = it })
                                Text("¿Tiene otras mascotas actualmente?", fontSize = 13.sp)
                            }
                            AdopcionFormField("Cuéntanos tu experiencia previa con animales:", experienciaPrevia, Modifier.height(80.dp), singleLine = false) { experienciaPrevia = it }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // 5. COMPROMISO
                            AdopcionSectionHeader(Icons.Default.FavoriteBorder, "Compromiso de Vida")
                            AdopcionFormField("¿Cuánto tiempo diario dedicará a la mascota?", tiempoDiario) { tiempoDiario = it }
                            
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Checkbox(checked = presupuestoVeterinario, onCheckedChange = { presupuestoVeterinario = it })
                                Text("Cuenta con solvencia para gastos veterinarios", fontSize = 13.sp)
                            }

                            Spacer(Modifier.height(30.dp))

                            // BOTONES DE ACCIÓN
                            Button(
                                onClick = { /* Lógica de guardado */ },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                            ) {
                                Icon(Icons.Default.Save, null)
                                Spacer(Modifier.width(8.dp))
                                Text("REGISTRAR SOLICITUD", fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(Modifier.height(12.dp))
                            
                            OutlinedButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFF673AB7))
                            ) {
                                Text("CANCELAR", color = Color(0xFF673AB7))
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun AdopcionSectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp, top = 8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF673AB7).copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7), fontSize = 15.sp)
    }
}

@Composable
fun AdopcionFormField(label: String, value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(label, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) 45.dp else 80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F3F4))
                .border(1.dp, Color(0xFFDADCE0).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Box(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    if (value.isEmpty()) Text("Escriba aquí...", color = Color.LightGray, fontSize = 14.sp)
                    innerTextField()
                }
            }
        )
    }
}
