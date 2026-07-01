package com.example.rescatando_mascotas_forever.presentation.adopciones

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.toSafeString
import com.example.rescatando_mascotas_forever.ui.theme.*
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.launch

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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    if (state is MascotaDetalleState.Success) {
                        val mascota = (state as MascotaDetalleState.Success).mascota
                        Text(
                            text = mascota.nombre, 
                            color = WebPrimary,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = WebAccent)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, "Cerrar", tint = WebDanger)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        when (state) {
            is MascotaDetalleState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is MascotaDetalleState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as MascotaDetalleState.Error).message, color = MaterialTheme.colorScheme.error)
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
                ) {
                    GallerySection(mascota)

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(Modifier.height(24.dp))

                        SectionTitle("Historia")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = mascota.descripcion ?: "Sin descripción disponible.",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Row(Modifier.fillMaxWidth()) {
                            Column(Modifier.weight(1f)) {
                                SectionTitle("Detalles")
                                DetailItem(Icons.Default.Pets, "ESPECIE", mascota.especie ?: "Desconocida")
                                DetailItem(Icons.Default.Transgender, "GÉNERO", mascota.genero ?: "No especificado")
                                DetailItem(Icons.Default.Straighten, "TAMAÑO", mascota.tamano ?: "No especificado")
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                SectionTitle("Salud")
                                // CORRECCIÓN: Usar métodos helper para obtener Booleanos reales
                                HealthItem("ESTERILIZADO", mascota.isEsterilizado())
                                HealthItem("VACUNADO", mascota.isVacunado())
                                HealthItem("DESPARASITADO", mascota.isDesparasitado())
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        SectionTitle("Compatibilidad")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            CompatCard(Icons.Default.ChildCare, "Apto con niños", mascota.isAptoNinos(), Modifier.weight(1f))
                            CompatCard(Icons.Default.Pets, "Apto con animales", mascota.isAptoOtrosAnimales(), Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(24.dp))

                        if (mascota.saludGeneral != null || mascota.enfermedadesCronicas != null) {
                            SectionTitle("Estado de Salud")
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    if (mascota.saludGeneral != null) {
                                        Text("General:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                        Text(mascota.saludGeneral.toSafeString(), fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                                    }
                                    if (mascota.enfermedadesCronicas != null) {
                                        Text("Enfermedades/Condiciones:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                        Text(mascota.enfermedadesCronicas.toSafeString(), fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        SectionTitle("Fundación")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                InfoRow("NOMBRE", mascota.fundacion?.nombre ?: "Fundación no disponible")
                                InfoRow("UBICACIÓN", "${mascota.fundacion?.ciudad ?: ""}, ${mascota.ubicacion ?: ""}")
                                InfoRow("REQUISITOS", mascota.requisitosAdopcion.toSafeString().ifBlank { "Consultar con la fundación" })
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        Text(
                            "Acciones",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        Spacer(Modifier.height(16.dp))

                        val actionGradient = Brush.horizontalGradient(colors = listOf(WebAccent, WebAccent.copy(alpha = 0.8f)))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(actionGradient)
                                .clickable { navController.navigate("formulario_adopcion?mascotaId=${mascota.id}") },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Favorite, null, tint = StaticWhite, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Solicitar adopción", color = StaticWhite, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { navController.navigate("suscripcion_form/${mascota.id}") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Stars, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Apadrinar a ${mascota.nombre}", fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun GallerySection(mascota: Mascota) {
    val context = LocalContext.current
    val fotos = remember(mascota.id, mascota.fotoPrincipal, mascota.galeriaFotos) {
        val list = mutableListOf<String>()
        mascota.fotoPrincipal?.trim()?.removeSurrounding("\"")?.let { 
            if (it.isNotBlank() && it != "null") list.add(it) 
        }
        
        try {
            val galeria = mascota.galeriaFotos
            if (galeria != null) {
                when (galeria) {
                    is List<*> -> {
                        galeria.forEach { item ->
                            val url = item?.toString()?.trim()?.removeSurrounding("\"")
                            if (!url.isNullOrBlank() && url != "null") list.add(url)
                        }
                    }
                    is String -> {
                        val raw = galeria.trim()
                        if (raw.startsWith("[")) {
                            try {
                                val jsonElement = JsonParser.parseString(raw)
                                if (jsonElement.isJsonArray) {
                                    val jsonArray = jsonElement.asJsonArray
                                    for (i in 0 until jsonArray.size()) {
                                        val element = jsonArray.get(i)
                                        val url = if (element.isJsonPrimitive) element.asString else element.toString()
                                        if (url.isNotBlank() && url != "null") {
                                            list.add(url.trim().removeSurrounding("\""))
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                raw.removePrefix("[").removeSuffix("]").split(",").forEach { item ->
                                    val url = item.trim().removeSurrounding("\"").replace("\\/", "/")
                                    if (url.isNotBlank() && url != "null") list.add(url)
                                }
                            }
                        } else if (raw.isNotBlank() && raw != "null") {
                            list.add(raw.removeSurrounding("\""))
                        }
                    }
                }
            }
        } catch (e: Exception) { 
            Log.e("GallerySection", "Error procesando galería: ${e.message}")
        }
        
        list.distinct().map { url ->
            val cleanUrl = url.replace("\\/", "/")
            if (cleanUrl.startsWith("http")) cleanUrl 
            else "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/${cleanUrl.trimStart('/')}"
        }
    }

    if (fotos.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(300.dp).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.ImageNotSupported, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), modifier = Modifier.size(64.dp))
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { fotos.size })
    val scope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxWidth().height(380.dp).background(Color.Black)) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context).data(fotos[page]).crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
            )
        }

        if (fotos.size > 1) {
            IconButton(
                onClick = { scope.launch { pagerState.animateScrollToPage((pagerState.currentPage - 1 + fotos.size) % fotos.size) } },
                modifier = Modifier.align(Alignment.CenterStart).padding(8.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Color.White) }

            IconButton(
                onClick = { scope.launch { pagerState.animateScrollToPage((pagerState.currentPage + 1) % fotos.size) } },
                modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White) }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(vertical = 12.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun HealthItem(label: String, status: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (status) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    null,
                    tint = if (status) WebSuccess else WebDanger,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(if (status) "Sí" else "No", color = if (status) WebSuccess else WebDanger, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CompatCard(icon: ImageVector, label: String, status: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Text(label, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(if (status) "Sí" else "No", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 11.sp)
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), modifier = Modifier.padding(top = 8.dp))
    }
}
