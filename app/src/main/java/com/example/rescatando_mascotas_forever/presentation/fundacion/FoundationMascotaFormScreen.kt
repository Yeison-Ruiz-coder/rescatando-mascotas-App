package com.example.rescatando_mascotas_forever.presentation.fundacion

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.toSafeInt
import com.example.rescatando_mascotas_forever.data.network.models.toSafeString
import com.example.rescatando_mascotas_forever.utils.Constants

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoundationMascotaFormScreen(
    navController: NavHostController,
    mascotaId: Int? = null,
    rescateId: Int? = null,
    viewModel: FoundationMascotaFormViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val mascotaEdit by viewModel.mascota.collectAsState()

    var currentStep by remember { mutableIntStateOf(1) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var hogarRecomendado by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }

    val generos = listOf("Macho", "Hembra", "Desconocido")
    var genero by remember { mutableStateOf(generos[0]) }

    val tamanos = listOf("pequeño", "mediano", "grande", "muy_grande")
    var tamano by remember { mutableStateOf(tamanos[1]) }

    val estados = listOf("En adopcion", "Adoptado", "Rescatada", "En acogida")
    var estado by remember { mutableStateOf(estados[0]) }

    var condicionesEspeciales by remember { mutableStateOf("") }
    var saludGeneral by remember { mutableStateOf("") }
    var enfermedadesCronicas by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var requisitosAdopcion by remember { mutableStateOf("") }
    var destacada by remember { mutableStateOf(false) }

    var esterilizado by remember { mutableStateOf(false) }
    var desparasitado by remember { mutableStateOf(false) }
    var vacunado by remember { mutableStateOf(false) }
    var aptoNinos by remember { mutableStateOf(true) }
    var aptoAnimales by remember { mutableStateOf(true) }
    var hogarTemporal by remember { mutableStateOf(false) }

    var fotoPrincipalUri by remember { mutableStateOf<Uri?>(null) }
    var galeriaUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val fotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { fotoPrincipalUri = it }
    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { galeriaUris = it }

    LaunchedEffect(mascotaId) { mascotaId?.let { viewModel.loadMascota(it) } }

    LaunchedEffect(mascotaEdit) {
        mascotaEdit?.let {
            nombre = it.nombre
            especie = it.especie ?: "Perro"
            edad = it.edadAprox.toSafeInt().toString()
            peso = it.pesoAprox.toSafeString()
            genero = if (it.genero in generos) it.genero!! else generos[0]
            tamano = if (it.tamano in tamanos) it.tamano!! else tamanos[1]
            estado = if (it.estado in estados) it.estado!! else estados[0]
            color = it.color ?: ""
            ubicacion = it.ubicacion ?: ""
            descripcion = it.descripcion ?: ""
            hogarRecomendado = it.hogarRecomendado.toSafeString()
            videoUrl = it.videoUrl ?: ""
            condicionesEspeciales = it.condicionesEspeciales.toSafeString()
            saludGeneral = it.saludGeneral.toSafeString()
            enfermedadesCronicas = it.enfermedadesCronicas.toSafeString()
            medicamentos = it.medicamentos.toSafeString()
            requisitosAdopcion = it.requisitosAdopcion.toSafeString()
            destacada = it.isDestacada()
            esterilizado = it.isEsterilizado()
            desparasitado = it.isDesparasitado()
            vacunado = it.isVacunado()
            aptoNinos = it.isAptoNinos()
            aptoAnimales = it.isAptoOtrosAnimales()
            hogarTemporal = it.isHogarTemporal()
        }
    }

    LaunchedEffect(state) {
        if (state is FormState.Success) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(if (mascotaId == null) "Registrar Mascota" else "Editar Mascota", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Paso $currentStep de 4", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { if (currentStep > 1) currentStep-- else navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    if (mascotaId != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LinearProgressIndicator(
                progress = { currentStep / 4f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                        }
                    }, label = ""
                ) { step ->
                    when (step) {
                        1 -> StepBasics(
                            nombre, { nombre = it }, especie, { especie = it }, edad, { edad = it },
                            peso, { peso = it }, color, { color = it }, genero, { genero = it }, generos,
                            tamano, { tamano = it }, tamanos, destacada, { destacada = it }
                        )
                        2 -> StepMedia(
                            fotoPrincipalUri, { fotoLauncher.launch("image/*") },
                            mascotaEdit?.fotoPrincipal, galeriaUris, { galeriaLauncher.launch("image/*") },
                            videoUrl, { videoUrl = it }
                        )
                        3 -> StepHealth(
                            esterilizado, { esterilizado = it }, desparasitado, { desparasitado = it },
                            vacunado, { vacunado = it }, saludGeneral, { saludGeneral = it },
                            enfermedadesCronicas, { enfermedadesCronicas = it }, medicamentos, { medicamentos = it },
                            condicionesEspeciales, { condicionesEspeciales = it }
                        )
                        4 -> StepAdoption(
                            aptoNinos, { aptoNinos = it }, aptoAnimales, { aptoAnimales = it },
                            hogarTemporal, { hogarTemporal = it }, descripcion, { descripcion = it },
                            ubicacion, { ubicacion = it }, hogarRecomendado, { hogarRecomendado = it },
                            requisitosAdopcion, { requisitosAdopcion = it }, estado, { estado = it }, estados
                        )
                    }
                }
            }

            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ANTERIOR")
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < 4) {
                                currentStep++
                            } else {
                                val fields = mutableMapOf(
                                    "nombre_mascota" to nombre,
                                    "especie" to especie,
                                    "genero" to genero,
                                    "tamano" to tamano,
                                    "estado" to estado,
                                    "destacada" to if (destacada) "1" else "0",
                                    "esterilizado" to if (esterilizado) "1" else "0",
                                    "desparasitado" to if (desparasitado) "1" else "0",
                                    "vacunado" to if (vacunado) "1" else "0",
                                    "apto_con_ninos" to if (aptoNinos) "1" else "0",
                                    "apto_con_otros_animales" to if (aptoAnimales) "1" else "0",
                                    "necesita_hogar_temporal" to if (hogarTemporal) "1" else "0"
                                )
                                if (edad.isNotEmpty()) fields["edad_aprox"] = edad
                                if (peso.isNotEmpty()) fields["peso_aprox"] = peso
                                if (color.isNotEmpty()) fields["color"] = color
                                if (ubicacion.isNotEmpty()) fields["lugar_rescate"] = ubicacion
                                if (descripcion.isNotEmpty()) fields["descripcion"] = descripcion
                                if (hogarRecomendado.isNotEmpty()) fields["hogar_recomendado"] = hogarRecomendado
                                if (saludGeneral.isNotEmpty()) fields["salud_general"] = saludGeneral
                                if (condicionesEspeciales.isNotEmpty()) fields["condiciones_especiales"] = condicionesEspeciales
                                if (videoUrl.isNotEmpty()) fields["video_url"] = videoUrl
                                if (requisitosAdopcion.isNotEmpty()) fields["requisitos_adopcion"] = requisitosAdopcion

                                viewModel.saveMascota(context, mascotaId, rescateId, fields, emptyMap(), fotoPrincipalUri, galeriaUris)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        enabled = nombre.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (state is FormState.Loading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                        } else {
                            Text(if (currentStep < 4) "SIGUIENTE" else "GUARDAR")
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar a $nombre?") },
            text = { Text("Esta acción borrará permanentemente a la mascota de la base de datos y no se podrá recuperar.") },
            confirmButton = {
                Button(
                    onClick = {
                        mascotaId?.let { viewModel.deleteMascota(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("SÍ, ELIMINAR", color = MaterialTheme.colorScheme.onError) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("CANCELAR") }
            }
        )
    }
}

@Composable
fun StepBasics(
    nombre: String, onNombre: (String) -> Unit,
    especie: String, onEspecie: (String) -> Unit,
    edad: String, onEdad: (String) -> Unit,
    peso: String, onPeso: (String) -> Unit,
    color: String, onColor: (String) -> Unit,
    genero: String, onGenero: (String) -> Unit, generos: List<String>,
    tamano: String, onTamano: (String) -> Unit, tamanos: List<String>,
    destacada: Boolean, onDestacada: (Boolean) -> Unit
) {
    FormCard("Información Básica") {
        AppTextField(value = nombre, onValueChange = onNombre, label = "Nombre de la mascota *")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppTextField(value = especie, onValueChange = onEspecie, label = "Especie", modifier = Modifier.weight(1f))
            AppTextField(value = color, onValueChange = onColor, label = "Color", modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppTextField(value = edad, onValueChange = onEdad, label = "Edad Aprox.", modifier = Modifier.weight(1f))
            AppTextField(value = peso, onValueChange = onPeso, label = "Peso (kg)", modifier = Modifier.weight(1f))
        }
        AppDropdown(label = "Género", options = generos, selected = genero, onSelect = onGenero)
        AppDropdown(label = "Tamaño", options = tamanos, selected = tamano, onSelect = onTamano)
        CheckboxLabel("Mascota Destacada", destacada, onDestacada)
    }
}

@Composable
fun StepMedia(
    fotoUri: Uri?, onPickPick: () -> Unit,
    fotoActual: String?,
    galeria: List<Uri>, onPickGaleria: () -> Unit,
    video: String, onVideo: (String) -> Unit
) {
    FormCard("Fotografías y Videos") {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onPickPick() },
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(fotoUri ?: Constants.getImageUrl(fotoActual))
                Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                if (fotoUri == null && fotoActual == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, null, tint = MaterialTheme.colorScheme.primary)
                        Text("Foto Principal", fontSize = 10.sp)
                    }
                }
            }
            Button(
                onClick = onPickGaleria,
                modifier = Modifier.height(120.dp).weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Collections, null)
                    Text("Galería (${galeria.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        AppTextField(value = video, onValueChange = onVideo, label = "URL de Video (YouTube)")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StepHealth(
    esterilizado: Boolean, onEst: (Boolean) -> Unit,
    desparasitado: Boolean, onDesp: (Boolean) -> Unit,
    vacunado: Boolean, onVac: (Boolean) -> Unit,
    salud: String, onSalud: (String) -> Unit,
    enfermedades: String, onEnf: (String) -> Unit,
    medicamentos: String, onMed: (String) -> Unit,
    condiciones: String, onCond: (String) -> Unit
) {
    FormCard("Salud y Estado") {
        FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppFilterChip(selected = esterilizado, onClick = { onEst(!esterilizado) }, label = "Esterilizado")
            AppFilterChip(selected = desparasitado, onClick = { onDesp(!desparasitado) }, label = "Desparasitado")
            AppFilterChip(selected = vacunado, onClick = { onVac(!vacunado) }, label = "Vacunado")
        }
        AppTextField(value = salud, onValueChange = onSalud, label = "Salud General", minLines = 2)
        AppTextField(value = enfermedades, onValueChange = onEnf, label = "Enfermedades crónicas", minLines = 2)
        AppTextField(value = medicamentos, onValueChange = onMed, label = "Medicamentos actuales", minLines = 2)
        AppTextField(value = condiciones, onValueChange = onCond, label = "Condiciones especiales", minLines = 2)
    }
}

