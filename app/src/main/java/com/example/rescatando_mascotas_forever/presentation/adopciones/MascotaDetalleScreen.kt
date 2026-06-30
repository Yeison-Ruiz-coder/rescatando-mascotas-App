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
                            color = WebPrimary, // Morado para el nombre
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Volver", 
                            tint = WebAccent // Naranja para la flecha
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close, 
                            contentDescription = "Cerrar", 
                            tint = WebDanger // Rojo para la X
                        )
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
                    // Carrusel de fotos profesional
                    GallerySection(mascota)

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(Modifier.height(24.dp))

                        // Historia
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

                        // Grid de Información
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
                                HealthItem("ESTERILIZADO", mascota.esterilizado ?: false)
                                HealthItem("VACUNADO", mascota.vacunado ?: false)
                                HealthItem("DESPARASITADO", mascota.desparasitado ?: false)
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Compatibilidad
                        SectionTitle("Compatibilidad")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            CompatCard(Icons.Default.ChildCare, "Apto con niños", mascota.aptoConNinos ?: true, Modifier.weight(1f))
                            CompatCard(Icons.Default.Pets, "Apto con animales", mascota.aptoConOtrosAnimales ?: true, Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(24.dp))

                        // Información de Salud Detallada
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

                        // Fundación
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

                        // SECCIÓN ACCIONES
                        Text(
                            "Acciones",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        Spacer(Modifier.height(16.dp))

                        val actionGradient = Brush.horizontalGradient(
                            colors = listOf(WebAccent, WebAccent.copy(alpha = 0.8f))
                        )
                        
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
    val baseUrl = "https://rescatando-mascotas-backend-final-production.up.railway.app/"
    
    // Extracción y limpieza de URLs de fotos
    val fotos = remember(mascota.id, mascota.fotoPrincipal, mascota.galeriaFotos) {
        val list = mutableListOf<String>()
        
        // 1. Añadir Foto Principal
        mascota.fotoPrincipal?.trim()?.removeSurrounding("\"")?.let { 
            if (it.isNotBlank() && it != "null") list.add(it) 
        }
        
        // 2. Procesar Galería JSON
        try {
            mascota.galeriaFotos?.let { galeria ->
                when {
                    galeria.isJsonArray -> {
                        galeria.asJsonArray.forEach { element ->
                            val url = if (element.isJsonPrimitive) element.asString else element.toString()
                            if (url.isNotBlank() && url != "null") list.add(url.trim().removeSurrounding("\""))
                        }
                    }
                    galeria.isJsonPrimitive -> {
                        val raw = galeria.asString.trim()
                        if (raw.startsWith("[")) {
                            // Si es un string "[...]", lo parseamos como array real para limpiar escapes \/
                            try {
                                JsonParser.parseString(raw).asJsonArray.forEach { element ->
                                    val url = element.asString
                                    if (url.isNotBlank() && url != "null") list.add(url)
                                }
                            } catch (e: Exception) {
                                // Fallback: limpieza manual si falla el parseo
                                raw.removePrefix("[").removeSuffix("]").split(",").forEach { item ->
                                    val url = item.trim().removeSurrounding("\"").removeSurrounding("'").replace("\\/", "/")
                                    if (url.isNotBlank() && url != "null") list.add(url)
                                }
                            }
                        } else if (raw.isNotBlank() && raw != "null") {
                            list.add(raw.removeSurrounding("\"").replace("\\/", "/"))
                        }
                    }
                }
            }
        } catch (e: Exception) { 
            Log.e("GallerySection", "Error procesando galería: ${e.message}")
        }
        
        // 3. Normalizar URLs y eliminar duplicados
        list.distinct().map { url ->
            val cleanUrl = url.replace("\\/", "/")
            if (cleanUrl.startsWith("http")) cleanUrl else "${baseUrl.trimEnd('/')}/storage/${cleanUrl.trimStart('/')}"
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
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1
        ) { page ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(fotos[page])
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto ${page + 1} de ${mascota.nombre}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, strokeWidth = 2.dp)
                    }
                },
                error = {
                    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.BrokenImage, null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                    }
                }
            )
        }

        // Degradado inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))))
        )

        if (fotos.size > 1) {
            IconButton(
                onClick = {
                    scope.launch {
                        val prev = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else fotos.size - 1
                        pagerState.animateScrollToPage(prev)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            IconButton(
                onClick = {
                    scope.launch {
                        val next = if (pagerState.currentPage < fotos.size - 1) pagerState.currentPage + 1 else 0
                        pagerState.animateScrollToPage(next)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Row(
                Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(fotos.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f))
                            .animateContentSize()
                    )
                }
            }
        }
        
        val estadoRaw = mascota.estado?.lowercase() ?: ""
        val tagColor = when {
            estadoRaw.contains("adopcion") || estadoRaw.contains("adopción") -> WebSuccess
            estadoRaw.contains("acogida") -> WebPrimary
            else -> WebPrimary
        }
        val tagText = mascota.estado?.uppercase() ?: "EN ADOPCIÓN"

        Surface(
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ) {
            Text(
                text = tagText,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp,
                color = tagColor,
                fontWeight = FontWeight.ExtraBold
            )
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
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun CompatCard(icon: ImageVector, label: String, status: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
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
fun HealthItem(label: String, status: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (status) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    null,
                    tint = if (status) WebSuccess else WebDanger,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    if (status) "Sí" else "No",
                    color = if (status) WebSuccess else WebDanger,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
