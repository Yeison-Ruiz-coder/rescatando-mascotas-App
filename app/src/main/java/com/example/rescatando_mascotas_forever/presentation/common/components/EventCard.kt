package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.ui.theme.WebAccent
import com.example.rescatando_mascotas_forever.utils.Constants

@Composable
fun EventCard(
    evento: Evento,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    val (dia, mes) = remember(evento.fecha) {
        try {
            val fechaLimpia = evento.fecha.split("T")[0]
            val delimiter = if (fechaLimpia.contains("-")) "-" else "/"
            val parts = fechaLimpia.split(delimiter)
            if (parts[0].length == 4) {
                Pair(parts[2].take(2), getMonthName(parts[1].toInt()))
            } else {
                Pair(parts[0].take(2), getMonthName(parts[1].toInt()))
            }
        } catch (e: Exception) {
            Pair("--", "--")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // FOTO DE FONDO TOTAL
            val imagePath = if (!evento.imagenUrl.isNullOrEmpty() && evento.imagenUrl != "null") evento.imagenUrl else evento.imagenPublicId
            AsyncImage(
                model = Constants.getImageUrl(imagePath),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradiente dinámico (se adapta al fondo oscuro/claro)
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                    startY = 200f
                )
            ))

            // 1. FECHA (Glassmorphism adaptable)
            Surface(
                modifier = Modifier.padding(16.dp).size(52.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(dia, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text(mes, color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            // 2. CORAZÓN (Esquina superior derecha)
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // CONTENIDO INFERIOR
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // 3. PRECIO (Badge sapote abajo)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WebAccent
                ) {
                    val costoLimpio = evento.costo?.replace("\"", "")?.replace("[", "")?.replace("]", "")?.trim() ?: ""
                    val costoText = when {
                        costoLimpio == "0" || costoLimpio.lowercase() == "gratis" || costoLimpio.isEmpty() -> "GRATIS"
                        costoLimpio.startsWith("$") -> costoLimpio
                        else -> "$$costoLimpio"
                    }
                    Text(
                        text = costoText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 4. NOMBRE
                Text(
                    text = evento.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun getMonthName(m: Int): String = when(m) {
    1 -> "ENE"; 2 -> "FEB"; 3 -> "MAR"; 4 -> "ABR"; 5 -> "MAY"; 6 -> "JUN"
    7 -> "JUL"; 8 -> "AGO"; 9 -> "SEP"; 10 -> "OCT"; 11 -> "NOV"; 12 -> "DIC"
    else -> "---"
}
