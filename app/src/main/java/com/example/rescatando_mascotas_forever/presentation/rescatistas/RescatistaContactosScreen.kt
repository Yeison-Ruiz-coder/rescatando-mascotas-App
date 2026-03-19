package com.example.rescatando_mascotas_forever.presentation.rescatistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Rescatista
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescatistaContactosScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val rescatistasPrueba = listOf(
        Rescatista(
            id = 1,
            nombre = "Carlos A. Muñoz",
            fotoUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            disponibilidad = "Tiempo completo",
            municipio = "Popayán",
            barrio = "El Modelo",
            especialidad = "Rescate y atención...",
            organizacion = "Paramédicos GER",
            estado = "Disponible",
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 2,
            nombre = "Diana P. Torres",
            fotoUrl = "https://randomuser.me/api/portraits/women/2.jpg",
            disponibilidad = "Fines de semana",
            municipio = "Popayán",
            barrio = "La Esmeralda",
            especialidad = "Rescate urbano de f...",
            organizacion = "Fundación Naciona...",
            estado = "Disponible",
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 3,
            nombre = "Julián R. López",
            fotoUrl = "https://randomuser.me/api/portraits/men/3.jpg",
            disponibilidad = "Turnos rotativos",
            municipio = "Popayán",
            barrio = "San Eduardo",
            especialidad = "Estudio/Rescate técni...",
            organizacion = "Independiente - Red...",
            estado = "Ocupado",
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 4,
            nombre = "María Fernanda Ruiz",
            fotoUrl = "https://randomuser.me/api/portraits/women/4.jpg",
            disponibilidad = "Tiempo completo",
            municipio = "Popayán",
            barrio = "El Modelo",
            especialidad = "Rescate animal y pri...",
            organizacion = "Fundación ComeDog...",
            estado = "Ocupado",
            whatsapp = "123456789",
            telefono = "987654321"
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
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Header Morado con diseño similar a la imagen
                item {
                    RescatistaHeader()
                }

                // Filtros (Municipio, Especialidad, Barrio, Disponible)
                item {
                    RescatistaFilters()
                }

                // Lista de Rescatistas
                items(rescatistasPrueba) { rescatista ->
                    RescatistaCard(rescatista)
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun RescatistaHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF7E57C2), Color(0xFF673AB7))
                )
            )
            .padding(vertical = 32.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Contacto de rescatistas",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Este espacio nace como respuesta al compromiso creciente de Popayán con el bienestar animal. Está diseñado para acercar a los rescatistas que, con entrega y compasión, dedican su tiempo a salvar, rehabilitar y proteger animales en situación de abandono, maltrato o vulnerabilidad.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun RescatistaFilters() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterDropdown("Municipio", Modifier.weight(1f))
            FilterDropdown("Especialidad", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterDropdown("Barrio", Modifier.weight(1f))
            FilterDropdown("Disponible", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FilterDropdown(label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF7E57C2),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Todos", color = Color.White, fontSize = 13.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun RescatistaCard(rescatista: Rescatista) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) { // Aumentado padding interno
            Text(
                text = rescatista.nombre,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp, // Aumentado tamaño nombre
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp)) // Más espacio bajo el nombre

            Row(
                verticalAlignment = Alignment.Top, // Alineado arriba para dar más aire
                modifier = Modifier.fillMaxWidth()
            ) {
                // Foto más grande y con más espacio
                Image(
                    painter = rememberAsyncImagePainter(rescatista.fotoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp) // Foto un poco más grande
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp)) // Más espacio entre foto e info

                // Columna central de información
                Column(modifier = Modifier.weight(1.3f)) {
                    RescatistaDetail("Disponibilidad:", rescatista.disponibilidad, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail("Municipio:", rescatista.municipio, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail("Barrio:", rescatista.barrio, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail("Especialidad:", rescatista.especialidad, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail("Organización:", rescatista.organizacion, Color(0xFF9575CD))
                }

                // Columna derecha de estado y botones
                Column(
                    modifier = Modifier.weight(0.7f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text("Estado", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (rescatista.estado == "Disponible") Color(0xFF7E57C2) else Color(0xFFF06292)
                    ) {
                        Text(
                            text = rescatista.estado,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp)) // Más espacio antes de los botones

                    // Botón WhatsApp
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { },
                        shape = CircleShape,
                        color = Color(0xFF25D366),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Call, 
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón Teléfono
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { },
                        shape = CircleShape,
                        color = Color(0xFF7E57C2),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RescatistaDetail(label: String, value: String, valueColor: Color) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = value, 
            fontSize = 12.sp, 
            color = valueColor, 
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}
