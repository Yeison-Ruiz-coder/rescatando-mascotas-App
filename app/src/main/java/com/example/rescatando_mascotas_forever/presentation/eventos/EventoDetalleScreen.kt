package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetalleScreen(
    navController: NavHostController,
    eventoId: Int,
    viewModel: EventoViewModel
) {
    val state by viewModel.state.collectAsState()

    // Obtenemos el evento del estado global del ViewModel.
    // Al usar remember(state, eventoId), garantizamos que si buscas un evento y haces click,
    // se encuentre correctamente por su ID único.
    val evento = remember(state, eventoId) {
        if (state is EventoState.Success) {
            (state as EventoState.Success).eventos.find { it.id == eventoId }
        } else null
    }

    Scaffold(
        bottomBar = {
            if (evento != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 16.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = { /* Acción de asistir */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                    ) {
                        Text("Confirmar Asistencia", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDF7F2))
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            if (evento != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- CABECERA CON IMAGEN INMERSIVA ---
                    Box(modifier = Modifier.height(320.dp).fillMaxWidth()) {
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

                        // Degradado oscuro superior para que el botón de atrás resalte
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Black.copy(0.4f), Color.Transparent),
                                        startY = 0f,
                                        endY = 200f
                                    )
                                )
                        )

                        // Botones de navegación sobre la imagen
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
                            }

                            IconButton(
                                onClick = { /* Compartir */ },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Compartir", tint = Color.Black)
                            }
                        }
                    }

                    // --- CUERPO DE LA PANTALLA ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-32).dp) // Efecto de solapamiento sobre la imagen
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(Color(0xFFFDF7F2))
                            .padding(24.dp)
                    ) {
                        // Badge de Categoría Minimalista
                        Surface(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF673AB7).copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = (evento.tipo ?: "EVENTO").lowercase(),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = Color(0xFF673AB7),
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
                            color = Color(0xFF2E1A7A),
                            lineHeight = 36.sp
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Formateo inteligente de Fecha y Hora (Limpio y legible)
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
                                } else if (evento.fecha.contains("-")) {
                                    val parts = evento.fecha.split(" ")
                                    Pair(parts[0], if (parts.size > 1) parts[1] else "")
                                } else {
                                    Pair(evento.fecha, "")
                                }
                            } catch (e: Exception) {
                                Pair(evento.fecha, "")
                            }
                        }

                        // Fila de Información: Fecha y Hora combinadas limpiamente
                        InfoSection(
                            icon = Icons.Default.CalendarMonth,
                            title = "Fecha y Hora",
                            subtitle = if (displayHora.isNotEmpty()) "$displayFecha • $displayHora" else displayFecha,
                            iconColor = Color(0xFF673AB7)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Fila de Información: Lugar
                        InfoSection(
                            icon = Icons.Default.LocationOn,
                            title = "Ubicación",
                            subtitle = evento.lugar,
                            iconColor = Color(0xFFE91E63)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Acerca del evento",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = evento.descripcion ?: "No hay una descripción disponible para este evento. ¡Pero seguro que será increíble! No olvides confirmar tu asistencia.",
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(120.dp)) // Espacio para el botón inferior
                    }
                }
            } else {
                // Estado de carga elegante
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF673AB7))
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
            Text(text = title, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(text = subtitle, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