@Composable
fun StepAdoption(
    aptoNinos: Boolean, onNinos: (Boolean) -> Unit,
    aptoAnimales: Boolean, onAnimales: (Boolean) -> Unit,
    hogarTemporal: Boolean, onTemp: (Boolean) -> Unit,
    descripcion: String, onDesc: (String) -> Unit,
    ubicacion: String, onUbi: (String) -> Unit,
    hogarRec: String, onHogar: (String) -> Unit,
    requisitos: String, onReq: (String) -> Unit,
    estado: String, onEstado: (String) -> Unit, estados: List<String>
) {
    FormCard("Comportamiento y Adopción") {
        Row(Modifier.fillMaxWidth()) {
            CheckboxLabel("Apto Niños", aptoNinos, onNinos)
            CheckboxLabel("Apto Animales", aptoAnimales, onAnimales)
        }
        CheckboxLabel("Necesita Hogar Temporal", hogarTemporal, onTemp)
        AppDropdown(label = "Estado Actual", options = estados, selected = estado, onSelect = onEstado)
        AppTextField(value = ubicacion, onValueChange = onUbi, label = "Ubicación / Lugar de Rescate")
        AppTextField(value = descripcion, onValueChange = onDesc, label = "Historia de la mascota", minLines = 3)
        AppTextField(value = hogarRec, onValueChange = onHogar, label = "Tipo de hogar recomendado")
        AppTextField(value = requisitos, onValueChange = onReq, label = "Requisitos de adopción", minLines = 2)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdown(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected, onValueChange = {}, readOnly = true, label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onSelect(option); expanded = false })
            }
        }
    }
}

@Composable
fun AppTextField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier, minLines: Int = 1) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        minLines = minLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppFilterChip(selected: Boolean, onClick: () -> Unit, label: String) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            selectedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun FormCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
            content()
        }
    }
}

@Composable
fun CheckboxLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 12.dp)) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
        )
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
