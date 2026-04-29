package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isAdminMode = currentRoute.startsWith("admin_")

    // --- ESTADOS DEL FORMULARIO ---
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 5

    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var ocupacion by remember { mutableStateOf("") }

    var tipoVivienda by remember { mutableStateOf("Casa") }
    var tienePatio by remember { mutableStateOf(false) }
    var tieneProtecciones by remember { mutableStateOf(false) }

    var integrantes by remember { mutableStateOf("") }
    var hayNinos by remember { mutableStateOf(false) }
    var estanDeAcuerdo by remember { mutableStateOf(true) }

    var tieneOtrasMascotas by remember { mutableStateOf(false) }
    var experienciaPrevia by remember { mutableStateOf("") }

    var tiempoDiario by remember { mutableStateOf("") }
    var presupuestoVeterinario by remember { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                AdminDrawerContent(navController, drawerState, scope)
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
                item { GradientHeader(stringResource(R.string.full_adop_title)) }

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
                                Icons.Default.Pets,
                                contentDescription = null,
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = stringResource(R.string.rescue_survey_step, currentStep, totalSteps),
                                color = Color(0xFF673AB7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = stringResource(R.string.full_adop_header),
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
                                    // 1. DATOS DEL SOLICITANTE
                                    AdopcionSectionHeader(Icons.Default.Person, stringResource(R.string.full_adop_sec_applicant))
                                    AdopcionFormField(stringResource(R.string.full_adop_label_fullname), nombre) { nombre = it }
                                    Row(Modifier.fillMaxWidth()) {
                                        AdopcionFormField(stringResource(R.string.full_adop_label_dni), dni, Modifier.weight(1f)) { dni = it }
                                        Spacer(Modifier.width(8.dp))
                                        AdopcionFormField(stringResource(R.string.full_adop_label_age), edad, Modifier.weight(0.5f)) { edad = it }
                                    }
                                    AdopcionFormField(stringResource(R.string.full_adop_label_occupation), ocupacion) { ocupacion = it }
                                    AdopcionFormField(stringResource(R.string.full_adop_label_phone), telefono) { telefono = it }
                                }
                                2 -> {
                                    // 2. ENTORNO Y VIVIENDA
                                    AdopcionSectionHeader(Icons.Default.Home, stringResource(R.string.full_adop_sec_housing))
                                    Text(stringResource(R.string.full_adop_q_housing_type), color = Color(0xFF333333), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        FilterChip(selected = tipoVivienda == "Casa", onClick = { tipoVivienda = "Casa" }, label = { Text(stringResource(R.string.full_adop_opt_house), color = if(tipoVivienda == "Casa") Color.White else Color.Black) })
                                        FilterChip(selected = tipoVivienda == "Apartamento", onClick = { tipoVivienda = "Apartamento" }, label = { Text(stringResource(R.string.full_adop_opt_apt), color = if(tipoVivienda == "Apartamento") Color.White else Color.Black) })
                                        FilterChip(selected = tipoVivienda == "Finca", onClick = { tipoVivienda = "Finca" }, label = { Text(stringResource(R.string.full_adop_opt_farm), color = if(tipoVivienda == "Finca") Color.White else Color.Black) })
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = tienePatio, onCheckedChange = { tienePatio = it })
                                        Text(stringResource(R.string.full_adop_label_patio), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = tieneProtecciones, onCheckedChange = { tieneProtecciones = it })
                                        Text(stringResource(R.string.full_adop_label_protection), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                }
                                3 -> {
                                    // 3. NÚCLEO FAMILIAR
                                    AdopcionSectionHeader(Icons.Default.FamilyRestroom, stringResource(R.string.full_adop_sec_family))
                                    AdopcionFormField(stringResource(R.string.full_adop_q_people_count), integrantes) { integrantes = it }

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Switch(checked = hayNinos, onCheckedChange = { hayNinos = it })
                                        Spacer(Modifier.width(12.dp))
                                        Text(stringResource(R.string.full_adop_q_kids), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Switch(checked = estanDeAcuerdo, onCheckedChange = { estanDeAcuerdo = it })
                                        Spacer(Modifier.width(12.dp))
                                        Text(stringResource(R.string.full_adop_q_agreement), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                }
                                4 -> {
                                    // 4. EXPERIENCIA Y MASCOTAS
                                    AdopcionSectionHeader(Icons.Default.History, stringResource(R.string.full_adop_sec_experience))
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = tieneOtrasMascotas, onCheckedChange = { tieneOtrasMascotas = it })
                                        Text(stringResource(R.string.full_adop_label_others), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                    AdopcionFormField(stringResource(R.string.full_adop_label_exp_desc), experienciaPrevia, Modifier.height(120.dp), singleLine = false) { experienciaPrevia = it }
                                }
                                5 -> {
                                    // 5. COMPROMISO
                                    AdopcionSectionHeader(Icons.Default.FavoriteBorder, stringResource(R.string.full_adop_sec_commitment))
                                    AdopcionFormField(stringResource(R.string.full_adop_q_daily_time), tiempoDiario) { tiempoDiario = it }

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(checked = presupuestoVeterinario, onCheckedChange = { presupuestoVeterinario = it })
                                        Text(stringResource(R.string.full_adop_label_budget), fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            Spacer(Modifier.height(30.dp))

                            // BOTONES DE NAVEGACIÓN Y ACCIÓN
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
                                        Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF673AB7))
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
                                        Icon(Icons.Default.ArrowForward, null, tint = Color.White)
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
                                        Text(stringResource(R.string.btn_send), fontWeight = FontWeight.Bold, color = Color.White)
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
fun AdopcionSectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp, top = 8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF673AB7).copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7), fontSize = 15.sp)
    }
}

@Composable
fun AdopcionFormField(label: String, value: String, modifier: Modifier = Modifier, singleLine: Boolean = true, onValueChange: (String) -> Unit) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(label, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) 45.dp else 80.dp)
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
}
