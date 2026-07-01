package com.example.rescatando_mascotas_forever.presentation.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.data.network.models.toSafeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMascotaFormScreen(
    navController: NavHostController,
    mascotaId: Int? = null,
    fundacionId: Int? = null,
    viewModel: AdminMascotaFormViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val mascotaEdit by viewModel.mascota.collectAsState()

    // --- ESTADOS DEL FORMULARIO ---
    var fundacionIdState by remember { mutableStateOf(fundacionId?.toString() ?: "1") }
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

    // Booleanos
    var esterilizado by remember { mutableStateOf(false) }
    var desparasitado by remember { mutableStateOf(false) }
    var vacunado by remember { mutableStateOf(false) }
    var aptoNinos by remember { mutableStateOf(true) }
    var aptoAnimales by remember { mutableStateOf(true) }
    var hogarTemporal by remember { mutableStateOf(false) }
    var destacada by remember { mutableStateOf(false) }

    var fotoPrincipalUri by remember { mutableStateOf<Uri?>(null) }

    val fotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fotoPrincipalUri = uri
    }

    LaunchedEffect(mascotaId) {
        if (mascotaId != null) viewModel.loadMascota(mascotaId)
    }

    LaunchedEffect(mascotaEdit) {
        mascotaEdit?.let {
            fundacionIdState = it.fundacionId?.toString() ?: "1"
            nombre = it.nombre
            especie = it.especie ?: "Perro"
            edad = it.edadAprox.toSafeString()
            peso = it.pesoAprox.toSafeString()
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
            
            // CORRECCIÓN: Usar métodos helper para asignar Booleanos reales
            esterilizado = it.isEsterilizado()
            desparasitado = it.isDesparasitado()
            vacunado = it.isVacunado()
            aptoNinos = it.isAptoNinos()
            aptoAnimales = it.isAptoOtrosAnimales()
            hogarTemporal = it.isHogarTemporal()
            destacada = it.isDestacada()
        }
    }

    LaunchedEffect(state) {
        if (state is AdminFormState.Success) {
            Toast.makeText(context, "Mascota guardada con éxito", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (mascotaId == null) "Nueva Mascota" else "Editar Mascota", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF673AB7))
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
            SectionTitle("Configuración")
            OutlinedTextField(
                value = fundacionIdState,
                onValueChange = { fundacionIdState = it },
                label = { Text("ID de la Fundación (Obligatorio)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Ej: 1") }
            )

            SectionTitle("Información Básica")

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre de la Mascota") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Especie Dropdown
                var expEsp by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = especie,
                        onValueChange = {},
                        label = { Text("Especie") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expEsp = true }) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expEsp, onDismissRequest = { expEsp = false }) {
                        listOf("Perro", "Gato", "Ave", "Conejo", "Hamster", "Otro").forEach { s ->
                            DropdownMenuItem(text = { Text(s) }, onClick = { especie = s; expEsp = false })
                        }
                    }
                }

                // Género Dropdown
                var expGen by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        label = { Text("Género") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expGen = true }) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expGen, onDismissRequest = { expGen = false }) {
                        listOf("Macho", "Hembra", "Desconocido").forEach { s ->
                            DropdownMenuItem(text = { Text(s) }, onClick = { genero = s; expGen = false })
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad Aprox.") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso (kg)") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Tamaño Dropdown
                var expTam by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = tamano,
                        onValueChange = {},
                        label = { Text("Tamaño") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expTam = true }) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expTam, onDismissRequest = { expTam = false }) {
                        listOf("pequeño", "mediano", "grande", "muy_grande").forEach { s ->
                            DropdownMenuItem(text = { Text(s) }, onClick = { tamano = s; expTam = false })
                        }
                    }
                }
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.weight(1f))
            }

            OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())

            // Estado Dropdown
            var expEst by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    label = { Text("Estado") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expEst = true }) }
                )
                DropdownMenu(expanded = expEst, onDismissRequest = { expEst = false }) {
                    listOf("En adopcion", "Adoptado", "Rescatada", "En acogida", "Urgente").forEach { s ->
                        DropdownMenuItem(text = { Text(s) }, onClick = { estado = s; expEst = false })
                    }
                }
            }

            SectionTitle("Salud y Cuidados")
            OutlinedTextField(value = salud, onValueChange = { salud = it }, label = { Text("Salud General") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = enfermedades, onValueChange = { enfermedades = it }, label = { Text("Enfermedades (Separa por comas)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = medicamentos, onValueChange = { medicamentos = it }, label = { Text("Medicamentos (Separa por comas)") }, modifier = Modifier.fillMaxWidth())

            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    LabeledCheckbox("Esterilizado", esterilizado) { esterilizado = it }
                    LabeledCheckbox("Desparasitado", desparasitado) { desparasitado = it }
                }
                Column(Modifier.weight(1f)) {
                    LabeledCheckbox("Vacunado", vacunado) { vacunado = it }
                    LabeledCheckbox("Mascota Destacada", destacada) { destacada = it }
                }
            }

            SectionTitle("Comportamiento y Requisitos")
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción / Historia") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            OutlinedTextField(value = requisitos, onValueChange = { requisitos = it }, label = { Text("Requisitos (Separa por comas)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = hogarRecomendado, onValueChange = { hogarRecomendado = it }, label = { Text("Hogar Recomendado") }, modifier = Modifier.fillMaxWidth())

            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    LabeledCheckbox("Apto con niños", aptoNinos) { aptoNinos = it }
                    LabeledCheckbox("Apto con animales", aptoAnimales) { aptoAnimales = it }
                }
                Column(Modifier.weight(1f)) {
                    LabeledCheckbox("Hogar Temporal", hogarTemporal) { hogarTemporal = it }
                }
            }

            SectionTitle("Multimedia")
            OutlinedTextField(value = videoUrl, onValueChange = { videoUrl = it }, label = { Text("URL de Video (YouTube/Vimeo)") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { fotoLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
            ) {
                Icon(Icons.Default.PhotoCamera, null)
                Spacer(Modifier.width(8.dp))
                Text(if (fotoPrincipalUri != null) "Foto Seleccionada" else "Cambiar Foto Principal")
            }

            Spacer(Modifier.height(24.dp))

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
                        esterilizado = esterilizado,
                        desparasitado = desparasitado,
                        vacunado = vacunado,
                        aptoNinos = aptoNinos,
                        aptoAnimales = aptoAnimales,
                        hogarTemporal = hogarTemporal,
                        destacada = destacada,
                        requisitos = requisitos,
                        hogarRecomendado = hogarRecomendado,
                        videoUrl = videoUrl,
                        fotoPrincipalUri = fotoPrincipalUri,
                        fundacionId = fundacionIdState
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                enabled = state !is AdminFormState.Loading && fundacionIdState.isNotBlank()
            ) {
                if (state is AdminFormState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("GUARDAR MASCOTA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            if (state is AdminFormState.Error) {
                Text((state as AdminFormState.Error).message, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(bottom = 20.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp,
        color = Color(0xFF673AB7),
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun LabeledCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onCheckedChange(!checked) }) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, fontSize = 14.sp)
    }
}
