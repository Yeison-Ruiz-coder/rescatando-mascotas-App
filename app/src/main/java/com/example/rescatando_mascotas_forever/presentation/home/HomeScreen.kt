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
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.*
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
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.ui.theme.BrandPurple
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import kotlinx.coroutines.launch
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estado para el refresco manual
    val pullRefreshState = rememberPullToRefreshState()

    // Estado para el detalle de consejos
    var selectedConsejo by remember { mutableStateOf<ConsejoInfo?>(null) }
    val sheetState = rememberModalBottomSheetState()

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
            PullToRefreshBox(
                state = pullRefreshState,
                isRefreshing = isLoading,
                onRefresh = { viewModel.cargarDatosHome() },
                modifier = Modifier.padding(padding),
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = pullRefreshState,
                        isRefreshing = isLoading,
                        modifier = Modifier.align(Alignment.TopCenter),
                        containerColor = MaterialTheme.colorScheme.surface,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    item { 
                        HeaderSection(
                            greeting = greeting, 
                            userName = firstName
                        ) 
                    }
                    
                    item {
                        QuickActionsRow(navController)
                    }

                    item {
                        BannerPromocional()
                    }

                    item {
                        HomeSearchBar(
                            searchQuery = searchQuery,
                            onSearchChange = { viewModel.onSearchQueryChange(it) }
                        )
                    }

                    item {
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                            SectionTitle("Explorar por categoría")
                            CategoryRow(
                                selectedCategoria = selectedCategoria,
                                onCategoriaSelected = { viewModel.selectCategoria(it) }
                            )
                        }
                    }

                    item {
                        SectionTitle("Cerca de ti")
                        if (isLoading && mascotas.isEmpty()) {
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
                        SectionTitle("Próximos eventos", "Ver todos") {
                            navController.navigate("eventos")
                        }
                        EventList(eventos, navController)
                    }

                    item {
                        SectionTitle("Consejos para tu peludito")
                        ConsejosRow { consejo ->
                            selectedConsejo = consejo
                        }
                    }

                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }

                // Mostrar el BottomSheet cuando se selecciona un consejo
                if (selectedConsejo != null) {
                    ModalBottomSheet(
                        onDismissRequest = { selectedConsejo = null },
                        sheetState = sheetState,
                        containerColor = MaterialTheme.colorScheme.surface,
                        dragHandle = { BottomSheetDefaults.DragHandle() }
                    ) {
                        ConsejoDetailContent(
                            consejo = selectedConsejo!!,
                            onDismiss = {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        selectedConsejo = null
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConsejoDetailContent(
    consejo: ConsejoInfo,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = consejo.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = consejo.category,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = consejo.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = consejo.detailedInfo,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ENTENDIDO", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ConsejosRow(onConsejoClick: (ConsejoInfo) -> Unit) {
    val consejos = listOf(
        ConsejoInfo(
            Icons.Default.HealthAndSafety, 
            "Salud", 
            "Vacunas al día: ¿Cuándo toca la siguiente?",
            "El esquema básico de vacunación para perros incluye la Pentavalente (Parvovirus, Moquillo, Hepatitis, Parainfluenza y Leptospirosis) y la Rabia. Se recomienda aplicar refuerzos anualmente. Para gatos, es vital la Triple Felina y la Rabia. ¡No olvides la desparasitación interna cada 3 meses!"
        ),
        ConsejoInfo(
            Icons.Default.Restaurant, 
            "Nutrición", 
            "Alimentos prohibidos para tu perro.",
            "Muchos alimentos humanos son tóxicos para las mascotas. Evita estrictamente: Chocolate (contiene teobromina), cebolla y ajo (dañan glóbulos rojos), uvas y pasas (fallo renal), y productos con Xilitol. Una dieta basada en concentrado de alta calidad es la opción más segura."
        ),
        ConsejoInfo(
            Icons.Default.Home, 
            "Hogar", 
            "Prepara tu casa para un nuevo gatito.",
            "La seguridad es lo primero: instala mallas en ventanas si vives en pisos altos. Provee rascadores para evitar daños en muebles, coloca el arenero en un lugar tranquilo y asegúrate de que no haya plantas tóxicas como los lirios. ¡El enriquecimiento ambiental es clave para su felicidad!"
        )
    )
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(consejos) { consejo ->
            Card(
                modifier = Modifier
                    .width(240.dp)
                    .clickable { onConsejoClick(consejo) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(consejo.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(consejo.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(consejo.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

data class ConsejoInfo(
    val icon: ImageVector,
    val category: String,
    val title: String,
    val detailedInfo: String
)

@Composable
fun HeaderSection(
    greeting: String, 
    userName: String
) {
    val carouselImages = listOf(
        R.drawable.img_carousel_1, 
        R.drawable.img_carousel_2, 
        R.drawable.img_carousel_3,
    )
    
    var currentImageIndex by remember { mutableIntStateOf(0) }
    
    // Cambia la imagen automáticamente cada 5 segundos
    LaunchedEffect(Unit) {
        while(true) {
            kotlinx.coroutines.delay(3000)
            currentImageIndex = (currentImageIndex + 1) % carouselImages.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Black) // Fondo negro para evitar el flash blanco inicial
    ) {
        // Imágenes con transición Crossfade
        Crossfade(
            targetState = carouselImages[currentImageIndex],
            animationSpec = tween(1500),
            label = "HeaderCarousel"
        ) { imageRes ->
            Image(
                painter = androidx.compose.ui.res.painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Overlay con degradado para asegurar legibilidad (Neutro, sin tinte morado)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
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
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(70.dp).offset(x = 10.dp, y = 10.dp)
                )
            }
        }
    }
}

@Composable
fun HomeSearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { 
                Text(
                    "Buscar por raza, nombre, ciudad...", 
                    color = Color.Gray, 
                    fontSize = 14.sp 
                ) 
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, null, tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

@Composable
fun QuickActionsRow(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-30).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard("Donar", Icons.Default.Favorite, Color(0xFFE91E63), Modifier.weight(1f)) {
            navController.navigate("donaciones")
        }
        QuickActionCard("Clínicas", Icons.Default.LocalHospital, Color(0xFF4CAF50), Modifier.weight(1f)) {
            navController.navigate("veterinarias")
        }
        QuickActionCard("Ayuda", Icons.Default.Face, Color(0xFF2196F3), Modifier.weight(1f)) {
            navController.navigate("rescatista_contactos")
        }
    }
}

@Composable
fun QuickActionCard(label: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
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
fun SectionTitle(
    title: String,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (actionText != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = "$actionText →",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CategoryRow(
    selectedCategoria: String,
    onCategoriaSelected: (String) -> Unit
) {
    val categories = listOf(
        CategoryItem("Todos", Icons.Default.Apps, BrandPurple),
        CategoryItem("Perros", Icons.Default.Pets, BrandPurple),
        CategoryItem("Gatos", Icons.Default.Pets, BrandPurple),
        CategoryItem("Conejos", Icons.Default.Pets, BrandPurple),
        CategoryItem("Aves", Icons.Default.Pets, BrandPurple)
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            val isSelected = cat.name == selectedCategoria
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
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
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        cat.name, 
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaCardVertical(mascota: Mascota) {
    val fullImageUrl = com.example.rescatando_mascotas_forever.utils.Constants.getImageUrl(mascota.fotoPrincipal)

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = if (mascota.id % 2 == 0) "NUEVO" else "URGENTE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        color = if (mascota.id % 2 == 0) Color(0xFF4C86F9) else Color(0xFFF44336),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
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
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${mascota.especie} • ${mascota.edadAprox?.toInt() ?: 0} años", 
                    fontSize = 12.sp, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Red.copy(alpha = 0.6f))
                    Text(
                        text = mascota.ubicacion ?: "Popayán",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun EventList(
    eventos: List<com.example.rescatando_mascotas_forever.data.network.models.Evento>,
    navController: NavHostController
) {
    if (eventos.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No hay eventos programados", color = Color.Gray, fontSize = 14.sp)
        }
        return
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(eventos) { evento ->
            // Lógica de fallback para imagen
            val imagePath = when {
                !evento.imagenUrl.isNullOrEmpty() && evento.imagenUrl != "null" -> evento.imagenUrl
                !evento.imagenPublicId.isNullOrEmpty() && evento.imagenPublicId != "null" -> evento.imagenPublicId
                else -> null
            }
            val fullImageUrl = com.example.rescatando_mascotas_forever.utils.Constants.getImageUrl(imagePath)

            Card(
                modifier = Modifier
                    .width(180.dp)
                    .clickable { navController.navigate("eventos/${evento.id}") },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    // Imagen del evento (Parte superior - misma altura que mascotas)
                    Box {
                        Image(
                            painter = rememberAsyncImagePainter(model = fullImageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Badge de tipo flotante
                        Surface(
                            modifier = Modifier.padding(10.dp).align(Alignment.TopStart),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = (evento.tipo ?: "Evento").lowercase(),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = evento.nombre, 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 14.sp, 
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Formateo de fecha y hora
                        val (displayFecha, displayHora) = remember(evento.fecha) {
                            try {
                                if (evento.fecha.contains("T")) {
                                    val parts = evento.fecha.split("T")
                                    val datePart = parts[0]
                                    val timePart = if (parts.size > 1) {
                                        val timeStr = parts[1].substring(0, 5)
                                        val timeParts = timeStr.split(":")
                                        val horaRaw = timeParts[0].toInt()
                                        val amPm = if (horaRaw >= 12) "PM" else "AM"
                                        val hora12 = if (horaRaw % 12 == 0) 12 else horaRaw % 12
                                        String.format("%02d:%s %s", hora12, timeParts[1], amPm)
                                    } else ""
                                    Pair(datePart, timePart)
                                } else {
                                    val parts = evento.fecha.split(" ")
                                    Pair(parts[0], if (parts.size > 1) parts[1] else "")
                                }
                            } catch (e: Exception) {
                                Pair(evento.fecha, "")
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = displayFecha,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = displayHora,
                                color = Color.Gray,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Red.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = evento.lugar, 
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector, val bgColor: Color)
