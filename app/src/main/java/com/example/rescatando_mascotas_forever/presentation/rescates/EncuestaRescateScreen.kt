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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados del formulario
    var nombreRescatista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    
    // Seccion 1
    var nombreMascota by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var vacunas by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var tamano by remember { mutableStateOf("") }
    var esterilizado by remember { mutableStateOf("") }
    var estadoSalud by remember { mutableStateOf("") }

    // Seccion 2
    var necesidadesEspeciales by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fechaRescate by remember { mutableStateOf("") }
    var comportamiento by remember { mutableStateOf("") }
    var condicionesEncontrado by remember { mutableStateOf("") }

    // Seccion 3
    var nombreReportante by remember { mutableStateOf("") }
    var numeroContacto by remember { mutableStateOf("") }
    var emailReportante by remember { mutableStateOf("") }
    var relacionMascota by remember { mutableStateOf("") }

    // Seccion 4
    var otrosAnimales by remember { mutableStateOf("") }
    var signosMaltrato by remember { mutableStateOf("") }

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
                    .background(Color(0xFFF6EEE9)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Degradado
                item {
                    GradientHeader("Encuesta de rescate")
                }

                // El Formulario
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
                                "FORMULARIO DE RESCATE DE MASCOTAS",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )

                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text("Nombre del rescatista", color = Color.Black, fontSize = 11.sp)
                                Spacer(Modifier.width(8.dp))
                                RescateTextField(nombreRescatista, Modifier.weight(1f)) { nombreRescatista = it }
                                Spacer(Modifier.width(16.dp))
                                Text("Fecha", color = Color.Black, fontSize = 11.sp)
                                Spacer(Modifier.width(8.dp))
                                RescateTextField(fecha, Modifier.width(100.dp)) { fecha = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.Black, thickness = 1.dp)

                            // Sección 1
                            Column(Modifier.fillMaxWidth()) {
                                Text("Seccion 1: Informacion de la mascota", color = Color.Black, fontSize = 12.sp)
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Nombre", nombreMascota, Modifier.weight(1f)) { nombreMascota = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Especie", especie, Modifier.weight(1f)) { especie = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Raza", raza, Modifier.weight(1f)) { raza = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Vacunas al dia", vacunas, Modifier.weight(1f)) { vacunas = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Edad aproximada", edad, Modifier.weight(1f)) { edad = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Sexo", sexo, Modifier.weight(0.5f)) { sexo = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Tamaño", tamano, Modifier.weight(0.5f)) { tamano = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Esterilizado", esterilizado, Modifier.weight(0.5f)) { esterilizado = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                RescateFormField("Estado de salud", estadoSalud, Modifier.fillMaxWidth()) { estadoSalud = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.Black, thickness = 1.dp)

                            // Sección 2
                            Column(Modifier.fillMaxWidth()) {
                                Text("Seccion 2: Detalles del rescate", color = Color.Black, fontSize = 12.sp)
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Necesidades especiales", necesidadesEspeciales, Modifier.weight(1f)) { necesidadesEspeciales = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Ubicación", ubicacion, Modifier.weight(1f)) { ubicacion = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Fecha del rescate", fechaRescate, Modifier.weight(1f)) { fechaRescate = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Comportamiento de la mascota", comportamiento, Modifier.weight(2f)) { comportamiento = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                RescateFormField("Condiciones en la que fue encontrado", condicionesEncontrado, Modifier.fillMaxWidth().height(80.dp), singleLine = false) { condicionesEncontrado = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.Black, thickness = 1.dp)

                            // Sección 3
                            Column(Modifier.fillMaxWidth()) {
                                Text("Seccion 3: Datos del reportante", color = Color.Black, fontSize = 12.sp)
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("Nombre completo", nombreReportante, Modifier.weight(1.5f)) { nombreReportante = it }
                                    Spacer(Modifier.width(8.dp))
                                    RescateFormField("Numero de contacto", numeroContacto, Modifier.weight(1f)) { numeroContacto = it }
                                }
                                Spacer(Modifier.height(8.dp))
                                RescateFormField("Correo electronico", emailReportante, Modifier.fillMaxWidth()) { emailReportante = it }
                                Spacer(Modifier.height(8.dp))
                                RescateFormField("Relacion con la mascota", relacionMascota, Modifier.fillMaxWidth(0.6f)) { relacionMascota = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.Black, thickness = 1.dp)

                            // Sección 4
                            Column(Modifier.fillMaxWidth()) {
                                Text("Seccion 4: Observaciones adicionales", color = Color.Black, fontSize = 12.sp)
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth()) {
                                    RescateFormField("¿Estaba con otros animales?", otrosAnimales, Modifier.weight(1f).height(80.dp), singleLine = false) { otrosAnimales = it }
                                    Spacer(Modifier.width(16.dp))
                                    RescateFormField("¿Mostraba signos de maltrato?", signosMaltrato, Modifier.weight(1f).height(80.dp), singleLine = false) { signosMaltrato = it }
                                }
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
fun RescateFormField(label: String, value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Black, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp))
        RescateTextField(value, onValueChange = onValueChange, singleLine = singleLine)
    }
}

@Composable
fun RescateTextField(
    value: String, 
    modifier: Modifier = Modifier, 
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, Color.Black, RoundedCornerShape(2.dp)),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
            ) {
                innerTextField()
            }
        }
    )
}
