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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
<<<<<<< HEAD
=======
import androidx.compose.ui.text.style.TextOverflow
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
<<<<<<< HEAD
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
=======
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import java.util.Calendar
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val eventos by viewModel.eventos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
<<<<<<< HEAD
=======
    val selectedCategoria by viewModel.selectedCategoria.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
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
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
<<<<<<< HEAD
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController) },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                item { HomeHeader() }
                item { PromoBanner() }
                item {
                    SectionHeader("Cerca de ti", "Ver más")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(mascotas) { mascota ->
                            ModernMascotaCard(mascota)
=======
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
                item { 
                    HeaderSection(
                        greeting = greeting, 
                        userName = firstName,
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.onSearchQueryChange(it) }
                    ) 
                }
                
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
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
                        }
                    }
                }
                item {
<<<<<<< HEAD
                    SectionHeader("Reportar emergencia", "Ayuda")
                    QuickRescateCard(navController)
                }
                item { Spacer(modifier = Modifier.height(30.dp)) }
=======
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
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
            }
        }
    }
}

@Composable
<<<<<<< HEAD
fun HomeHeader() {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("¡Hola, Yeison! 👋", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1A7A))
                Text("Popayán, Cauca", fontSize = 14.sp, color = Color.Gray)
            }
            Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = Color(0xFFD1C4E9)) {
                Icon(Icons.Default.Notifications, null, modifier = Modifier.padding(12.dp), tint = Color(0xFF673AB7))
            }
        }
    }
}

@Composable
fun PromoBanner() {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(140.dp), shape = RoundedCornerShape(24.dp)) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xFF9C27B0), Color(0xFF3F51B5))))) {
            Row(modifier = Modifier.fillMaxSize().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Adopta un amigo,\ncambia una vida", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.Favorite, null, modifier = Modifier.size(60.dp), tint = Color.White.copy(alpha = 0.3f))
=======
fun HeaderSection(
    greeting: String, 
    userName: String,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(brush = AppMainGradient)
    ) {
        // Imagen de fondo temática (perros y gatos) con opacidad baja
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1548199973-03c40e556566?auto=format&fit=crop&w=800&q=80"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.3f),
            contentScale = ContentScale.Crop
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
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(70.dp).offset(x = 10.dp, y = 10.dp)
                )
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BARRA DE BÚSQUEDA ACTIVA
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                placeholder = { 
                    Text(
                        "Buscar por raza, nombre, ciudad...", 
                        color = Color.Gray, 
                        fontSize = 14.sp 
                    ) 
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF673AB7)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }
                },
                shape = RoundedCornerShape(27.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )
        }
    }
}

@Composable
<<<<<<< HEAD
fun SectionHeader(title: String, action: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(action, fontSize = 13.sp, color = Color(0xFF673AB7))
=======
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
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
    }
}

@Composable
<<<<<<< HEAD
fun ModernMascotaCard(mascota: com.example.rescatando_mascotas_forever.data.network.models.Mascota) {
    Card(modifier = Modifier.width(160.dp).shadow(2.dp, RoundedCornerShape(20.dp)), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column {
            Image(painter = rememberAsyncImagePainter(mascota.fotoPrincipal), contentDescription = null, modifier = Modifier.fillMaxWidth().height(120.dp), contentScale = ContentScale.Crop)
            Text(mascota.nombre, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, color = Color(0xFF2E1A7A))
        }
    }
}

@Composable
fun QuickRescateCard(navController: NavHostController) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { navController.navigate("formulario_rescate") }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(40.dp))
            Spacer(Modifier.width(16.dp))
            Text("¿Encontraste una mascota? Repórtala aquí", fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C), fontSize = 14.sp)
=======
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
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
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
