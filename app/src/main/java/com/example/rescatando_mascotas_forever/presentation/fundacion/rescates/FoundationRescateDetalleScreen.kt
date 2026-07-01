package com.example.rescatando_mascotas_forever.presentation.fundacion.rescates

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import com.example.rescatando_mascotas_forever.data.network.models.getGaleriaAsList
import com.example.rescatando_mascotas_forever.ui.theme.WebPrimary
import com.example.rescatando_mascotas_forever.ui.theme.WebSecondary
import com.example.rescatando_mascotas_forever.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationRescateDetalleScreen(
    navController: NavHostController,
    rescateId: Int,
    viewModel: FoundationRescatesViewModel = viewModel()
) {
    val state by viewModel.detailState.collectAsState()

    LaunchedEffect(rescateId) {
        viewModel.fetchRescateById(rescateId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Emergencia", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WebPrimary)
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is FoundationRescateDetailState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = WebPrimary)
                }
            }
            is FoundationRescateDetailState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ErrorOutline, null, Modifier.size(64.dp), tint = Color.LightGray)
                        Text(currentState.message, color = Color.Gray, modifier = Modifier.padding(16.dp))
                        Button(onClick = { viewModel.fetchRescateById(rescateId) }) { Text("Reintentar") }
                    }
                }
            }
            is FoundationRescateDetailState.Success -> {
                RescateDetailContent(padding, currentState.rescate, navController, viewModel)
            }
        }
    }
}

@Composable
fun RescateDetailContent(
    padding: PaddingValues,
    rescate: Rescate,
    navController: NavHostController,
    viewModel: FoundationRescatesViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8F9FE))
    ) {
        // --- IMAGEN PRINCIPAL (Mismo estilo que Mascotas pública) ---
        Box(modifier = Modifier.height(280.dp).fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(Constants.getImageUrl(rescate.fotoPrincipal)),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Badge de Prioridad
            val priorityColor = when (rescate.prioridad?.lowercase()) {
                "alta" -> Color(0xFFD32F2F)
                "media" -> Color(0xFFF57C00)
                else -> WebPrimary
            }
            Surface(
                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd),
                shape = RoundedCornerShape(8.dp),
                color = priorityColor
            ) {
                Text(
                    text = rescate.tipoEmergencia.uppercase(),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "REPORTE CIUDADANO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WebPrimary,
                letterSpacing = 1.sp
            )
            Text(
                text = rescate.descripcionRescate,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- TARJETA DE DETALLES ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    DetailInfoItem(Icons.Default.Place, "Ubicación", rescate.lugarRescate)
                    HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
                    DetailInfoItem(Icons.Default.Person, "Reportante", rescate.usuarioReporto?.nombre ?: rescate.nombreReportante ?: "Anónimo")
                    
                    if (!rescate.telefonoReportante.isNullOrBlank()) {
                        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
                        DetailInfoItem(Icons.Default.Phone, "Contacto", rescate.telefonoReportante)
                    }
                    
                    HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
                    DetailInfoItem(Icons.Default.Schedule, "Estado actual", rescate.estado.uppercase())
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- GALERÍA ---
            val galeria = rescate.getGaleriaAsList()
            if (galeria.isNotEmpty()) {
                Text("Evidencias fotográficas", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(galeria) { foto ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(Constants.getImageUrl(foto)),
                                contentDescription = null,
                                modifier = Modifier.size(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- BOTONES DE ACCIÓN ---
            if (rescate.estado == "pendiente") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { rescate.id?.let { viewModel.aceptarRescate(it) { navController.popBackStack() } } },
                        modifier = Modifier.weight(1.2f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WebPrimary)
                    ) {
                        Text("ACEPTAR", fontWeight = FontWeight.ExtraBold)
                    }
                    
                    OutlinedButton(
                        onClick = { rescate.id?.let { viewModel.rechazarRescate(it) { navController.popBackStack() } } },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Red),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("RECHAZAR", fontWeight = FontWeight.Bold)
                    }
                }
            } else if (rescate.estado == "en_proceso") {
                Button(
                    onClick = { 
                        // Navega al formulario de registro de mascota
                        navController.navigate("foundation_mascota_form?mascotaId=null") 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WebSecondary)
                ) {
                    Icon(Icons.Default.Pets, null)
                    Spacer(Modifier.width(8.dp))
                    Text("REGISTRAR MASCOTA RESCATADA", fontWeight = FontWeight.ExtraBold)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun DetailInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = WebPrimary.copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = WebPrimary)
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
