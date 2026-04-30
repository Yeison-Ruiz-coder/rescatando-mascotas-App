package com.example.rescatando_mascotas_forever.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
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
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
                        label = { Text("Inicio", color = Color.White) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("adopciones") },
                        icon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(0.6f)) },
                        label = { Text("Buscar", color = Color.White.copy(0.6f)) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White.copy(0.6f)) },
                        label = { Text("Reportar", color = Color.White.copy(0.6f)) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("perfil") },
                        icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White.copy(0.6f)) },
                        label = { Text("Perfil", color = Color.White.copy(0.6f)) }
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6EEE9))
            ) {
                item { HeaderSection() }
                item {
                    SectionTitle("Explorar")
                    CategoryRow()
                }
                item {
                    SectionTitle("Cerca de ti")
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (mascotas.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No hay mascotas disponibles", color = Color.Gray)
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(mascotas) { mascota ->
                                MascotaCardVertical(mascota)
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Próximos eventos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5E49BF))
                        TextButton(onClick = { }) {
                            Text("Ver todos →", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    EventList()
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Text("BUENOS DIAS, VALERIA", color = Color.White.copy(0.8f), fontSize = 12.sp)
            Text(
                "Encuentra tu\ncompañero ideal 🐶",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(45.dp),
                shape = RoundedCornerShape(25.dp),
                color = Color.White.copy(0.2f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buscar por raza, nombre, ciudad...", color = Color.White.copy(0.7f), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color(0xFF5E49BF),
        modifier = Modifier.padding(16.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CategoryRow() {
    val categories = listOf(
        CategoryItem("Perros", Icons.Default.Info, Color(0xFF5E49BF), true),
        CategoryItem("Gatos", Icons.Default.Info, Color.White, false),
        CategoryItem("Conejos", Icons.Default.Info, Color.White, false),
        CategoryItem("Aves", Icons.Default.Info, Color.White, false)
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = cat.bgColor,
                shadowElevation = 2.dp,
                modifier = Modifier.height(40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        cat.icon, 
                        contentDescription = null, 
                        modifier = Modifier.size(16.dp),
                        tint = if (cat.isSelected) Color.White else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        cat.name, 
                        color = if (cat.isSelected) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaCardVertical(mascota: Mascota) {
    // Construir la URL completa para la imagen desde Railway
    val fullImageUrl = if (mascota.fotoPrincipal?.startsWith("http") == true) {
        mascota.fotoPrincipal
    } else {
        "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/${mascota.fotoPrincipal}"
    }

    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
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
                Text(mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp , color = Color.Black)
                Text("${mascota.especie} • ${mascota.edadAprox ?: 0} años", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TagChip("Juguetón")
                    TagChip("Tranquilo")
                }
            }
        }
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        color = Color(0xFFD9D9D9),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = Color(0xFF7333BE))
    }
}

@Composable
fun EventList() {
    val events = listOf(
        EventData("15 Mar", "Jornada de Adopción 🐾", "Parque Simón Bolívar", "Gratis"),
        EventData("22 Mar", "Feria Mascota Feliz 🎉", "C.C. Gran Estación", "$15.000 COP")
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        events.forEach { event ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF5E49BF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(event.date, color = Color.White, textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = event.title, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 14.sp,
                            color = Color(0xFF5E49BF) 
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Red)
                            Text(event.location, fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(color = Color(0xFFD1FADF), shape = RoundedCornerShape(4.dp)) {
                            Text(event.price, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, color = Color(0xFF027A48))
                        }
                    }
                }
            }
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector, val bgColor: Color, val isSelected: Boolean)
data class EventData(val date: String, val title: String, val location: String, val price: String)
