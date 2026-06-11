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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
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
                        }
                    }
                }
                item {
                    SectionHeader("Reportar emergencia", "Ayuda")
                    QuickRescateCard(navController)
                }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}

@Composable
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
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(action, fontSize = 13.sp, color = Color(0xFF673AB7))
    }
}

@Composable
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
        }
    }
}
