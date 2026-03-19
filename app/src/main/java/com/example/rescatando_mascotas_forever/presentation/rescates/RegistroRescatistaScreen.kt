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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    var nombre by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var capacidadAnimales by remember { mutableStateOf("") }
    var colaboraVeterinarias by remember { mutableStateOf("") }
    var tiempoRescatando by remember { mutableStateOf("") }
    var motivacion by remember { mutableStateOf("") }
    
    var tieneEspacio by remember { mutableStateOf<Boolean?>(null) }
    var tieneTransporte by remember { mutableStateOf(false) }
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
                item { GradientHeader("Formulario Rescatista") }

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
                                Icons.Default.Badge, 
                                contentDescription = null, 
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                "REGISTRO DE RESCATISTA",
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            // Datos Personales
                            SectionHeader(Icons.Default.Person, "Datos Personales")
                            Row(Modifier.fillMaxWidth()) {
                                FormField("Nombre completo", nombre, Modifier.weight(1f)) { nombre = it }
                                Spacer(Modifier.width(8.dp))
                                FormField("Documento", documento, Modifier.weight(1f)) { documento = it }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth()) {
                                FormField("Nacimiento", fechaNacimiento, Modifier.weight(1f)) { fechaNacimiento = it }
                                Spacer(Modifier.width(8.dp))
                                FormField("WhatsApp", whatsapp, Modifier.weight(1f)) { whatsapp = it }
                            }
                            Spacer(Modifier.height(12.dp))
                            FormField("Correo electrónico", email) { email = it }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // Información de Rescate
                            SectionHeader(Icons.Default.Pets, "Información de Rescate")
                            Row(Modifier.fillMaxWidth()) {
                                Column(Modifier.weight(1f)) {
                                    Text("Capacidad actual", color = Color.Gray, fontSize = 11.sp)
                                    SimpleTextField(capacidadAnimales) { capacidadAnimales = it }
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                        Checkbox(checked = tieneTransporte, onCheckedChange = { tieneTransporte = it })
                                        Text("Transporte propio", color = Color.DarkGray, fontSize = 11.sp)
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("¿Tiene espacio?", color = Color.Gray, fontSize = 11.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = tieneEspacio == true, onClick = { tieneEspacio = true })
                                        Text("Sí", fontSize = 11.sp)
                                        RadioButton(selected = tieneEspacio == false, onClick = { tieneEspacio = false })
                                        Text("No", fontSize = 11.sp)
                                    }
                                }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 20.dp), color = Color(0xFFEEEEEE))

                            // Motivación
                            SectionHeader(Icons.Default.Edit, "Experiencia y Motivación")
                            Text("Describe tu experiencia rescatando animales:", color = Color.Gray, fontSize = 11.sp)
                            SimpleTextField(motivacion, Modifier.height(100.dp), singleLine = false) { motivacion = it }

                            Spacer(Modifier.height(24.dp))
                            
                            // Declaraciones
                            Column(Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = declaraVerdad, onCheckedChange = { declaraVerdad = it })
                                    Text("Declaro que la información es verdadera", fontSize = 11.sp, color = Color.Gray)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = aceptaTerminos, onCheckedChange = { aceptaTerminos = it })
                                    Text("Acepto los términos y condiciones", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
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
                            Icon(Icons.Default.FileDownload, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Guardar")
                        }
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF673AB7))
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
fun SectionHeader(icon: ImageVector, title: String) {
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
fun FormField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp))
        SimpleTextField(value, onValueChange = onValueChange)
    }
}

@Composable
fun SimpleTextField(value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
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
