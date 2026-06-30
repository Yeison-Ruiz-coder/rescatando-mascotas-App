package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Mascota

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaDetalleScreen(
    navController: NavHostController,
    mascotaId: Int,
    viewModel: MascotaDetalleViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(mascotaId) {
        viewModel.cargarMascota(mascotaId)
    }

    Scaffold(
        containerColor = Color(0xFF0F0E17),
        topBar = {
            TopAppBar(
                title = { 
                    if (state is MascotaDetalleState.Success) {
                        Text((state as MascotaDetalleState.Success).mascota.nombre ?: "Detalles", color = Color.White, fontWeight = FontWeight.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        when (state) {
            is MascotaDetalleState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF7B5EE1))
                }
            }
            is MascotaDetalleState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as MascotaDetalleState.Error).message, color = Color.Red)
                }
            }
            is MascotaDetalleState.Success -> {
                val mascota = (state as MascotaDetalleState.Success).mascota
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp)
                ) {
                    // Galería de Imágenes
                    GallerySection(mascota)

                    Spacer(Modifier.height(24.dp))

                    // Sección Historia
                    Text(
                        "Historia",
                        color = Color(0xFF7B5EE1),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A23)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Text(
                            text = mascota.descripcion ?: "No hay historia disponible.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // Grid de Información y Salud
                    Row(Modifier.fillMaxWidth()) {
                        // Columna Izquierda: Datos Básicos
                        Column(Modifier.weight(1f)) {
                            Text(mascota.nombre ?: "Mascota", color = Color(0xFF7B5EE1), fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Spacer(Modifier.height(12.dp))
                            DetailItem(Icons.Default.Pets, "ESPECIE", mascota.especie ?: "Perro")
                            DetailItem(Icons.Default.Transgender, "GÉNERO", mascota.genero ?: "Macho")
                            DetailItem(Icons.Default.CalendarToday, "EDAD", "${mascota.edadAprox?.toInt() ?: 0} años")
                        }

                        Spacer(Modifier.width(20.dp))

                        // Columna Derecha: Salud y Cuidados
                        Column(Modifier.weight(1f)) {
                            Text("Salud y Cuidados", color = Color(0xFF7B5EE1), fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Spacer(Modifier.height(12.dp))
                            HealthItem("ESTERILIZADO", mascota.esterilizado)
                            HealthItem("DESPARASITADO", mascota.desparasitado)
                            HealthItem("VACUNADO", mascota.vacunado)
                        }
                    }

                    Spacer(Modifier.height(40.dp))

                    // Botón de Acción Principal
                    Button(
                        onClick = { navController.navigate("formulario_adopcion?mascotaId=${mascota.id}") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B5EE1)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("QUIERO ADOPTAR A ${(mascota.nombre ?: "MASCOTA").uppercase()}", fontWeight = FontWeight.ExtraBold)
                    }
                    
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun GallerySection(mascota: Mascota) {
    val baseUrl = "https://rescatando-mascotas-backend-final-production.up.railway.app/"
    val fotos = mutableListOf<String>()
    mascota.fotoPrincipal?.let { fotos.add(it) }
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(fotos) { foto ->
            val url = if (foto.startsWith("http")) foto else "${baseUrl}storage/$foto"
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF7B5EE1), RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
        // Placeholders para completar el diseño como en la web
        items(listOf(1,2)) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B1A23))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A23)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFF7B5EE1), modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HealthItem(label: String, status: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A23)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (status) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    null,
                    tint = if (status) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    if (status) "Sí" else "No",
                    color = if (status) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
