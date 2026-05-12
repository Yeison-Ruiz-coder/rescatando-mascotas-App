package com.example.rescatando_mascotas_forever.presentation.adopciones.solicitud

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionSolicitudScreen(
    navController: NavHostController,
    mascotaId: Int
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ocupacion by remember { mutableStateOf("") }
    var tieneMascotas by remember { mutableStateOf(false) }
    var porQueAdopta by remember { mutableStateOf("") }

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.adop_form_title), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                // Cabecera con Info de la Mascota
                item {
                    SolicitudPetHeader("Boby") // Nombre temporal, debería venir de un ViewModel
                }

                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Sección: Datos Personales
                        FormSectionLabel(stringResource(R.string.rescuer_reg_sec_personal))
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text(stringResource(R.string.rescuer_reg_label_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF673AB7)) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = { Text(stringResource(R.string.rescue_survey_label_reporter_phone)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color(0xFF673AB7)) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Sección: Vivienda y Estilo de Vida
                        FormSectionLabel(stringResource(R.string.adop_sec_lifestyle))
                        OutlinedTextField(
                            value = direccion,
                            onValueChange = { direccion = it },
                            label = { Text(stringResource(R.string.rescue_survey_label_loc)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Home, null, tint = Color(0xFF673AB7)) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = ocupacion,
                            onValueChange = { ocupacion = it },
                            label = { Text(stringResource(R.string.full_adop_label_occupation)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Work, null, tint = Color(0xFF673AB7)) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = tieneMascotas,
                                onCheckedChange = { tieneMascotas = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7))
                            )
                            Text(stringResource(R.string.adop_label_others_convive), fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Sección: Compromiso
                        FormSectionLabel(stringResource(R.string.full_adop_sec_commitment))
                        OutlinedTextField(
                            value = porQueAdopta,
                            onValueChange = { porQueAdopta = it },
                            label = { Text(stringResource(R.string.adop_form_label_motive)) },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { 
                                // Aquí se llamaría al ViewModel para enviar la solicitud
                                navController.navigate("home") 
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                        ) {
                            Text(stringResource(R.string.adop_btn_send_full), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudPetHeader(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1543466835-00a7907e9de1"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                stringResource(R.string.adop_applying_to),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Text(
                name,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun FormSectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF673AB7),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}
