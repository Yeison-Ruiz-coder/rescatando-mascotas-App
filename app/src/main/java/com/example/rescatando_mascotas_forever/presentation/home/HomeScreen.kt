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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar

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
                item { HomeHeader(navController) }
                item { PromoBanner(navController) }

                // SECCIÓN: PROCESO DE ADOPCIÓN
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_process), 
                        stringResource(R.string.home_action_details)
                    ) {
                        navController.navigate("proceso_adopcion")
                    }
                    AdoptionProcessHomeCard(navController)
                }

                // Sección de Categorías
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_categories), 
                        stringResource(R.string.home_action_all)
                    ) {
                        navController.navigate("adopciones")
                    }
                    CategoryList(navController)
                }

                // Mascotas en Adopción
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_nearby), 
                        stringResource(R.string.home_action_more)
                    ) {
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
                                ModernMascotaCard(mascota, navController)
                            }
                        }
                    }
                }

                // SECCIÓN: ÚLTIMOS RESCATES
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_rescues), 
                        stringResource(R.string.home_action_map)
                    ) {
                        navController.navigate("ultimos_rescates")
                    }
                    RecentRescuesRow(navController)
                }

                // SECCIÓN: REPORTE RÁPIDO
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_report), 
                        stringResource(R.string.home_action_help)
                    ) {
                        navController.navigate("formulario_rescate")
                    }
                    QuickRescateCard(navController)
                }

                // SECCIÓN: EVENTOS
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_events), 
                        stringResource(R.string.home_action_calendar)
                    ) {
                        navController.navigate("eventos")
                    }
                    HomeEventsList(navController)
                }

                // SECCIÓN: NOSOTROS
                item {
                    SectionHeader(
                        stringResource(R.string.home_section_about), 
                        stringResource(R.string.home_btn_more)
                    ) {
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
fun HomeHeader(navController: NavHostController) {
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
                    stringResource(R.string.home_welcome),
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF2E1A7A)
                )
                Text(
                    stringResource(R.string.home_location_default), 
                    fontSize = 14.sp, 
                    color = Color.Gray
                )
            }
            Surface(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { navController.navigate("perfil") },
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
            Image(
                painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?q=80&w=1000&auto=format&fit=crop"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                        )
                    )
            )
            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.home_promo_title),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate("nosotros") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(stringResource(R.string.home_btn_more), color = Color(0xFF673AB7), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = action, 
            fontSize = 13.sp, 
            color = Color(0xFF673AB7), 
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun CategoryList(navController: NavHostController) {
    val shopLabel = stringResource(R.string.home_cat_shop)
    val items = listOf(
        Triple(stringResource(R.string.home_cat_dogs), Icons.Default.Pets, Color(0xFFE8F5E9)),
        Triple(stringResource(R.string.home_cat_cats), Icons.Default.CrueltyFree, Color(0xFFFFF3E0)),
        Triple(stringResource(R.string.home_cat_others), Icons.Default.GridView, Color(0xFFE3F2FD)),
        Triple(shopLabel, Icons.Default.Storefront, Color(0xFFF3E5F5))
    )
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { 
                    if (item.first == shopLabel) navController.navigate("donaciones")
                    else navController.navigate("adopciones")
                }
            ) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = item.third
                ) {
                    Icon(item.second, null, modifier = Modifier.padding(20.dp), tint = Color(0xFF2E1A7A))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun ModernMascotaCard(mascota: Mascota, navController: NavHostController) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clickable { navController.navigate("adopciones") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(mascota.fotoPrincipal),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(10.dp).align(Alignment.BottomStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    val ageSuffix = if (mascota.edadAprox.toInt() == 1) stringResource(R.string.pet_age_singular) else stringResource(R.string.pet_age_suffix)
                    Text("${mascota.genero} • ${mascota.edadAprox} $ageSuffix", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, fontSize = 10.sp)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2E1A7A), modifier = Modifier.weight(1f))
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
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = Color.Red) {
                Icon(Icons.Default.Warning, null, modifier = Modifier.padding(12.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(stringResource(R.string.home_report_title), fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                Text(stringResource(R.string.home_report_desc), fontSize = 12.sp, color = Color(0xFFD32F2F))
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
            Column(modifier = Modifier.background(Color(0xFFEDE7F6), RoundedCornerShape(12.dp)).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("15", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF673AB7))
                Text("MAR", fontSize = 10.sp, color = Color(0xFF673AB7))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(stringResource(R.string.mock_event_title_1), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text("${stringResource(R.string.mock_location_1)} • 9:00 AM", fontSize = 12.sp, color = Color.DarkGray)
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
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.List, null, tint = Color(0xFF0288D1), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(stringResource(R.string.home_process_title), fontWeight = FontWeight.Bold, color = Color(0xFF01579B))
                Text(stringResource(R.string.home_process_desc), fontSize = 12.sp, color = Color(0xFF0277BD))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color(0xFF0288D1))
        }
    }
}

@Composable
fun RecentRescuesRow(navController: NavHostController) {
    val rescues = listOf(
        Pair("https://images.unsplash.com/photo-1537151608828-ea2b11777ee8", stringResource(R.string.mock_location_4)),
        Pair("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba", stringResource(R.string.mock_location_5))
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
                    Text(rescue.second, modifier = Modifier.padding(8.dp), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.Black)
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
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.home_about_title), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4A148C))
                Text(stringResource(R.string.home_about_desc), fontSize = 12.sp, color = Color(0xFF4A148C))
            }
            Icon(Icons.Default.Info, null, modifier = Modifier.size(40.dp), tint = Color(0xFF9C27B0))
        }
    }
}
