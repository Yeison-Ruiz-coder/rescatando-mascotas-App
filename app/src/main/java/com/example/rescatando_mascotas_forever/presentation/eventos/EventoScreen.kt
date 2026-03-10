package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categorias = listOf("Todos", "Gratis", "Concursos", "Adopciones")
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val eventosPrueba = listOf(
        Evento(
            id = 1,
            titulo = "Gran Jornada de Adopción 2025",
            fecha = "15 Marzo",
            hora = "9am - 5pm",
            precio = "Gratis",
            ubicacion = "Parque Simón Bolívar, Bogotá",
            descripcion = "Más de 80 mascotas buscando hogar. Habrá veterinarios, vacunación gratuita, microchip y mucho más.",
            imagenUrl = "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?q=80&w=1000",
            etiqueta = "DESTACADO",
            confirmados = 342
        ),
        Evento(
            id = 2,
            titulo = "Taller de Adiestramiento Básico",
            fecha = "22 Marzo",
            hora = "10am",
            precio = "$35.000",
            ubicacion = "Centro Veterinario Norte",
            descripcion = "Aprende las técnicas básicas para educar a tu cachorro de forma positiva.",
            imagenUrl = "https://images.unsplash.com/photo-1587300003388-59208cc962cb?q=80&w=1000",
            etiqueta = "TALLER",
            cuposActuales = 18,
            cuposTotales = 25
        ),
        Evento(
            id = 3,
            titulo = "Feria Mascota Feliz 🎊",
            fecha = "05 Abril",
            hora = "11am",
            precio = "$15.000",
            ubicacion = "C.C. Gran Estación, Bogotá",
            descripcion = "Un día lleno de sorpresas, premios y actividades para toda la familia multiespecie.",
            imagenUrl = "https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?q=80&w=1000",
            etiqueta = "CONCURSO",
            confirmados = 126
        )
    )

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFFDF7F2) // Color crema suave de fondo
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Eventos",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Actividades para ti y tu mascota",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        items(categorias) { categoria ->
                            FilterChip(
                                selected = categoria == categoriaSeleccionada,
                                onClick = { categoriaSeleccionada = categoria },
                                label = { Text(categoria) },
                                leadingIcon = {
                                    val icon = when(categoria) {
                                        "Todos" -> Icons.Default.DateRange
                                        "Gratis" -> Icons.Default.Favorite
                                        "Concursos" -> Icons.Default.Star
                                        else -> Icons.Default.Face
                                    }
                                    Icon(icon, null, modifier = Modifier.size(18.dp))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF673AB7),
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color.Gray
                                ),
                                border = if (categoria == categoriaSeleccionada) null else BorderStroke(1.dp, Color.LightGray)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Evento Destacado (el primero)
                item {
                    EventCard(eventosPrueba[0], isFeatured = true)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Próximos eventos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Lista de otros eventos
                items(eventosPrueba.drop(1)) { evento ->
                    EventCard(evento)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun EventCard(evento: Evento, isFeatured: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(evento.imagenUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isFeatured) 200.dp else 160.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Etiqueta (DESTACADO, TALLER, etc)
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF673AB7).copy(alpha = 0.9f)
                ) {
                    Text(
                        text = evento.etiqueta,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = evento.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${evento.fecha} - ${evento.hora}", fontSize = 13.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Icon(Icons.Default.ShoppingCart, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = evento.precio, fontSize = 13.sp, color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = evento.ubicacion, fontSize = 13.sp, color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = evento.descripcion,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF3F51B5), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    
                    val infoExtra = if (evento.confirmados != null) {
                        "${evento.confirmados} confirmados"
                    } else {
                        "${evento.cuposActuales} / ${evento.cuposTotales} cupos"
                    }
                    
                    Text(text = infoExtra, fontSize = 13.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    val botonTexto = when(evento.etiqueta) {
                        "DESTACADO" -> "Quiero ir →"
                        "TALLER" -> "Reservar →"
                        else -> "Ver más →"
                    }
                    
                    Button(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF673AB7),
                            contentColor = Color.White // Asegura que el contenido sea blanco
                        )
                    ) {
                        Text(
                            text = botonTexto, 
                            fontSize = 12.sp, 
                            fontWeight = FontWeight.Bold,
                            color = Color.White // Refuerza el color blanco para el texto y la flecha
                        )
                    }
                }
            }
        }
    }
}
