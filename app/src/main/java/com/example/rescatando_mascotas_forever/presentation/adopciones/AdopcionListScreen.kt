package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(
    navController: NavHostController,
    viewModel: AdopcionViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val mascotas by viewModel.mascotas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            bottomBar = {
                AppBottomBar(navController)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    GradientHeader(stringResource(R.string.adopt_title))
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.adopt_search_hint)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2E1A7A)) },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2E1A7A),
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }
                }

                item {
                    Text(
                        stringResource(R.string.adopt_subtitle),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else if (error != null) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(text = error!!, color = Color.Red, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                } else if (mascotas.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No hay mascotas disponibles para adopción", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
else {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            val chunks = mascotas.chunked(2)
                            chunks.forEach { rowMascotas ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    rowMascotas.forEach { mascota ->
                                        ModernPetCard(mascota, navController, Modifier.weight(1f))
                                    }
                                    if (rowMascotas.size < 2) {
                                        repeat(2 - rowMascotas.size) { Spacer(modifier = Modifier.weight(1f)) }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                        TextButton(onClick = { viewModel.cargarMascotas() }) {
                            Text(stringResource(R.string.adopt_btn_more), color = Color(0xFF2E1A7A), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(brush = AppMainGradient)
                            .padding(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(R.string.adopt_fact_title),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    stringResource(R.string.adopt_fact_desc),
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.3f),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            stringResource(R.string.adopt_steps_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E1A7A)
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        AdoptionStepItem(1, stringResource(R.string.adopt_step_1))
                        AdoptionStepItem(2, stringResource(R.string.adopt_step_2))
                        AdoptionStepItem(3, stringResource(R.string.adopt_step_3))
                        AdoptionStepItem(4, stringResource(R.string.adopt_step_4))
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Button(
                            onClick = { navController.navigate("formulario_adopcion") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(stringResource(R.string.adopt_btn_form), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun ModernPetCard(mascota: Mascota, navController: NavHostController, modifier: Modifier = Modifier) {
    val baseUrl = com.example.rescatando_mascotas_forever.utils.Constants.BASE_URL
    val fullImageUrl = if (mascota.fotoPrincipal?.startsWith("http") == true) {
        mascota.fotoPrincipal
    } else {
        "${baseUrl}storage/${mascota.fotoPrincipal}"
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = mascota.estado.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    mascota.nombre, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 16.sp, 
                    color = MaterialTheme.colorScheme.primary
                )
                val ageSuffix = if (mascota.edadAprox == 1.0) stringResource(R.string.pet_age_singular) else stringResource(R.string.pet_age_suffix)
                Text(
                    "${mascota.especie} • ${mascota.edadAprox ?: 0} $ageSuffix",
                    fontSize = 12.sp, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(mascota.ubicacion ?: "Sin ubicación", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = { navController.navigate("suscripcion_form/${mascota.id}") },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Favorite, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.width(4.dp))
                    Text("Apadrinar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdoptionStepItem(number: Int, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
    }
}
