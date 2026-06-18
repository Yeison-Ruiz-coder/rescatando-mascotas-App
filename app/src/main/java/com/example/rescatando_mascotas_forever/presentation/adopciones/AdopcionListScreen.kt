package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(
    navController: NavHostController,
    viewModel: AdopcionViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val mascotas by viewModel.mascotas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedEspecie by remember { mutableStateOf("Todas") }

    val especies = listOf("Todas", "Perro", "Gato", "Ave", "Otro")

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
            },
            containerColor = Color(0xFF0F0E17)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.logo_foreground),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.05f
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "adopción",
                                color = Color(0xFF7B5EE1).copy(alpha = 0.9f),
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.offset(y = 15.dp)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.1f),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.padding(6.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Tenemos ${mascotas.size} mascotas esperando un hogar",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "FILTRAR MASCOTAS",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar por nombre...", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF7B5EE1)) },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7B5EE1),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1B1A23),
                                unfocusedContainerColor = Color(0xFF1B1A23)
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            items(especies) { especie ->
                                CustomChipItem(
                                    text = especie,
                                    selected = selectedEspecie == especie,
                                    onClick = { selectedEspecie = especie }
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF7B5EE1), strokeWidth = 3.dp)
                        }
                    }
                } else if (error != null) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text(text = error!!, color = Color.Red, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    item {
                        val filteredMascotas = mascotas.filter { 
                            (selectedEspecie == "Todas" || it.especie?.contains(selectedEspecie, ignoreCase = true) == true) &&
                            (it.nombre.contains(searchQuery, ignoreCase = true))
                        }

                        if (filteredMascotas.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                Text("No se encontraron mascotas", color = Color.White.copy(alpha = 0.5f))
                            }
                        } else {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                val chunks = filteredMascotas.chunked(2)
                                chunks.forEach { rowMascotas ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        rowMascotas.forEach { mascota ->
                                            ModernPetCard(mascota, navController, Modifier.weight(1f))
                                        }
                                        if (rowMascotas.size < 2) {
                                            repeat(2 - rowMascotas.size) { Spacer(modifier = Modifier.weight(1f)) }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun CustomChipItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFF7B5EE1) else Color(0xFF1B1A23),
        border = BorderStroke(1.dp, if (selected) Color(0xFF7B5EE1) else Color.White.copy(alpha = 0.1f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
        )
    }
}

@Composable
fun ModernPetCard(mascota: Mascota, navController: NavHostController, modifier: Modifier = Modifier) {
    val fullImageUrl = if (mascota.fotoPrincipal?.startsWith("http") == true) {
        mascota.fotoPrincipal
    } else {
        "${Constants.BASE_URL}storage/${mascota.fotoPrincipal}"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navController.navigate("mascota_detalle/${mascota.id}") },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A23)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column {
            Box(modifier = Modifier.height(150.dp).fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 200f
                            )
                        )
                )

                Surface(
                    modifier = Modifier.padding(10.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = mascota.nombre,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PetTag(mascota.especie ?: "Perro", Color(0xFF4A90E2))
                    PetTag(mascota.genero ?: "Macho", Color(0xFF9C27B0))
                    val edad = mascota.edadAprox?.toInt() ?: 0
                    PetTag("$edad años", Color(0xFFFF9800))
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clickable { navController.navigate("mascota_detalle/${mascota.id}") },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF7B5EE1).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF7B5EE1).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Conocer más",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, null, tint = Color.White, modifier = Modifier.size(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PetTag(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            fontSize = 9.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold
        )
    }
}
