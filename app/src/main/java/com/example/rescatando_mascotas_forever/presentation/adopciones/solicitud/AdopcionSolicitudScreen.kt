package com.example.rescatando_mascotas_forever.presentation.adopciones.solicitud

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionSolicitudScreen(
    navController: NavHostController,
    mascotaId: Int = 0
) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var ocupacion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var tieneMascotas by remember { mutableStateOf(false) }
    var motivo by remember { mutableStateOf("") }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF673AB7), Color(0xFF9C27B0))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Solicitud de Adopción", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (currentStep > 1) currentStep-- else navController.popBackStack() 
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(gradient))

            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)
            ) {
                SolicitudStepper(currentStep, totalSteps)
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                                } else {
                                    slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                                }.using(SizeTransform(clip = false))
                            }, label = "FormTransition"
                        ) { step ->
                            when (step) {
                                1 -> StepPersonal(nombre, { nombre = it }, telefono, { telefono = it })
                                2 -> StepEnvironment(ocupacion, { ocupacion = it }, direccion, { direccion = it }, tieneMascotas, { tieneMascotas = it })
                                3 -> StepMotivation(motivo, { motivo = it })
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 1) {
                        TextButton(onClick = { currentStep-- }) {
                            Text("ANTERIOR", color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(Modifier.width(80.dp))
                    }

                    Button(
                        onClick = {
                            if (currentStep < totalSteps) currentStep++
                            else { navController.popBackStack() }
                        },
                        modifier = Modifier.height(56.dp).width(160.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                    ) {
                        Text(if (currentStep == totalSteps) "ENVIAR" else "SIGUIENTE", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudStepper(currentStep: Int, totalSteps: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(if (isActive) Color.White else Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = i.toString(), color = if (isActive) Color(0xFF673AB7) else Color.White, fontWeight = FontWeight.Bold)
            }
            if (i < totalSteps) {
                Box(modifier = Modifier.width(40.dp).height(2.dp).background(if (i < currentStep) Color.White else Color.White.copy(alpha = 0.3f)))
            }
        }
    }
}

@Composable
fun StepPersonal(nombre: String, onNombre: (String) -> Unit, tel: String, onTel: (String) -> Unit) {
    Column {
        Text("Cuéntanos de ti", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E1A7A))
        Text("Queremos conocer a la persona que cuidará de nuestro peludito.", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        SolicitudInput(nombre, onNombre, "Nombre Completo", Icons.Default.Person)
        Spacer(modifier = Modifier.height(16.dp))
        SolicitudInput(tel, onTel, "Teléfono de Contacto", Icons.Default.Phone, KeyboardType.Phone)
    }
}

@Composable
fun StepEnvironment(ocup: String, onOcup: (String) -> Unit, dir: String, onDir: (String) -> Unit, tiene: Boolean, onTiene: (Boolean) -> Unit) {
    Column {
        Text("Tu Entorno", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E1A7A))
        Text("¿Cómo es el ambiente donde vivirá la mascota?", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        SolicitudInput(ocup, onOcup, "Ocupación / Profesión", Icons.Default.Work)
        Spacer(modifier = Modifier.height(16.dp))
        SolicitudInput(dir, onDir, "Dirección de Residencia", Icons.Default.Home)
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = tiene, onCheckedChange = onTiene, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7)))
            Text("¿Convives con otras mascotas?", fontSize = 15.sp)
        }
    }
}

@Composable
fun StepMotivation(motivo: String, onMotivo: (String) -> Unit) {
    Column {
        Text("El Compromiso", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E1A7A))
        Text("¿Por qué deseas adoptar y qué prometes al nuevo integrante?", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = motivo, onValueChange = onMotivo, modifier = Modifier.fillMaxWidth().height(200.dp),
            label = { Text("Escribe tus motivos aquí...") }, shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF673AB7))
        )
    }
}

@Composable
fun SolicitudInput(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, keyboardType: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF673AB7)) }, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF673AB7), unfocusedBorderColor = Color(0xFFE0E0E0)),
        singleLine = true
    )
}
