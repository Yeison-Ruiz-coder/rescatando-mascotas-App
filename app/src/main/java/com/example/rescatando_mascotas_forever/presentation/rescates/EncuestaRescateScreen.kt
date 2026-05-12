package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import com.example.rescatando_mascotas_forever.presentation.admin.AdminDrawerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isAdminMode = currentRoute.startsWith("admin_")

    // --- ESTADOS DEL FORMULARIO ---
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3

    var nombreRescatista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var nombreMascota by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var estadoSalud by remember { mutableStateOf("") }

    var ubicacion by remember { mutableStateOf("") }
    var condicionesEncontrado by remember { mutableStateOf("") }

    var nombreReportante by remember { mutableStateOf("") }
    var numeroContacto by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                if (isAdminMode) {
                    AdminDrawerContent(navController, drawerState, scope)
                } else {
                    com.example.rescatando_mascotas_forever.presentation.common.components.DrawerContent(navController, drawerState, scope)
                }
            }
        }
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { if (!isAdminMode) AppBottomBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { GradientHeader(stringResource(R.string.rescue_survey_title)) }

                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Assignment,
                                contentDescription = null,
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                stringResource(R.string.rescue_survey_step, currentStep, totalSteps),
                                color = Color(0xFF673AB7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                stringResource(R.string.rescue_survey_header),
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            LinearProgressIndicator(
                                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF673AB7),
                                trackColor = Color(0xFFEEEEEE),
                            )

                            when (currentStep) {
                                1 -> {
                                    // 1. INFORMACIÓN DE LA MASCOTA
                                    RescateSectionHeader(Icons.Default.Pets, stringResource(R.string.rescue_survey_sec_pet))
                                    Row(Modifier.fillMaxWidth()) {
                                        RescateFormField(stringResource(R.string.rescue_form_pet_name), nombreMascota, Modifier.weight(1f)) { nombreMascota = it }
                                        Spacer(Modifier.width(8.dp))
                                        RescateFormField(stringResource(R.string.rescue_form_species), especie, Modifier.weight(1f)) { especie = it }
                                    }
                                    Row(Modifier.fillMaxWidth()) {
                                        RescateFormField(stringResource(R.string.rescue_survey_label_age), edad, Modifier.weight(1f)) { edad = it }
                                        Spacer(Modifier.width(8.dp))
                                        RescateFormField(stringResource(R.string.rescue_survey_label_sex), sexo, Modifier.weight(1f)) { sexo = it }
                                    }
                                    RescateFormField(stringResource(R.string.rescue_survey_label_health), estadoSalud) { estadoSalud = it }

                                    Spacer(Modifier.height(8.dp))
                                    Row(Modifier.fillMaxWidth()) {
                                        RescateFormField(stringResource(R.string.rescue_survey_label_rescuer), nombreRescatista, Modifier.weight(1f)) { nombreRescatista = it }
                                        Spacer(Modifier.width(8.dp))
                                        RescateFormField(stringResource(R.string.rescue_survey_label_date), fecha, Modifier.width(110.dp)) { fecha = it }
                                    }
                                }
                                2 -> {
                                    // 2. DETALLES DEL RESCATE
                                    RescateSectionHeader(Icons.Default.LocationOn, stringResource(R.string.rescue_survey_sec_details))
                                    RescateFormField(stringResource(R.string.rescue_survey_label_loc), ubicacion) { ubicacion = it }
                                    Spacer(Modifier.height(8.dp))
                                    Text(stringResource(R.string.rescue_survey_desc_cond), color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 4.dp))
                                    RescateTextFieldCustom(condicionesEncontrado, Modifier.height(140.dp), singleLine = false) { condicionesEncontrado = it }
                                }
                                3 -> {
                                    // 3. DATOS DEL REPORTANTE
                                    RescateSectionHeader(Icons.Default.ContactPhone, stringResource(R.string.rescue_survey_sec_reporter))
                                    RescateFormField(stringResource(R.string.rescue_survey_label_reporter_name), nombreReportante) { nombreReportante = it }
                                    RescateFormField(stringResource(R.string.rescue_survey_label_reporter_phone), numeroContacto) { numeroContacto = it }

                                    Spacer(Modifier.height(20.dp))
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Info, null, tint = Color(0xFF2E7D32))
                                            Spacer(Modifier.width(12.dp))
                                            Text(stringResource(R.string.rescue_survey_info_verify), fontSize = 12.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(30.dp))

                            // BOTONES DE NAVEGACIÓN
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (currentStep > 1) {
                                    OutlinedButton(
                                        onClick = { currentStep-- },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, Color(0xFF673AB7))
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFF673AB7))
                                        Spacer(Modifier.width(4.dp))
                                        Text(stringResource(R.string.btn_previous), color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (currentStep < totalSteps) {
                                    Button(
                                        onClick = { currentStep++ },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF673AB7),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(stringResource(R.string.btn_next), fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(Modifier.width(4.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
                                    }
                                } else {
                                    Button(
                                        onClick = { /* Lógica de guardado */ },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(Icons.Default.Save, null, tint = Color.White)
                                        Spacer(Modifier.width(4.dp))
                                        Text(stringResource(R.string.btn_finish), fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }

                            if (currentStep == totalSteps) {
                                Spacer(Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color.Red)
                                ) {
                                    Text(stringResource(R.string.btn_cancel_upper), color = Color.Red, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun RescateSectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
    ) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.ExtraBold, color = Color(0xFF673AB7), fontSize = 15.sp)
    }
}

@Composable
fun RescateFormField(label: String, value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(label, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
        RescateTextFieldCustom(value, onValueChange = onValueChange, singleLine = singleLine)
    }
}

@Composable
fun RescateTextFieldCustom(value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F3F4))
            .border(1.dp, Color(0xFFDADCE0).copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                if (value.isEmpty()) Text(stringResource(R.string.hint_type_here), color = Color(0xFF666666), fontSize = 14.sp)
                innerTextField()
            }
        }
    )
}
