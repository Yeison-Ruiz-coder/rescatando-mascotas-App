package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAdopcionScreen(
    navController: NavHostController,
    mascotaId: Int = -1,
    viewModel: FormularioAdopcionViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = MaterialTheme.colorScheme.background
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
                        .padding(horizontal = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.95f) 
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        ) {
                            // Área de contenido scrollable
                            Box(modifier = Modifier.weight(1f)) {
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
                                        1 -> StepDatosPersonales(viewModel)
                                        2 -> StepInformacionVivienda(viewModel)
                                        3 -> StepCompromisos(viewModel)
                                        4 -> StepRevision(viewModel)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botones de Navegación
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (viewModel.currentPage > 1) {
                                    OutlinedButton(
                                        onClick = { viewModel.previousStep() },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, WebPrimary.copy(alpha = 0.3f))
                                    ) {
                                        Text("Atrás", fontWeight = FontWeight.Bold, color = WebPrimary)
                                    }
                                }

                                val isLastStep = viewModel.currentPage == viewModel.totalPages
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
                                    modifier = Modifier.weight(2f).height(50.dp),
                                    enabled = !viewModel.isSaving,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isLastStep) WebAccent else WebPrimary
                                    )
                                ) {
                                    if (viewModel.isSaving) {
                                        CircularProgressIndicator(color = StaticWhite, modifier = Modifier.size(20.dp))
                                    } else {
                                        Text(
                                            if (isLastStep) "ENVIAR SOLICITUD" else "SIGUIENTE",
                                            fontWeight = FontWeight.Bold,
                                            color = StaticWhite
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Icon(
                                            if (isLastStep) Icons.Default.Send else Icons.Default.ArrowForward, 
                                            null, 
                                            modifier = Modifier.size(18.dp),
                                            tint = StaticWhite
                                        )
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
            .padding(24.dp)
    ) {
        Text(
            "Solicitud de Adopción",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "PASO $currentPage DE $totalPages",
                color = WebPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                "${(progress * 100).toInt()}%",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = WebPrimary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun StepDatosPersonales(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Person, "Datos Personales", "Completa tu información de contacto")
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ModernInputField(value = viewModel.nombre, onValueChange = { viewModel.nombre = it }, label = "Nombre", modifier = Modifier.weight(1f))
            ModernInputField(value = viewModel.apellido, onValueChange = { viewModel.apellido = it }, label = "Apellido", modifier = Modifier.weight(1f))
        }
        
        ModernInputField(value = viewModel.documentoIdentidad, onValueChange = { viewModel.documentoIdentidad = it }, label = "Documento de Identidad", icon = Icons.Default.Badge)
        ModernInputField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = "Correo Electrónico", icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
        ModernInputField(value = viewModel.telefono, onValueChange = { viewModel.telefono = it }, label = "Teléfono", icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
        ModernInputField(value = viewModel.fechaNacimiento, onValueChange = { viewModel.fechaNacimiento = it }, label = "Fecha de Nacimiento", icon = Icons.Default.CalendarToday)
        ModernInputField(value = viewModel.ocupacion, onValueChange = { viewModel.ocupacion = it }, label = "Ocupación", icon = Icons.Default.Work)
    }
}

@Composable
fun StepInformacionVivienda(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Home, "Información de Vivienda", "¿Dónde vivirá tu nueva mascota?")
        
        ModernInputField(value = viewModel.direccion, onValueChange = { viewModel.direccion = it }, label = "Dirección", icon = Icons.Default.LocationOn)
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ModernInputField(value = viewModel.ciudad, onValueChange = { viewModel.ciudad = it }, label = "Ciudad", modifier = Modifier.weight(1f))
            ModernInputField(value = viewModel.departamento, onValueChange = { viewModel.departamento = it }, label = "Depto.", modifier = Modifier.weight(1f))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ModernInputField(value = viewModel.codigoPostal, onValueChange = { viewModel.codigoPostal = it }, label = "Cód. Postal", modifier = Modifier.weight(1f), keyboardType = KeyboardType.Number)
            ModernInputField(value = viewModel.cantidadHijos, onValueChange = { viewModel.cantidadHijos = it }, label = "Cant. Hijos", modifier = Modifier.weight(1f), keyboardType = KeyboardType.Number)
        }

        Text("Tipo de Vivienda", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val opciones = listOf("Casa", "Apto", "Finca")
            opciones.forEach { opcion ->
                SelectableChip(
                    text = opcion,
                    selected = viewModel.tipoVivienda == opcion,
                    onClick = { viewModel.tipoVivienda = opcion },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("¿Es Propietario?", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val opciones = listOf("Sí", "No", "Familiar")
            opciones.forEach { opcion ->
                SelectableChip(
                    text = opcion,
                    selected = viewModel.esPropietario == opcion,
                    onClick = { viewModel.esPropietario = opcion },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StepCompromisos(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.Favorite, "Compromisos", "Tu promesa con tu nuevo amigo")
        
        Text("Experiencia con mascotas", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = viewModel.experienciaMascotas,
            onValueChange = { viewModel.experienciaMascotas = it },
            placeholder = { Text("Cuéntanos...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)) },
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                focusedBorderColor = WebPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )
        )

        Text("Motivo de adopción", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = viewModel.motivoAdopcion,
            onValueChange = { viewModel.motivoAdopcion = it },
            placeholder = { Text("¿Por qué deseas adoptar?", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)) },
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                focusedBorderColor = WebPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )
        )

        Spacer(Modifier.height(16.dp))
        
        CompromisoCheck(
            checked = viewModel.compromisoCuidado,
            onCheckedChange = { viewModel.compromisoCuidado = it },
            text = "Cuidado responsable y bienestar"
        )
        CompromisoCheck(
            checked = viewModel.compromisoEsterilizacion,
            onCheckedChange = { viewModel.compromisoEsterilizacion = it },
            text = "Esterilización obligatoria"
        )
        CompromisoCheck(
            checked = viewModel.compromisoSeguimiento,
            onCheckedChange = { viewModel.compromisoSeguimiento = it },
            text = "Permitir seguimiento post-adopción"
        )
    }
}

@Composable
fun StepRevision(viewModel: FormularioAdopcionViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StepTitle(Icons.Default.FactCheck, "Revisión", "Verifica que todo esté correcto")
        
        RevisionSection("Datos Personales") {
            RevisionItem("Nombre", "${viewModel.nombre} ${viewModel.apellido}")
            RevisionItem("Documento", viewModel.documentoIdentidad)
            RevisionItem("Email", viewModel.email)
            RevisionItem("Teléfono", viewModel.telefono)
        }

        RevisionSection("Vivienda") {
            RevisionItem("Dirección", viewModel.direccion)
            RevisionItem("Ciudad", "${viewModel.ciudad}, ${viewModel.departamento}")
            RevisionItem("Tipo", viewModel.tipoVivienda)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = WebPrimary.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, WebPrimary.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, tint = WebPrimary)
                Spacer(Modifier.width(12.dp))
                Text(
                    "Al enviar esta solicitud, confirmas que toda la información es verídica.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun StepTitle(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = WebPrimary.copy(alpha = 0.1f),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(icon, null, tint = WebPrimary, modifier = Modifier.padding(10.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun ModernInputField(
    value: String, 
    onValueChange: (String) -> Unit, 
    label: String, 
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        leadingIcon = icon?.let { { Icon(it, null, tint = WebPrimary, modifier = Modifier.size(18.dp)) } },
        modifier = modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = WebPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            focusedLabelColor = WebPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) WebPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) WebPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 12.dp),
            color = if (selected) StaticWhite else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CompromisoCheck(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = WebPrimary)
        )
        Text(text, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), fontSize = 13.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun RevisionSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Text(title, color = WebPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun RevisionItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 12.sp)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
