package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.ui.theme.*
import com.example.rescatando_mascotas_forever.utils.Constants

@Composable
fun PetCard(
    mascota: Mascota,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fullImageUrl = Constants.getImageUrl(mascota.fotoPrincipal)
    var isFavorite by remember { mutableStateOf(false) }

    val estadoRaw = mascota.estado?.lowercase() ?: ""
    val tagColor = when {
        estadoRaw.contains("adopcion") || estadoRaw.contains("adopción") -> WebSuccess
        estadoRaw.contains("acogida") -> WebPrimary
        else -> WebPrimary
    }
    
    val tagText = when {
        estadoRaw.contains("adopcion") || estadoRaw.contains("adopción") -> "ADOPCIÓN"
        estadoRaw.contains("acogida") -> "ACOGIDA"
        else -> mascota.estado?.uppercase()?.replace("EN ", "") ?: "ADOPCIÓN"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(18.dp),
                            tint = if (isFavorite) WebHeart else Color.Gray
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = mascota.nombre,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = tagText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            color = tagColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Text(
                    text = "${mascota.especie ?: "Desconocida"} • ${mascota.edadAprox?.toInt() ?: 0} años",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
