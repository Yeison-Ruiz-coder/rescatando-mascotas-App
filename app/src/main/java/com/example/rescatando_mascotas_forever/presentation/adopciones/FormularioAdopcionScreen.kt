package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAdopcionScreen(
    navController: NavHostController,
    viewModel: FormularioAdopcionViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFF8F9FA)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Header con Progreso
                FormHeader(
                    currentPage = viewModel.currentPage,
                    totalPages = viewModel.totalPages
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                            .offset(y = (-30).dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            AnimatedContent(
                                targetState = viewModel.currentPage,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        slideInHorizontally { it } + fadeIn() togetherWith
                                                slideOutHorizontally { -it } + fadeOut()
                                    } else {
                                        slideInHorizontally { -it } + fadeIn() togetherWith
                                                slideOutHorizontally { it } + fadeOut()
                                    }
                                },
                                label = "stepTransition"
                            ) { step ->
                                when (step) {
                                    1 -> StepInformacionPersonal(viewModel)
                                    2 -> StepEntornoMotivo(viewModel)
                                    3 -> StepCompromiso(viewModel)
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Botones de Navegación
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (viewModel.currentPage > 1) {
                                    OutlinedButton(
                                        onClick = { viewModel.previousStep() },
                                        modifier = Modifier.weight(1f).height(56.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text("Atrás", fontWeight = FontWeight.Bold)
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (viewModel.currentPage < viewModel.totalPages) {
                                            viewModel.nextStep()
                                        } else {
                                            viewModel.enviarSolicitud {
                                                navController.popBackStack()
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(2f).height(56.dp),
                                    enabled = viewModel.isStepValid(viewModel.currentPage) && !viewModel.isSaving,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                                ) {
                                    if (viewModel.isSaving) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                    } else {
                                        Text(
                                            if (viewModel.currentPage == viewModel.totalPages) "ENVIAR" else "CONTINUAR",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Icon(if (viewModel.currentPage == viewModel.totalPages) Icons.Default.Check else Icons.Default.ArrowForward, null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormHeader(currentPage: Int, totalPages: Int) {
    val progress by animateFloatAsState(targetValue = currentPage.toFloat() / totalPages, label = "progress")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF673AB7), Color(0xFF9575CD))
                )
            )
            .padding(top = 24.dp, bottom = 54.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            "Solicitud de Adopción",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Paso $currentPage de $totalPages",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun StepInformacionPersonal(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Person, "Datos Personales", "Queremos conocerte un poco más")
        ModernInputField(value = viewModel.nombreCompleto, onValueChange = { viewModel.nombreCompleto = it }, label = "Nombre Completo", icon = Icons.Default.Badge)
        ModernInputField(value = viewModel.edad, onValueChange = { viewModel.edad = it }, label = "Edad", icon = Icons.Default.CalendarToday)
        ModernInputField(value = viewModel.direccion, onValueChange = { viewModel.direccion = it }, label = "Dirección", icon = Icons.Default.Home)
        ModernInputField(value = viewModel.telefono, onValueChange = { viewModel.telefono = it }, label = "Teléfono", icon = Icons.Default.Phone)
    }
}

@Composable
fun StepEntornoMotivo(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Pets, "Tu Hogar", "Cuéntanos sobre el futuro hogar")
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Icon(Icons.Default.Info, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text("¿Tienes otras mascotas?", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Switch(checked = viewModel.tieneOtrasMascotas, onCheckedChange = { viewModel.tieneOtrasMascotas = it })
        }

        ModernInputField(value = viewModel.tiempoDisponible, onValueChange = { viewModel.tiempoDisponible = it }, label = "¿Cuánto tiempo dedicarás al día?", icon = Icons.Default.Timer)

        OutlinedTextField(
            value = viewModel.motivo,
            onValueChange = { viewModel.motivo = it },
            label = { Text("¿Por qué deseas adoptar?") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF673AB7))
        )
    }
}

@Composable
fun StepCompromiso(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Favorite, "Compromiso", "Finaliza tu solicitud")
        
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                "Al enviar esta solicitud, te comprometes a brindar un hogar lleno de amor, cuidados veterinarios y protección a la mascota. Nos pondremos en contacto contigo pronto.",
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF4A148C)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Checkbox(checked = viewModel.aceptaTerminos, onCheckedChange = { viewModel.aceptaTerminos = it })
            Text("Acepto los términos y condiciones de adopción", fontSize = 14.sp)
        }
    }
}

@Composable
fun StepTitle(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF673AB7).copy(alpha = 0.1f),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.padding(12.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ModernInputField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF673AB7)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF673AB7),
            focusedLabelColor = Color(0xFF673AB7)
        )
    )
}
