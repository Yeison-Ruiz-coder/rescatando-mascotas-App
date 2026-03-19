package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.BorderStroke
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
fun RegistroRescatistaScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isAdminMode = currentRoute.startsWith("admin_")

    // --- ESTADOS DEL FORMULARIO ---
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3

    var nombre by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var capacidadAnimales by remember { mutableStateOf("") }
    var tieneEspacio by remember { mutableStateOf<Boolean?>(null) }
    var tieneTransporte by remember { mutableStateOf(false) }

    var motivacion by remember { mutableStateOf("") }
    var declaraVerdad by remember { mutableStateOf(false) }
    var aceptaTerminos by remember { mutableStateOf(false) }

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
                item { GradientHeader("Registro de Rescatista") }

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
                                Icons.Default.Badge,
                                contentDescription = null,
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                "PASO $currentStep DE $totalSteps",
                                color = Color(0xFF673AB7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                "FORMULARIO DE RESCATISTA",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            LinearProgressIndicator(
                                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF673AB7),
                                trackColor = Color(0xFFEEEEEE),
                            )

                            when (currentStep) {
                                1 -> {
                                    // 1. DATOS PERSONALES
                                    SectionHeader(Icons.Default.Person, "Datos Personales")
                                    FormField("Nombre completo", nombre) { nombre = it }
                                    Row(Modifier.fillMaxWidth()) {
                                        FormField("Documento", documento, Modifier.weight(1f)) { documento = it }
                                        Spacer(Modifier.width(8.dp))
                                        FormField("F. Nacimiento", fechaNacimiento, Modifier.weight(1f)) { fechaNacimiento = it }
                                    }
                                    Row(Modifier.fillMaxWidth()) {
                                        FormField("WhatsApp", whatsapp, Modifier.weight(1f)) { whatsapp = it }
                                        Spacer(Modifier.width(8.dp))
                                        FormField("Email", email, Modifier.weight(1f)) { email = it }
                                    }
                                }
                                2 -> {
                                    // 2. INFORMACIÓN DE RESCATE
                                    SectionHeader(Icons.Default.Pets, "Capacidad y Logística")
                                    FormField("Capacidad de animales (número)", capacidadAnimales) { capacidadAnimales = it }

                                    Spacer(Modifier.height(12.dp))
                                    Text("¿Cuenta con espacio adecuado?", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        RadioButton(selected = tieneEspacio == true, onClick = { tieneEspacio = true })
                                        Text("Sí, tengo espacio", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                        Spacer(Modifier.width(16.dp))
                                        RadioButton(selected = tieneEspacio == false, onClick = { tieneEspacio = false })
                                        Text("No tengo espacio", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }

                                    Spacer(Modifier.height(12.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = tieneTransporte, onCheckedChange = { tieneTransporte = it })
                                        Text("Cuenta con transporte propio para rescates", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                }
                                3 -> {
                                    // 3. MOTIVACIÓN Y TÉRMINOS
                                    SectionHeader(Icons.Default.Favorite, "Experiencia y Compromiso")
                                    Text("Cuéntanos tu motivación y experiencia previa:", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
                                    SimpleTextField(motivacion, Modifier.height(120.dp), singleLine = false) { motivacion = it }

                                    Spacer(Modifier.height(20.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = declaraVerdad, onCheckedChange = { declaraVerdad = it })
                                        Text("Declaro que la información es verídica", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = aceptaTerminos, onCheckedChange = { aceptaTerminos = it })
                                        Text("Acepto términos y condiciones de voluntariado", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            Spacer(Modifier.height(30.dp))

                            // BOTONES DE NAVEGACIÓN
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (currentStep > 1) {
                                    OutlinedButton(
                                        onClick = { currentStep-- },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, Color(0xFF673AB7))
                                    ) {
                                        Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF673AB7))
                                        Spacer(Modifier.width(4.dp))
                                        Text("ANTERIOR", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (currentStep < totalSteps) {
                                    Button(
                                        onClick = { currentStep++ },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF673AB7),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("SIGUIENTE", fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(Modifier.width(4.dp))
                                        Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                                    }
                                } else {
                                    Button(
                                        onClick = { /* Lógica de guardado */ },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(Icons.Default.Save, null, tint = Color.White)
                                        Spacer(Modifier.width(4.dp))
                                        Text("ENVIAR", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }

                            if (currentStep == totalSteps) {
                                Spacer(Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color.Red)
                                ) {
                                    Text("CANCELAR", color = Color.Red, fontWeight = FontWeight.Bold)
                                }
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
fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    ) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.ExtraBold, color = Color(0xFF673AB7), fontSize = 15.sp)
    }
}

@Composable
fun FormField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        Text(label, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
        SimpleTextField(value, onValueChange = onValueChange)
    }
}

@Composable
fun SimpleTextField(value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F3F4))
            .border(1.dp, Color(0xFFDADCE0).copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                if (value.isEmpty()) Text("Escriba aquí...", color = Color(0xFF666666), fontSize = 14.sp)
                innerTextField()
            }
        }
    )
}
