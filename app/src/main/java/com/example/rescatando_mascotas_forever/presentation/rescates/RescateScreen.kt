package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val rescatesPrueba = listOf(
        RescateData(
            "15 de Enero del 2026",
            "Parque Caldas, centro",
            "En tratamiento veterinario",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuR3F5T8IPNjBh5NIJ0bQ6bKTcA5IHlHvo8A&s" // Reemplazar con URL real
        ),
        RescateData(
            "3 de Febrero del 2026",
            "Maria Occidente",
            "Disponible en adopción",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIz8HrCAk_EfSVeqVm_aYCbSXoTqsI-NgyvQ&s" // Reemplazar con URL real
        ),
        RescateData(
            "4 de Abril del 2026",
            "Centro, calle 5",
            "En recuperación",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhGJq_ATRDJ9WChNF3IWHOdmRYIErpo7Qy7Q&s"
        ),
        RescateData(
            "7 de Abril del 2026",
            "Santo Domingo",
            "En observación",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-ChyweReTJG4gyDapygRCm-aI8ap3O0vmbA&s"
        )
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
                    NavigationBarItem(selected = false, onClick = { navController.navigate("adopciones") }, icon = { Icon(Icons.Default.Search, null, tint = Color.White.copy(0.6f)) }, label = { Text("Buscar", color = Color.White.copy(0.6f)) })
                    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Default.Warning, null, tint = Color.White.copy(0.6f)) }, label = { Text("Reportar", color = Color.White.copy(0.6f)) })
                    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Default.Person, null, tint = Color.White.copy(0.6f)) }, label = { Text("Perfil", color = Color.White.copy(0.6f)) })
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6EEE9))
            ) {
                // 1. Header "Ultimos rescates" con imagen (Ajustado a 180.dp)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp) // Reducido de 240.dp
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))
                                )
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(15.dp))
                            Text("Ultimos rescates", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Light)
                            Spacer(modifier = Modifier.height(15.dp))
                            Image(
                                painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRz-Mh-4f_z-Mh-4f_z-Mh-4f_z-Mh-4f_z-Mh&s"), 
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp), // Reducido de 140.dp
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // 2. Título "Ultimas mascotas rescatadas"
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ultimas mascotas rescatadas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Cada rescate es una oportunidad de cambiara una vida.\nCorazones,lineas,amparados",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }

                // 3. Lista de Tarjetas de Rescate
                items(rescatesPrueba) { rescate ->
                    RescateCard(rescate)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. Frase motivacional (Morada)
                item {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF7B5EE1))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "\"La segunda oportunidad\nde un corazon valiente\"",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Amando encontre una familia para este adorable cachorro.\nLa experiencia dever como se creaba un vínculo entre\nhumano y animal es simplemente invaluable. Ahora estan\nJuntos y felices.",
                                color = Color.White.copy(0.9f),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                // 5. Sección final "Tu puedes cambiar su historia"
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            border = BorderStroke(4.dp, Color(0xFFF48FB1).copy(0.5f))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1wnYVVl7EoE0VGyczFdbb7D02izZbKq9xTA&s"),
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tu puedes cambiar su historia", fontSize = 18.sp, fontWeight = FontWeight.Bold , color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Cada rescate es una oportunidad para cambiar una vida\nCompasion,empatia,solidaridad",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB388FF)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Formulario de rescate", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Registra como rescatista", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RescateCard(rescate: RescateData) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(rescate.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                RescateInfoRow("Fecha de encuentro :", rescate.fecha)
                RescateInfoRow("Lugar de rescate :", rescate.lugar)
                RescateInfoRow("Estado :", rescate.estado)
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                        modifier = Modifier.height(30.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Ver mas ..", fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun RescateInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(value, fontSize = 11.sp, color = Color.Gray)
    }
}

data class RescateData(val fecha: String, val lugar: String, val estado: String, val imageUrl: String)
