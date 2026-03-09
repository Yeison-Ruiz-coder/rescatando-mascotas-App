package com.example.rescatando_mascotas_forever.presentation.nosotros

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
fun NosotrosScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                // 1. Header Morado: Mision y Vision
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Mision y Vision", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Light)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Una segunda oportunidad comienza aquí ", color = Color.White.copy(0.9f), fontSize = 14.sp)
                        }
                    }
                }

                // 2. Seccion Mision y Vision (Fondo Azul Oscuro)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2E1A7A))
                            .padding(24.dp)
                    ) {
                        // Misión
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Misión", color = Color.White.copy(0.7f), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Rescatar, proteger y rehabilitar animales en situación de abandono o maltrato, brindándoles atención integral y la oportunidad de encontrar un hogar responsable y amoroso. A través de nuestra plataforma web, conectamos corazones solidarios con mascotas que necesitan una segunda oportunidad.",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                            Image(
                                painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlyHXTTuVFKjbW1gTpMYrgW3qMFU2k0rRmcQ&s"),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        // Visión
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9WYlhI9Y4C9T--pu6QiA6U5hthQXjzBiHUoHmW-RYcw&s"),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Visión", color = Color.White.copy(0.7f), fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Ser la página web de referencia en el rescate y adopción de mascotas, fomentando una cultura de respeto, empatía y responsabilidad hacia los animales. Aspiramos a construir una comunidad digital activa y comprometida, donde cada mascota tenga la oportunidad de vivir con dignidad y amor..",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }

                // 3. Seccion Sobre Nosotros (Fondo Rosa)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8BBD0).copy(0.6f))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sobre Nosotros", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2E1A7A))
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "En nuestra página creemos que cada vida importa. Nacimos del deseo de cambiar la realidad de miles de animales que sufren abandono, maltrato o indiferencia. Somos un equipo de amantes de los animales que, con esfuerzo, cariño y compromiso, rescatamos, cuidamos y buscamos hogares responsables para quienes más lo necesitan.\nMás que una plataforma, somos un puente entre corazones: el de una mascota que espera ser amada y el de una persona dispuesta a dar amor. Aquí no solo promovemos la adopción, también educamos, sensibilizamos y construimos una comunidad que cree en el respeto, la empatía y la acción.\nCada rescate es una historia de esperanza. Y cada adopción, una nueva oportunidad de vida. Gracias por ser parte de este movimiento que transforma el mundo, una patita a la vez.",
                            fontSize = 13.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Info, null, modifier = Modifier.size(40.dp), tint = Color.Black.copy(0.6f))
                            Spacer(modifier = Modifier.width(20.dp))
                            Icon(Icons.Default.Face, null, modifier = Modifier.size(40.dp), tint = Color.Black.copy(0.6f))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}
