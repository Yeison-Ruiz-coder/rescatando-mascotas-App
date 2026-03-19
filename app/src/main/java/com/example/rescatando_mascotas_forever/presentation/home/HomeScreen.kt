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
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient

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
                AppBottomBar(navController)
            },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Bienvenida Personalizada
                item { 
                    HomeHeader() 
                }

                // Banner Promocional con Imagen de Mascota
                item {
                    PromoBanner(navController)
                }

                // SECCIÓN: PROCESO DE ADOPCIÓN
                item {
                    SectionHeader("Estado de tu proceso", "Ver detalles") {
                        navController.navigate("proceso_adopcion")
                    }
                    AdoptionProcessHomeCard(navController)
                }

                // Sección de Categorías
                item {
                    SectionHeader("Categorías", "Ver todas") {
                        navController.navigate("adopciones")
                    }
                    CategoryList(navController)
                }

                // Mascotas en Adopción (Cerca de ti)
                item {
                    SectionHeader("Cerca de ti", "Ver más") {
                        navController.navigate("adopciones")
                    }
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF673AB7))
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            items(mascotas) { mascota ->
                                ModernMascotaCard(mascota, onClick = {
                                    // Podría navegar al detalle si existiera la ruta
                                })
                            }
                        }
                    }
                }

                // SECCIÓN: ÚLTIMOS RESCATES
                item {
                    SectionHeader("Últimos rescates", "Ver mapa") {
                        navController.navigate("ultimos_rescates")
                    }
                    RecentRescuesRow(navController)
                }

                // SECCIÓN: REPORTE RÁPIDO (La tarjeta roja)
                item {
                    SectionHeader("Reportar emergencia", "Ayuda") {
                        navController.navigate("formulario_rescate")
                    }
                    QuickRescateCard(navController)
                }

                // SECCIÓN: EVENTOS
                item {
                    SectionHeader("Próximos eventos", "Calendario") {
                        navController.navigate("eventos")
                    }
                    HomeEventsList(navController)
                }

                // SECCIÓN: NOSOTROS (Ahora al final)
                item {
                    SectionHeader("Nuestra fundación", "Saber más") {
                        navController.navigate("nosotros")
                    }
                    AboutUsHomeCard(navController)
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "¡Hola, Yeison! 👋",
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF2E1A7A)
                )
                Text(
                    "Popayán, Cauca", 
                    fontSize = 14.sp, 
                    color = Color.Gray
                )
            }
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0xFFD1C4E9)
            ) {
                Icon(Icons.Default.Notifications, null, modifier = Modifier.padding(12.dp), tint = Color(0xFF673AB7))
            }
        }
    }
}

@Composable
fun PromoBanner(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo de una mascota feliz
            Image(
                painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1534361960057-19889db9621e?q=80&w=1000"), 
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Capa de degradado oscuro para que el texto resalte
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Adopta un amigo,\ncambia una vida",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate("adopciones") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Saber más", 
                            color = Color(0xFF2E1A7A), 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String, onActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            action, 
            fontSize = 13.sp, 
            color = Color(0xFF673AB7), 
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun CategoryList(navController: NavHostController) {
    val categories = listOf(
        CategoryData(
            name = "Perros", 
            iconPainter = painterResource(id = R.drawable.ic_dog_silhouette),
            bgColor = Color(0xFFFFE0B2), 
            iconColor = Color(0xFFE65100), 
            route = "adopciones?especie=Perro"
        ),
        CategoryData(
            name = "Gatos", 
            iconPainter = painterResource(id = R.drawable.ic_cat_silhouette),
            bgColor = Color(0xFFE1BEE7), 
            iconColor = Color(0xFF4A148C), 
            route = "adopciones?especie=Gato"
        ),
        CategoryData(
            name = "Otros", 
            iconVector = Icons.Rounded.Category,
            bgColor = Color(0xFFC8E6C9), 
            iconColor = Color(0xFF1B5E20), 
            route = "adopciones"
        ),
        CategoryData(
            name = "Tienda", 
            iconVector = Icons.Rounded.Storefront,
            bgColor = Color(0xFFB3E5FC), 
            iconColor = Color(0xFF01579B), 
            route = "tienda"
        )
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(categories) { cat ->
            CategoryItem(cat) {
                navController.navigate(cat.route)
            }
        }
    }
}

data class CategoryData(
    val name: String,
    val iconVector: ImageVector? = null,
    val iconPainter: Painter? = null,
    val bgColor: Color,
    val iconColor: Color,
    val route: String
)

@Composable
fun CategoryItem(category: CategoryData, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(22.dp),
            color = category.bgColor,
            shadowElevation = 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (category.iconPainter != null) {
                    Icon(
                        painter = category.iconPainter,
                        contentDescription = category.name,
                        modifier = Modifier.size(32.dp),
                        tint = category.iconColor
                    )
                } else if (category.iconVector != null) {
                    Icon(
                        imageVector = category.iconVector,
                        contentDescription = category.name,
                        modifier = Modifier.size(32.dp),
                        tint = category.iconColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )
    }
}

@Composable
fun ModernMascotaCard(mascota: Mascota, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(mascota.fotoPrincipal),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(10.dp).align(Alignment.BottomStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        "${mascota.genero} • ${mascota.edadAprox} años",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        mascota.nombre, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 16.sp, 
                        color = Color(0xFF2E1A7A),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.LocationOn, null, tint = Color.Red, modifier = Modifier.size(14.dp))
                }
                Text(mascota.ubicacion, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun QuickRescateCard(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("formulario_rescate") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color.Red
            ) {
                Icon(Icons.Default.Warning, null, modifier = Modifier.padding(12.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("¿Encontraste una mascota?", fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                Text("Repórtala aquí para ayudarla rápido", fontSize = 12.sp, color = Color(0xFFD32F2F))
            }
        }
    }
}

@Composable
fun HomeEventsList(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { navController.navigate("eventos") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFEDE7F6), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("15", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF673AB7))
                Text("MAR", fontSize = 10.sp, color = Color(0xFF673AB7))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Gran Jornada de Adopción 2025", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text("Parque Simón Bolívar • 9:00 AM", fontSize = 12.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun AdoptionProcessHomeCard(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("proceso_adopcion") },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.List, null, tint = Color(0xFF0288D1), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Tu Proceso de Adopción", fontWeight = FontWeight.Bold, color = Color(0xFF01579B))
                Text("Consulta requisitos y estado de tu solicitud", fontSize = 12.sp, color = Color(0xFF0277BD))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFF0288D1))
        }
    }
}

@Composable
fun RecentRescuesRow(navController: NavHostController) {
    val rescues = listOf(
        Pair("https://images.unsplash.com/photo-1537151608828-ea2b11777ee8", "Barrio El Modelo"),
        Pair("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba", "Sector Norte")
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rescues) { rescue ->
            Card(
                modifier = Modifier.width(140.dp).clickable { navController.navigate("ultimos_rescates") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(rescue.first),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        rescue.second,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun AboutUsHomeCard(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("nosotros") },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Conócenos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4A148C))
                Text(
                    "Sanando su historia y uniendo familias desde 2020.",
                    fontSize = 12.sp,
                    color = Color(0xFF4A148C)
                )
            }
            Icon(Icons.Default.Info, null, modifier = Modifier.size(40.dp), tint = Color(0xFF9C27B0))
        }
    }
}
