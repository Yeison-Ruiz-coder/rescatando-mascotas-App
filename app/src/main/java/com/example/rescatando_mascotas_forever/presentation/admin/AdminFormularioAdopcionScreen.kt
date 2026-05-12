package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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

// ==========================
// MODELO STEP
// ==========================

data class FormStep(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

// ==========================
// SCREEN PRINCIPAL
// ==========================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFormularioAdopcionScreen(
    navController: NavHostController,
    viewModel: AdminFormularioAdopcionViewModel = viewModel()
) {
    val formSteps = listOf(
        FormStep("Datos personales", "Información básica", Icons.Default.Person),
        FormStep("Contacto", "Comunicación", Icons.Default.Phone),
        FormStep("Vivienda", "Entorno del hogar", Icons.Default.Home),
        FormStep("Familia", "Integrantes del hogar", Icons.Default.Groups),
        FormStep("Experiencia", "Compromiso y cuidado", Icons.Default.Favorite)
    )

    Scaffold(
        containerColor = Color(0xFFF4F6FA)
    ) { padding ->
        if (viewModel.isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1A237E))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ==========================
            // HEADER CON PAGINACIÓN (DOTS)
            // ==========================
            AdoptionFormHeader(
                currentPage = viewModel.currentPage,
                totalPages = viewModel.totalPages,
                step = formSteps[viewModel.currentPage - 1]
            )

            // ==========================
            // FORM CARD
            // ==========================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    AnimatedContent(
                        targetState = viewModel.currentPage,
                        modifier = Modifier.fillMaxSize(),
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { it } + fadeIn() togetherWith
                                        slideOutHorizontally { -it } + fadeOut()
                            } else {
                                slideInHorizontally { -it } + fadeIn() togetherWith
                                        slideOutHorizontally { it } + fadeOut()
                            }
                        },
                        label = "stepAnimation"
                    ) { page ->
                        when (page) {
                            1 -> AdopcionStep1(
                                viewModel.nombre, { viewModel.nombre = it },
                                viewModel.dni, { viewModel.dni = it },
                                viewModel.edad, { viewModel.edad = it }
                            )
                            2 -> AdopcionStep2(
                                viewModel.ocupacion, { viewModel.ocupacion = it },
                                viewModel.telefono, { viewModel.telefono = it }
                            )
                            3 -> AdopcionStep3(
                                viewModel.tipoVivienda, { viewModel.tipoVivienda = it },
                                viewModel.tienePatio, { viewModel.tienePatio = it },
                                viewModel.tieneProtecciones, { viewModel.tieneProtecciones = it }
                            )
                            4 -> AdopcionStep4(
                                viewModel.integrantes, { viewModel.integrantes = it },
                                viewModel.hayNinos, { viewModel.hayNinos = it },
                                viewModel.estanDeAcuerdo, { viewModel.estanDeAcuerdo = it }
                            )
                            5 -> AdopcionStep5(
                                viewModel.tieneOtrasMascotas, { viewModel.tieneOtrasMascotas = it },
                                viewModel.experienciaPrevia, { viewModel.experienciaPrevia = it },
                                viewModel.tiempoDiario, { viewModel.tiempoDiario = it },
                                viewModel.presupuestoVeterinario, { viewModel.presupuestoVeterinario = it }
                            )
                        }
                    }
                }
            }

            // ==========================
            // BOTONES DE NAVEGACIÓN
            // ==========================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viewModel.currentPage > 1) {
                    OutlinedButton(
                        onClick = { viewModel.previousStep() },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color(0xFF1A237E))
                    ) {
                        Icon(Icons.Default.ArrowBack, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Anterior", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        if (viewModel.currentPage < viewModel.totalPages) {
                            viewModel.nextStep()
                        } else {
                            viewModel.guardarSolicitud {
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = viewModel.isStepValid(viewModel.currentPage),
                    modifier = Modifier.weight(if (viewModel.currentPage > 1) 2f else 1f).height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                ) {
                    Text(
                        if (viewModel.currentPage == viewModel.totalPages) "Finalizar" else "Siguiente",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        if (viewModel.currentPage == viewModel.totalPages) Icons.Default.Check else Icons.Default.ArrowForward,
                        null
                    )
                }
            }
        }
    }
}

// ==========================
// HEADER ACTUALIZADO CON DOTS
// ==========================

