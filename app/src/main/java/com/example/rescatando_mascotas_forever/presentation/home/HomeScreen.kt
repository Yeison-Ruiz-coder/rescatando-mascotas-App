package com.example.rescatando_mascotas_forever.presentation.home

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val eventos by viewModel.eventos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategoria by viewModel.selectedCategoria.collectAsState()
    
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val user = remember { sessionManager.getUser() }
    
    val firstName = user?.nombre?.split(" ")?.get(0)?.uppercase() ?: "USUARIO"
    
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "BUENOS DÍAS"
            in 12..18 -> "BUENAS TARDES"
            else -> "BUENAS NOCHES"
        }
    }

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
                AppBottomBar(navController = navController)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6EEE9))
            ) {
                item { HeaderSection(greeting, firstName) }
                
                // Nueva sección de Acciones Rápidas
                item {
                    QuickActionsRow(navController)
                }

                item {
                    SectionTitle("Explorar por categoría")
                    CategoryRow(
                        selectedCategoria = selectedCategoria,
                        onCategoriaSelected = { viewModel.selectCategoria(it) }
                    )
                }

                item {
                    BannerPromocional()
                }

                item {
                    SectionTitle("Cerca de ti")
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF673AB7))
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
                        TextButton(onClick = { navController.navigate("eventos") }) {
                            Text("Ver todos →", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    EventList(eventos)
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun HeaderSection(greeting: String, userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(brush = AppMainGradient)
    ) {
        // Imagen de fondo temática (perros y gatos) con opacidad baja
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1548199973-03c40e556566?auto=format&fit=crop&w=800&q=80"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.3f),
            contentScale = ContentScale.Crop
        )

        // Overlay sutil para mejorar el contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$greeting, $userName",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Encuentra tu\ncompañero ideal",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp
                    )
                }
                // Icono decorativo de patita
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(70.dp).offset(x = 10.dp, y = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.25f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Buscar por raza, nombre, ciudad...", 
                        color = Color.White.copy(alpha = 0.8f), 
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsRow(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionItem("Donar", Icons.Default.Favorite, Color(0xFFE91E63)) {
            navController.navigate("donaciones")
        }
        QuickActionItem("Veterinarias", Icons.Default.LocalHospital, Color(0xFF4CAF50)) {
            navController.navigate("veterinarias")
        }
        QuickActionItem("Voluntarios", Icons.Default.Face, Color(0xFF2196F3)) {
            navController.navigate("rescatista_contactos")
        }
    }
}

@Composable
fun QuickActionItem(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
    }
}

@Composable
fun BannerPromocional() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1A7A))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Adopta, no compres", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Cientos de peluditos buscan hogar", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            Icon(
                Icons.Default.Pets,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(60.dp).offset(x = 20.dp)
            )
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
fun CategoryRow(
    selectedCategoria: String,
    onCategoriaSelected: (String) -> Unit
) {
    val categories = listOf(
        CategoryItem("Todos", Icons.Default.Apps, Color(0xFF5E49BF)),
        CategoryItem("Perros", Icons.Default.Pets, Color(0xFF5E49BF)),
        CategoryItem("Gatos", Icons.Default.Pets, Color(0xFF5E49BF)),
        CategoryItem("Conejos", Icons.Default.Pets, Color(0xFF5E49BF)),
        CategoryItem("Aves", Icons.Default.Pets, Color(0xFF5E49BF))
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            val isSelected = cat.name == selectedCategoria
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Color(0xFF5E49BF) else Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .height(40.dp)
                    .clickable { onCategoriaSelected(cat.name) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        cat.icon, 
                        contentDescription = null, 
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) Color.White else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        cat.name, 
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaCardVertical(mascota: Mascota) {
    val fullImageUrl = if (mascota.fotoPrincipal?.startsWith("http") == true) {
        mascota.fotoPrincipal
    } else {
        "https://rescatando-mascotas-backend-final-production.up.railway.app/storage/${mascota.fotoPrincipal}"
    }

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop
                )
                // Badge de estado
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = if (mascota.id % 2 == 0) "NUEVO" else "URGENTE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        color = if (mascota.id % 2 == 0) Color(0xFF4C86F9) else Color(0xFFF44336),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                // Botón favorito
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp).size(18.dp),
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
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
                    "${mascota.especie} • ${mascota.edadAprox?.toInt() ?: 0} años", 
                    fontSize = 12.sp, 
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Red.copy(alpha = 0.6f))
                    Text(
                        text = mascota.ubicacion ?: "Popayán",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun EventList(eventos: List<com.example.rescatando_mascotas_forever.data.network.models.Evento>) {
    if (eventos.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No hay eventos programados", color = Color.Gray, fontSize = 14.sp)
        }
        return
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        eventos.forEach { evento ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp), 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Contenedor de Fecha
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppMainGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Intentamos extraer día y mes
                            val parts = evento.fecha.split(" ")
                            val dia = if (parts.isNotEmpty()) parts[0] else "00"
                            val mes = if (parts.size > 1) parts[1].take(3).uppercase() else "MES"
                            
                            Text(
                                dia, 
                                color = Color.White, 
                                fontSize = 18.sp, 
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                mes, 
                                color = Color.White.copy(alpha = 0.8f), 
                                fontSize = 10.sp, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = evento.nombre, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 15.sp, 
                            color = Color(0xFF2E1A7A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(
                                evento.lugar, 
                                fontSize = 12.sp, 
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    // Etiqueta de Tipo (Ej: Gratis, Concurso)
                    Surface(
                        color = Color(0xFF673AB7).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            evento.tipo ?: "Evento", 
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color(0xFF673AB7),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector, val bgColor: Color)
data class EventData(val date: String, val title: String, val location: String, val price: String, val statusColor: Color)
