package com.example.rescatando_mascotas_forever.presentation.adopciones

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados del formulario - Datos Personales
    var nombres by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var cc by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var oficina by remember { mutableStateOf("") }
    
    // Referencias
    var refNombre by remember { mutableStateOf("") }
    var refTelefono by remember { mutableStateOf("") }

    // Preguntas
    var p1 by remember { mutableStateOf("") }
    var p2SiNo by remember { mutableStateOf("") }
    var p2Cuales by remember { mutableStateOf("") }
    var p3SiNo by remember { mutableStateOf("") }
    var p3Porque by remember { mutableStateOf("") }
    var p4SiNo by remember { mutableStateOf("") }
    var p4Cuales by remember { mutableStateOf("") }
    var p5 by remember { mutableStateOf("") }
    var p6SiNo by remember { mutableStateOf("") }
    var p6Porque by remember { mutableStateOf("") }
    var p7 by remember { mutableStateOf("") }
    var p8SiNo by remember { mutableStateOf("") }
    var p9SiNo by remember { mutableStateOf("") }
    var p9Edades by remember { mutableStateOf("") }
    var p10 by remember { mutableStateOf("") }
    var p11Arrendadores by remember { mutableStateOf("") }
    var p11Propio by remember { mutableStateOf("") }
    var p11PropietarioInfo by remember { mutableStateOf("") }

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
                    GradientHeader("Formulario de adopción")
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
                                "FORMULARIO DE ADOPCIÓN",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )

                            // Datos Personales
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("Apellidos y Nombres", nombres, Modifier.weight(2f)) { nombres = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Telefono", telefono, Modifier.weight(1f)) { telefono = it }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("Fecha", fecha, Modifier.weight(1f)) { fecha = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Ciudad", ciudad, Modifier.weight(1f)) { ciudad = it }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("C.C", cc, Modifier.weight(1f)) { cc = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Estado civil", estadoCivil, Modifier.weight(1f)) { estadoCivil = it }
                            }
                            Spacer(Modifier.height(8.dp))
                            AdopcionFormField("Dirección", direccion, Modifier.fillMaxWidth()) { direccion = it }
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("E-mail", email, Modifier.weight(1f)) { email = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Telefono (fijo)", celular, Modifier.weight(1f)) { celular = it } // Reorganizado para claridad
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                AdopcionFormField("Celular", celular, Modifier.weight(1f)) { celular = it }
                                Spacer(Modifier.width(8.dp))
                                AdopcionFormField("Oficina", oficina, Modifier.weight(1f)) { oficina = it }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Black)

                            AdopcionFormField("Nombre de referencia personal", refNombre, Modifier.fillMaxWidth()) { refNombre = it }
                            Spacer(Modifier.height(8.dp))
                            AdopcionFormField("Telefono de referencia personal", refTelefono, Modifier.fillMaxWidth()) { refTelefono = it }

                            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Black)

                            Text("Preguntas y Respuestas", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(12.dp))

                            // Pregunta 1
                            PreguntaItem("1", "¿Por qué desea adoptar una mascota?", p1, { p1 = it }, height = 60.dp)
                            
                            // Pregunta 2
                            Row(Modifier.fillMaxWidth()) {
                                PreguntaItem("2", "¿Actualmente tiene otros animales?", p2SiNo, { p2SiNo = it }, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                PreguntaItem("", "¿Cuáles?", p2Cuales, { p2Cuales = it }, Modifier.weight(1f))
                            }

                            // Pregunta 3
                            Row(Modifier.fillMaxWidth()) {
                                PreguntaItem("3", "¿Están esterilizados?", p3SiNo, { p3SiNo = it }, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                PreguntaItem("", "¿Por qué?", p3Porque, { p3Porque = it }, Modifier.weight(1f))
                            }

                            // Pregunta 4
                            Row(Modifier.fillMaxWidth()) {
                                PreguntaItem("4", "¿Anteriormente ha tenido otros animales?", p4SiNo, { p4SiNo = it }, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                PreguntaItem("", "¿Cuáles?", p4Cuales, { p4Cuales = it }, Modifier.weight(1f))
                            }

                            // Pregunta 5
                            PreguntaItem("5", "¿Qué fue lo que pasó con él/ellos?", p5, { p5 = it })

                            // Pregunta 6
                            PreguntaItem("6", "¿Está de acuerdo en visita periódica?", p6SiNo, { p6SiNo = it })
                            PreguntaItem("", "¿Por qué?", p6Porque, { p6Porque = it })

                            // Pregunta 7 y 8
                            Row(Modifier.fillMaxWidth()) {
                                PreguntaItem("7", "¿Cuántas personas viven en casa?", p7, { p7 = it }, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                PreguntaItem("8", "¿Todos de acuerdo?", p8SiNo, { p8SiNo = it }, Modifier.weight(1f))
                            }

                            // Pregunta 9
                            Row(Modifier.fillMaxWidth()) {
                                PreguntaItem("9", "¿Hay niños en casa?", p9SiNo, { p9SiNo = it }, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                PreguntaItem("", "Edades", p9Edades, { p9Edades = it }, Modifier.weight(1f))
                            }

                            // Pregunta 10
                            PreguntaItem("10", "¿Alguien alérgico o con asma?", p10, { p10 = it })

                            // Pregunta 11
                            PreguntaItem("11", "¿Permiten mascotas en alquiler?", p11Arrendadores, { p11Arrendadores = it })
                            PreguntaItem("", "Nombre y tlf propietario", p11PropietarioInfo, { p11PropietarioInfo = it })
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
fun AdopcionFormField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Black, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp))
        AdopcionTextField(value, onValueChange = onValueChange)
    }
}

@Composable
fun PreguntaItem(num: String, text: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier, height: androidx.compose.ui.unit.Dp = 36.dp) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (num.isNotEmpty()) {
                Text(num, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(16.dp))
            }
            Text(text, color = Color.Black, fontSize = 10.sp, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(2.dp))
        AdopcionTextField(value, Modifier.height(height), onValueChange = onValueChange, singleLine = height <= 40.dp)
    }
}

@Composable
fun AdopcionTextField(
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
