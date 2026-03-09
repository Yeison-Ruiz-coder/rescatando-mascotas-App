package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Datos de prueba para la cuadrícula
    val mascotasPrueba = listOf(
        Mascota(1, "Boby", "Perro", 2, "Macho", "Disponible", "Lomas de granada", "https://www.eluniverso.com/resizer/v2/O7KQPPPB3FBPJCMSYCPDBQRFQQ.jpeg?auth=a9b01da1ab8408eaad376afe9f6737b0f3f4e77976cd2cf7f90f5c011834be7e&width=1191&height=670&quality=75&smart=true", true, true, 1),
        Mascota(2, "Moly", "Perro", 1, "Hembra", "Disponible", "Lomas de granada", "https://images.dog.ceo/breeds/retriever-golden/n02099601_3004.jpg", true, true, 1),
        Mascota(3, "Nala", "Perro", 3, "Hembra", "Disponible", "Lomas de granada", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYCWRMQoJ4PCR7yYa-vKN8mknNvAIo3UfQQw&s", true, true, 1),
        Mascota(4, "Felix", "Gato", 2, "Macho", "Disponible", "Lomas de granada", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhGJq_ATRDJ9WChNF3IWHOdmRYIErpo7Qy7Q&s", true, true, 1),
        Mascota(5, "Misifu", "Gato", 1, "Macho", "Disponible", "Lomas de granada", "https://thumbs.dreamstime.com/b/gatito-rescatado-de-la-inundaci%C3%B3n-peque%C3%B1o-acaba-ser-lo-levant%C3%A9-y-puse-en-una-caja-peque%C3%B1a-sus-ojos-irradiaban-tristeza-262076026.jpg?w=768", true, true, 1),
        Mascota(6, "Luna", "Gato", 2, "Hembra", "Disponible", "Lomas de granada", "https://i0.wp.com/puppis.blog/wp-content/uploads/2020/02/Particularidades-de-los-felinos.jpg?w=1200&ssl=1", true, true, 1)
    )

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
                NavigationBar(containerColor = Color(0xFF5E49BF)) {
                    NavigationBarItem(selected = false, onClick = { navController.navigate("home") }, icon = { Icon(Icons.Default.Home, null, tint = Color.White.copy(0.6f)) }, label = { Text("Inicio", color = Color.White.copy(0.6f)) })
                    NavigationBarItem(selected = true, onClick = { }, icon = { Icon(Icons.Default.Search, null, tint = Color.White) }, label = { Text("Buscar", color = Color.White) })
                    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Default.Warning, null, tint = Color.White.copy(0.6f)) }, label = { Text("Reportar", color = Color.White.copy(0.6f)) })
                    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Default.Person, null, tint = Color.White.copy(0.6f)) }, label = { Text("Perfil", color = Color.White.copy(0.6f)) })
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF6EEE9))
            ) {
                // 1. Header Adopciones
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(160.dp).background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Adopciones", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Light)
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(0.85f).height(45.dp),
                                shape = RoundedCornerShape(25.dp),
                                color = Color.White.copy(0.2f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Icon(Icons.Default.Search, null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("\"Buscar mascotas...\"", color = Color.White.copy(0.7f), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                // 2. Grilla de Mascotas
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val chunks = mascotasPrueba.chunked(2) // Cambiado a 2 columnas para que se parezcan más al Home
                        chunks.forEach { rowMascotas ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                rowMascotas.forEach { mascota ->
                                    MascotaCardVerticalAdopcion(mascota, Modifier.weight(1f))
                                }
                                if (rowMascotas.size < 2) {
                                    repeat(2 - rowMascotas.size) { Spacer(modifier = Modifier.weight(1f)) }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // 3. Botón Ver más...
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB388FF)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ver mas...", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.Gray.copy(0.2f), thickness = 1.dp)
                }

                // 4. Sección Reflexiona
                item {
                    SectionTitleAdopcion("Reflexiona")
                    Box(
                        modifier = Modifier.padding(16.dp).fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp)).background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))))
                    ) {
                        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(90.dp).clip(CircleShape).background(Color.Black), contentAlignment = Alignment.Center) {
                                Image(
                                        painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkjpuDUdhTX9QkpJE9fFm0ZM_-_s5_wmatRg&s"),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                                )
                                Icon(Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.size(40.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                "Cuando adoptas, no solo salvas una vida...\ntambién llenas tu hogar de amor.",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // 5. Sección Formularios y Requisitos
                item {
                    SectionTitleAdopcion("Formularios y Requisitos")
                    Box(
                        modifier = Modifier.padding(16.dp).fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFB39DDB).copy(0.4f))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Aquí iría el dibujo de las ondas
                        }
                        
                        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(shape = RoundedCornerShape(15.dp), color = Color(0xFFB388FF).copy(0.6f)) {
                                    Text("Requisitos", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color.Black, fontSize = 12.sp)
                                }
                                Surface(shape = RoundedCornerShape(15.dp), color = Color(0xFFB388FF).copy(0.6f)) {
                                    Text("Formularios de adopción", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color.Black, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(modifier = Modifier.size(110.dp), shape = CircleShape, color = Color(0xFFA5D6A7).copy(0.5f)) {
                                Image(
                                    painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSKMNb_rC4gjR33VZI8tvr0sqAcYqWxCbE3Q&s"),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun MascotaCardVerticalAdopcion(mascota: Mascota, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(mascota.fotoPrincipal),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = if (mascota.id % 2 == 0) Color(0xFF4C86F9) else Color(0xFF7B5EE1)
                ) {
                    Text(
                        text = if (mascota.id % 2 == 0) "NUEVO" else "URGENTE",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 8.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text("${mascota.especie} • ${mascota.edadAprox} años", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TagChipAdopcion("Juguetón")
                    TagChipAdopcion("Tranquilo")
                }
            }
        }
    }
}

@Composable
fun TagChipAdopcion(text: String) {
    Surface(
        color = Color(0xFFD9D9D9),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = Color(0xFF7333BE))
    }
}

@Composable
fun SectionTitleAdopcion(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}
