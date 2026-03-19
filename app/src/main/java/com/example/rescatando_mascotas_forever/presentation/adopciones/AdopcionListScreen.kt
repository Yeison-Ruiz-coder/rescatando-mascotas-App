package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(navController: NavHostController, especie: String? = null) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Datos de prueba para la cuadrícula
    val mascotasPrueba = listOf(
        Mascota(1, "Boby", "Perro", 2, "Macho", "Disponible", "Lomas de granada", "Descripción de Boby", "https://images.unsplash.com/photo-1543466835-00a7907e9de1?q=80&w=1000&auto=format&fit=crop", true, true, 1),
        Mascota(2, "Moly", "Perro", 1, "Hembra", "Disponible", "Lomas de granada", "Descripción de Moly", "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?q=80&w=1000&auto=format&fit=crop", true, true, 1),
        Mascota(3, "Nala", "Perro", 3, "Hembra", "Disponible", "Lomas de granada", "Descripción de Nala", "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?q=80&w=1000&auto=format&fit=crop", true, true, 1),
        Mascota(4, "Felix", "Gato", 2, "Macho", "Disponible", "Lomas de granada", "Descripción de Felix", "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=1000&auto=format&fit=crop", true, true, 1),
        Mascota(5, "Misifu", "Gato", 1, "Macho", "Disponible", "Lomas de granada", "Descripción de Misifu", "https://www.besame.fm/wp-content/uploads/2025/01/07012025-significado-de-encontrar-un-gato-negro-en-la-calle.png", true, true, 1),
        Mascota(6, "Luna", "Gato", 2, "Hembra", "Disponible", "Lomas de granada", "Descripción de Luna", "https://images.unsplash.com/photo-1495360010541-f48722b34f7d?q=80&w=1000&auto=format&fit=crop", true, true, 1)
    )

    val mascotasFiltradas = if (especie != null) {
        mascotasPrueba.filter { it.especie.equals(especie, ignoreCase = true) }
    } else {
        mascotasPrueba
    }

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
                modifier = Modifier.fillMaxSize().padding(padding).background(Color.White)
            ) {
                // 1. Header Unificado
                item {
                    GradientHeader(if (especie != null) "Adopciones - $especie" else "Adopciones")
                }

                // 2. Buscador Moderno
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
                            placeholder = { Text("Buscar por raza, edad o ciudad...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2E1A7A)) },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2E1A7A),
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }
                }

                // 3. Título de Sección
                item {
                    Text(
                        if (mascotasFiltradas.isEmpty()) "No se encontraron mascotas en esta categoría" else "Encuentra a tu nuevo mejor amigo",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E1A7A)
                    )
                }

                // 4. Grilla de Mascotas (2 columnas)
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        val chunks = mascotasFiltradas.chunked(2)
                        chunks.forEach { rowMascotas ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                rowMascotas.forEach { mascota ->
                                    ModernPetCard(mascota, Modifier.weight(1f))
                                }
                                if (rowMascotas.size < 2) {
                                    repeat(2 - rowMascotas.size) { Spacer(modifier = Modifier.weight(1f)) }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // 5. Botón Ver más
                if (especie != null && mascotasFiltradas.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                            TextButton(onClick = { navController.navigate("adopciones") }) {
                                Text("Ver todas las mascotas →", color = Color(0xFF2E1A7A), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 6. Sección Informativa Destacada
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
                                    "¿Sabías que...?",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Adoptar salva dos vidas: la del animal que llevas a casa y la del siguiente que puede ocupar su lugar.",
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

                // 7. Gestión y Requisitos
                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            "Pasos para adoptar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E1A7A)
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        AdoptionStepItem(1, "Elige a tu mascota ideal de nuestra lista.")
                        AdoptionStepItem(2, "Completa el formulario de solicitud oficial.")
                        AdoptionStepItem(3, "Realizaremos una pequeña entrevista y visita.")
                        AdoptionStepItem(4, "¡Firma el acta y llévalo a su nuevo hogar!")
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Button(
                            onClick = { navController.navigate("formulario_adopcion") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Ir al Formulario de Adopción", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun ModernPetCard(mascota: Mascota, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(mascota.fotoPrincipal),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = mascota.estado.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E1A7A)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    mascota.nombre, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 16.sp, 
                    color = Color(0xFF2E1A7A)
                )
                Text(
                    "${mascota.especie} • ${mascota.edadAprox} años", 
                    fontSize = 12.sp, 
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(mascota.ubicacion, fontSize = 11.sp, color = Color.Gray)
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
                .background(Color(0xFF2E1A7A)),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
    }
}
