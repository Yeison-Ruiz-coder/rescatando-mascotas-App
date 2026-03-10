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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroRescatistaScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados del formulario
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

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            bottomBar = {
                AppBottomBar(navController)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6EEE9)), // Fondo beige claro como en la imagen
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Degradado
                item {
                    GradientHeader("Formulario rescatista")
                }

                // El Formulario (Cuadro Blanco)
                item {
                    Card(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(0.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "REGISTRO DE RESCATISTA",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )

                            // Primera Sección: Datos Personales
                            Row(Modifier.fillMaxWidth()) {
                                FormField("Nombre completo", nombre, Modifier.weight(1f)) { nombre = it }
                                Spacer(Modifier.width(8.dp))
                                FormField("Documento de identidad", documento, Modifier.weight(1f)) { documento = it }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth()) {
                                FormField("Fecha de nacimiento", fechaNacimiento, Modifier.weight(1f)) { fechaNacimiento = it }
                                Spacer(Modifier.width(8.dp))
                                FormField("Whatsapp disponible", whatsapp, Modifier.weight(1f)) { whatsapp = it }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth()) {
                                FormField("Número de teléfono", telefono, Modifier.weight(1f)) { telefono = it }
                                Spacer(Modifier.width(8.dp))
                                FormField("Correo electrónico", email, Modifier.weight(1f)) { email = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Black, thickness = 1.dp)

                            // Segunda Sección: Información de Rescate
                            Row(Modifier.fillMaxWidth()) {
                                Column(Modifier.weight(1f)) {
                                    Text("Información de Rescate", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("¿Cuántos animales puede cuidar actualmente?", color = Color.Black, fontSize = 10.sp, lineHeight = 12.sp)
                                    Spacer(Modifier.height(4.dp))
                                    SimpleTextField(capacidadAnimales) { capacidadAnimales = it }
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = tieneTransporte, onClick = { tieneTransporte = !tieneTransporte })
                                        Text("Cuenta con transporte propio", color = Color.Black, fontSize = 10.sp)
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("¿Tiene espacio para albergarlos?", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = tieneEspacio == true, onClick = { tieneEspacio = true })
                                        Text("Si", color = Color.Black, fontSize = 10.sp)
                                        Spacer(Modifier.width(8.dp))
                                        RadioButton(selected = tieneEspacio == false, onClick = { tieneEspacio = false })
                                        Text("No", color = Color.Black, fontSize = 10.sp)
                                    }
                                    Text("¿Colabora con veterinarias ?", color = Color.Black, fontSize = 10.sp)
                                    Spacer(Modifier.height(4.dp))
                                    SimpleTextField(colaboraVeterinarias) { colaboraVeterinarias = it }
                                }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Black, thickness = 1.dp)

                            // Tercera Sección: Experiencia
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Tiempo rescatando animales", color = Color.Black, fontSize = 11.sp, modifier = Modifier.weight(1f))
                                SimpleTextField(tiempoRescatando, Modifier.weight(1f)) { tiempoRescatando = it }
                            }
                            
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Escribe una breve descripción de tu motivación o experiencia.",
                                color = Color.Black,
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            SimpleTextField(motivacion, Modifier.fillMaxWidth().height(80.dp), singleLine = false) { motivacion = it }

                            Spacer(Modifier.height(16.dp))
                            Column(Modifier.fillMaxWidth()) {
                                Text("Archivos Adjuntos", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(". Foto de perfil", color = Color.Black, fontSize = 10.sp)
                                Text(". Documento de identidad", color = Color.Black, fontSize = 10.sp)
                                Text(". Certificado de bienestar animal", color = Color.Black, fontSize = 10.sp)
                            }

                            Spacer(Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                RadioButton(selected = declaraVerdad, onClick = { declaraVerdad = !declaraVerdad })
                                Text("Declaro que toda la información suministrada es verdadera", color = Color.Black, fontSize = 10.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                RadioButton(selected = aceptaTerminos, onClick = { aceptaTerminos = !aceptaTerminos })
                                Text("Acepto los términos y condiciones del programa de rescate.", color = Color.Black, fontSize = 10.sp)
                            }
                        }
                    }
                }

                // Botones Finales
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E49BF)),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text("Descargar")
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.width(140.dp).border(1.dp, Color.LightGray, RoundedCornerShape(20.dp)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text("Imprimir", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Black, fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp))
        SimpleTextField(value, onValueChange = onValueChange)
    }
}

@Composable
fun SimpleTextField(
    value: String, 
    modifier: Modifier = Modifier, 
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 13.sp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFD9D9D9))
            .border(1.dp, Color.Gray, RoundedCornerShape(18.dp)),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
            ) {
                innerTextField()
            }
        }
    )
}
