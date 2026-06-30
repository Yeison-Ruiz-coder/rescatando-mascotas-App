package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetalleScreen(
    navController: NavHostController,
    eventoId: Int,
    viewModel: EventoViewModel
) {
    val state by viewModel.state.collectAsState()
    val isDark = ThemeController.isDarkOverride.value ?: isSystemInDarkTheme()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val evento = remember(state, eventoId) {
        if (state is EventoState.Success) {
            (state as EventoState.Success).eventos.find { it.id == eventoId }
        } else null
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { 
                    evento?.let { 
                        Text(
                            text = it.nombre, 
                            color = WebPrimary, // Nombre morado
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        ) 
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Volver", 
                            tint = WebAccent // Flecha naranja
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Compartir */ }) {
                        Icon(
                            imageVector = Icons.Default.Share, 
                            contentDescription = "Compartir", 
                            tint = WebPrimary // Morado para compartir
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) StaticWhite else StaticBlack,
                    scrolledContainerColor = if (isDark) StaticWhite else StaticBlack
                )
            )
        },
        bottomBar = {
            if (evento != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Button(
                        onClick = { /* Acción de asistir */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WebAccent) // Sapote
                    ) {
                        Text("Confirmar Asistencia", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StaticWhite)
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            if (evento != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- CABECERA CON IMAGEN ---
                    Box(modifier = Modifier.height(280.dp).fillMaxWidth()) {
                        val imagePath = when {
                            !evento.imagenUrl.isNullOrEmpty() && evento.imagenUrl != "null" -> evento.imagenUrl
                            !evento.imagenPublicId.isNullOrEmpty() && evento.imagenPublicId != "null" -> evento.imagenPublicId
                            else -> null
                        }
                        val fullImageUrl = com.example.rescatando_mascotas_forever.utils.Constants.getImageUrl(imagePath)

                        Image(
                            painter = rememberAsyncImagePainter(model = fullImageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(0.2f)),
                                        startY = 200f
                                    )
                                )
                        )
                    }

                    // --- CUERPO ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        // Badge de Categoría
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WebPrimary.copy(alpha = 0.3f))
                        ) {
                            val tagText = (evento.tipo ?: "EVENTO").uppercase().replace("EN ", "")
                            Text(
                                text = tagText,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = WebPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = evento.nombre,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 36.sp
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Formateo de Fecha y Hora
                        val (displayFecha, displayHora) = remember(evento.fecha) {
                            try {
                                if (evento.fecha.contains("T")) {
                                    val parts = evento.fecha.split("T")
                                    val datePart = parts[0]
                                    val timePart = if (parts.size > 1) {
                                        val timeStr = parts[1].substring(0, 5)
                                        val timeParts = timeStr.split(":")
                                        val horaRaw = timeParts[0].toInt()
                                        val amPm = if (horaRaw >= 12) "PM" else "AM"
                                        val hora12 = if (horaRaw % 12 == 0) 12 else horaRaw % 12
                                        String.format("%02d:%s %s", hora12, timeParts[1], amPm)
                                    } else ""
                                    Pair(datePart, timePart)
                                } else Pair(evento.fecha, "")
                            } catch (e: Exception) {
                                Pair(evento.fecha, "")
                            }
                        }

                        InfoSection(
                            icon = Icons.Default.CalendarMonth,
                            title = "Fecha y Hora",
                            subtitle = if (displayHora.isNotEmpty()) "$displayFecha • $displayHora" else displayFecha,
                            iconColor = WebPrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        InfoSection(
                            icon = Icons.Default.LocationOn,
                            title = "Ubicación",
                            subtitle = evento.lugar,
                            iconColor = WebAccent
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Acerca del evento",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = evento.descripcion ?: "No hay una descripción disponible.",
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = WebPrimary)
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(text = title, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
            Text(text = subtitle, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
    }
}
