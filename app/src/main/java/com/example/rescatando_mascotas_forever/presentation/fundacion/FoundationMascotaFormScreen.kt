package com.example.rescatando_mascotas_forever.presentation.fundacion

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.data.network.models.toSafeString
import com.example.rescatando_mascotas_forever.ui.theme.FoundationGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationMascotaFormScreen(
    navController: NavHostController,
    mascotaId: Int? = null,
    viewModel: FoundationMascotaFormViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val mascotaEdit by viewModel.mascota.collectAsState()

    // Form states
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var tamano by remember { mutableStateOf("mediano") }
    var genero by remember { mutableStateOf("Macho") }
    var color by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("En adopcion") }
    var ubicacion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var condiciones by remember { mutableStateOf("") }
    var salud by remember { mutableStateOf("") }
    var enfermedades by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var requisitos by remember { mutableStateOf("") }
    var hogarRecomendado by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    
    // Boolean states
    var esterilizado by remember { mutableStateOf(false) }
    var desparasitado by remember { mutableStateOf(false) }
    var vacunado by remember { mutableStateOf(false) }
    var aptoNinos by remember { mutableStateOf(true) }
    var aptoAnimales by remember { mutableStateOf(true) }
    var hogarTemporal by remember { mutableStateOf(false) }
    var destacada by remember { mutableStateOf(false) }
    
    var fotoPrincipalUri by remember { mutableStateOf<Uri?>(null) }
    var galeriaUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val fotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fotoPrincipalUri = uri
    }
    
    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        galeriaUris = uris
    }

    LaunchedEffect(mascotaId) {
        if (mascotaId != null) {
            viewModel.loadMascota(mascotaId)
        }
    }

    LaunchedEffect(mascotaEdit) {
        mascotaEdit?.let {
            nombre = it.nombre
            especie = it.especie ?: "Perro"
            edad = it.edadAprox?.toString() ?: ""
            peso = it.pesoAprox?.toString() ?: ""
            tamano = it.tamano ?: "mediano"
            genero = it.genero ?: "Macho"
            color = it.color ?: ""
            estado = it.estado ?: "En adopcion"
            ubicacion = it.ubicacion ?: ""
            descripcion = it.descripcion ?: ""
            condiciones = it.condicionesEspeciales.toSafeString()
            salud = it.saludGeneral.toSafeString()
            enfermedades = it.enfermedadesCronicas.toSafeString()
            medicamentos = it.medicamentos.toSafeString()
            requisitos = it.requisitosAdopcion.toSafeString()
            hogarRecomendado = it.hogarRecomendado.toSafeString()
            videoUrl = it.videoUrl ?: ""
            
            esterilizado = it.esterilizado ?: false
            desparasitado = it.desparasitado ?: false
            vacunado = it.vacunado ?: false
            aptoNinos = it.aptoConNinos ?: true
            aptoAnimales = it.aptoConOtrosAnimales ?: true
            hogarTemporal = it.necesitaHogarTemporal ?: false
            destacada = it.destacada ?: false
        }
    }

    LaunchedEffect(state) {
        if (state is FormState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (mascotaId == null) "Nueva Mascota" else "Editar Mascota", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(FoundationGradient)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección: Información Básica
            FormSectionTitle("Información Básica")
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la mascota") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Pets, null) }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = especie,
                    onValueChange = { especie = it },
                    label = { Text("Especie") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad aprox.") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ej: 2") }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                var expandedTamano by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = tamano,
                        onValueChange = {},
                        label = { Text("Tamaño") },
                        readOnly = true,
                        trailingIcon = { IconButton(onClick = { expandedTamano = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expandedTamano, onDismissRequest = { expandedTamano = false }) {
                        listOf("pequeño", "mediano", "grande", "muy_grande").forEach { t ->
                            DropdownMenuItem(text = { Text(t) }, onClick = { tamano = t; expandedTamano = false })
                        }
                    }
                }

                var expandedGenero by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        label = { Text("Género") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { IconButton(onClick = { expandedGenero = true }) { Icon(Icons.Default.ArrowDropDown, null) } }
                    )
                    DropdownMenu(expanded = expandedGenero, onDismissRequest = { expandedGenero = false }) {
                        listOf("Macho", "Hembra", "Desconocido").forEach { g ->
                            DropdownMenuItem(text = { Text(g) }, onClick = { genero = g; expandedGenero = false })
                        }
                    }
                }
            }

            var expandedEstado by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    label = { Text("Estado") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { IconButton(onClick = { expandedEstado = true }) { Icon(Icons.Default.ArrowDropDown, null) } }
                )
                DropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                    listOf("Adoptado", "En adopcion", "Rescatada", "En acogida").forEach { e ->
                        DropdownMenuItem(text = { Text(e) }, onClick = { estado = e; expandedEstado = false })
                    }
                }
            }

            // Sección: Salud y Detalles
            FormSectionTitle("Salud y Características")
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CheckboxLabel("Esterilizado", esterilizado) { esterilizado = it }
                CheckboxLabel("Desparasitado", desparasitado) { desparasitado = it }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CheckboxLabel("Vacunado", vacunado) { vacunado = it }
                CheckboxLabel("Apto niños", aptoNinos) { aptoNinos = it }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CheckboxLabel("Apto animales", aptoAnimales) { aptoAnimales = it }
                CheckboxLabel("Hogar Temporal", hogarTemporal) { hogarTemporal = it }
            }
            CheckboxLabel("Mascota Destacada", destacada) { destacada = it }

            OutlinedTextField(
                value = salud,
                onValueChange = { salud = it },
                label = { Text("Salud General") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = enfermedades,
                onValueChange = { enfermedades = it },
                label = { Text("Enfermedades Crónicas") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = medicamentos,
                onValueChange = { medicamentos = it },
                label = { Text("Medicamentos") },
                modifier = Modifier.fillMaxWidth()
            )

            // Sección: Comportamiento y Requisitos
            FormSectionTitle("Comportamiento y Requisitos")
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción / Historia") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = condiciones,
                onValueChange = { condiciones = it },
                label = { Text("Condiciones Especiales") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = requisitos,
                onValueChange = { requisitos = it },
                label = { Text("Requisitos de Adopción") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = hogarRecomendado,
                onValueChange = { hogarRecomendado = it },
                label = { Text("Hogar Recomendado") },
                modifier = Modifier.fillMaxWidth()
            )

            // Sección: Ubicación y Multimedia
            FormSectionTitle("Ubicación y Multimedia")
            
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Lugar de rescate / Ciudad") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, null) }
            )

            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("URL de Video (YouTube/Vimeo)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.VideoLibrary, null) }
            )

            // Fotos
            Button(
                onClick = { fotoLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Icon(Icons.Default.PhotoCamera, null)
                Spacer(Modifier.width(8.dp))
                Text(if (fotoPrincipalUri != null) "Foto Principal Seleccionada" else "Seleccionar Foto Principal")
            }

            Button(
                onClick = { galeriaLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Icon(Icons.Default.Collections, null)
                Spacer(Modifier.width(8.dp))
                Text("Galería (${galeriaUris.size} fotos)")
            }

            Spacer(Modifier.height(24.dp))

            // Botón Guardar
            Button(
                onClick = {
                    viewModel.saveMascota(
                        context = context,
                        id = mascotaId,
                        nombre = nombre,
                        especie = especie,
                        edad = edad,
                        peso = peso,
                        tamano = tamano,
                        genero = genero,
                        color = color,
                        estado = estado,
                        ubicacion = ubicacion,
                        descripcion = descripcion,
                        condiciones = condiciones,
                        salud = salud,
                        enfermedades = enfermedades,
                        medicamentos = medicamentos,
                        requisitos = requisitos,
                        hogarRecomendado = hogarRecomendado,
                        videoUrl = videoUrl,
                        esterilizado = esterilizado,
                        desparasitado = desparasitado,
                        vacunado = vacunado,
                        aptoNinos = aptoNinos,
                        aptoAnimales = aptoAnimales,
                        hogarTemporal = hogarTemporal,
                        destacada = destacada,
                        fotoPrincipalUri = fotoPrincipalUri,
                        galeriaUris = galeriaUris
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                enabled = state !is FormState.Loading && nombre.isNotBlank()
            ) {
                if (state is FormState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("GUARDAR MASCOTA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            if (state is FormState.Error) {
                Text((state as FormState.Error).message, color = Color.Red, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun FormSectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun CheckboxLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, fontSize = 13.sp)
    }
}