@Composable
fun AdoptionFormHeader(
    currentPage: Int,
    totalPages: Int,
    step: FormStep
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF3949AB)))
            )
            .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 48.dp)
    ) {
        Text(
            text = "Nueva Solicitud",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(16.dp))

        // Indicador de Paginación (Dots)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 1..totalPages) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (i <= currentPage) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Icon(
                    step.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    step.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    step.subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Reutilizamos los pasos previos (AdopcionStep1...5) y componentes (FormSectionTitle, ModernAdminField, ModernAdminSwitch) del archivo original...
// [Mantenemos las funciones AdopcionStepX y componentes auxiliares que ya tenías]

@Composable
fun AdopcionStep1(nombre: String, onNombre: (String) -> Unit, dni: String, onDni: (String) -> Unit, edad: String, onEdad: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        FormSectionTitle(Icons.Default.Person, "Datos del solicitante", "Información personal")
        ModernAdminField(nombre, onNombre, "Nombre completo", Icons.Default.Badge)
        ModernAdminField(dni, onDni, "DNI / Cédula", Icons.Default.AssignmentInd)
        ModernAdminField(edad, onEdad, "Edad", Icons.Default.Cake)
    }
}

@Composable
fun AdopcionStep2(ocupacion: String, onOcupacion: (String) -> Unit, telefono: String, onTelefono: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        FormSectionTitle(Icons.Default.Work, "Contacto", "Información de comunicación")
        ModernAdminField(ocupacion, onOcupacion, "Ocupación", Icons.Default.BusinessCenter)
        ModernAdminField(telefono, onTelefono, "Teléfono", Icons.Default.Phone)
    }
}

@Composable
fun AdopcionStep3(tipo: String, onTipo: (String) -> Unit, patio: Boolean, onPatio: (Boolean) -> Unit, prot: Boolean, onProt: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        FormSectionTitle(Icons.Default.Home, "Vivienda", "Condiciones del hogar")
        Text("Tipo de vivienda", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Casa", "Apartamento", "Finca").forEach {
                FilterChip(
                    selected = tipo == it,
                    onClick = { onTipo(it) },
                    label = { Text(it) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF1A237E), selectedLabelColor = Color.White)
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        ModernAdminSwitch("¿Tiene patio o balcón?", patio, onPatio)
        ModernAdminSwitch("¿Tiene protecciones?", prot, onProt)
    }
}

@Composable
fun AdopcionStep4(integrantes: String, onIntegrantes: (String) -> Unit, ninos: Boolean, onNinos: (Boolean) -> Unit, acuerdo: Boolean, onAcuerdo: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        FormSectionTitle(Icons.Default.Groups, "Familia", "Información del hogar")
        ModernAdminField(integrantes, onIntegrantes, "Número de integrantes", Icons.Default.FormatListNumbered)
        ModernAdminSwitch("¿Hay niños en casa?", ninos, onNinos)
        ModernAdminSwitch("¿Todos están de acuerdo?", acuerdo, onAcuerdo)
    }
}

@Composable
fun AdopcionStep5(otras: Boolean, onOtras: (Boolean) -> Unit, exp: String, onExp: (String) -> Unit, tiempo: String, onTiempo: (String) -> Unit, presupuesto: Boolean, onPresupuesto: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        FormSectionTitle(Icons.Default.Favorite, "Experiencia", "Compromiso con la mascota")
        ModernAdminSwitch("¿Tiene otras mascotas?", otras, onOtras)
        ModernAdminField(tiempo, onTiempo, "Horas diarias disponibles", Icons.Default.Timer)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = exp, onValueChange = onExp,
            modifier = Modifier.fillMaxWidth().height(120.dp),
            label = { Text("Experiencia previa") },
            placeholder = { Text("Describe tu experiencia...") },
            shape = RoundedCornerShape(18.dp)
        )
        Spacer(Modifier.height(20.dp))
        ModernAdminSwitch("¿Tiene presupuesto veterinario?", presupuesto, onPresupuesto)
    }
}

@Composable
fun FormSectionTitle(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 28.dp)) {
        Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFF1A237E).copy(alpha = 0.1f)) {
            Icon(icon, null, tint = Color(0xFF1A237E), modifier = Modifier.padding(14.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(subtitle, color = Color.Gray)
        }
    }
}

@Composable
fun ModernAdminField(value: String, onValue: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value, onValueChange = onValue,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF1A237E)) },
        shape = RoundedCornerShape(18.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1A237E))
    )
}

@Composable
fun ModernAdminSwitch(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8F9FC),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontWeight = FontWeight.Medium)
            Switch(
                checked = checked, onCheckedChange = onChecked,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF1A237E))
            )
        }
    }
}
