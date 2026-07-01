package com.example.rescatando_mascotas_forever.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationListScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val foundations by viewModel.filteredFoundations.collectAsState()

    var showCityDropdown by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { MainTopBar(drawerState = drawerState, scope = scope, scrollBehavior = scrollBehavior) },
            bottomBar = { AppBottomBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 1. Hero Section (Banner Amarillo)
                item {
                    Box(modifier = Modifier.fillMaxWidth().background(WebYellow).padding(24.dp)) {
                        Column {
                            Text("Fundaciones\nProtectoras", fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color(0xFF673AB7), lineHeight = 40.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(color = Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(4.dp)) {
                                Text("💛 6 fundaciones trabajando por los animales", color = WebYellow, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 2. Filtros
                item {
                    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = { Text("Buscar fundación...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                Button(
                                    onClick = { showCityDropdown = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Text(selectedCity, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                DropdownMenu(
                                    expanded = showCityDropdown,
                                    onDismissRequest = { showCityDropdown = false },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                                    offset = androidx.compose.ui.unit.DpOffset(0.dp, 4.dp)
                                ) {
                                    cities.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(city, color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = { viewModel.onCitySelected(city); showCityDropdown = false },
                                            colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSurface)
                                        )
                                    }
                                }
                            }
                            Button(onClick = { viewModel.clearFilters() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                                Icon(Icons.Default.Refresh, null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }

                // 3. Resultados
                item {
                    Text("Mostrando ${foundations.size} resultados", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
                }

                items(foundations.size) { index ->
                    val foundation = foundations[index]
                    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        FoundationCard(foundation, navController)
                    }
                }

                item { SupportSection() }
                item { DetailedFooter() }
            }
        }
    }
}

// --- PIEZAS DEL DISEÑO QUE FALTABAN ---

@Composable
fun FoundationCard(foundation: Foundation, navController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).background(WebPurpleGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Business, null, tint = Color.White, modifier = Modifier.size(50.dp))
                    Text("Fundación", color = Color.White.copy(0.8f), fontSize = 12.sp)
                }
                if (foundation.isVerified) {
                    Surface(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp), color = WebVerifiedGreen, shape = RoundedCornerShape(4.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verificada", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(foundation.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(foundation.city, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate("foundation_detail/${foundation.name}") },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(WebPurpleGradient), contentAlignment = Alignment.Center) {
                        Text("Ver más >", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SupportSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp, horizontal = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Pets, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Apoya a las fundaciones", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Cada fundación trabaja incansablemente por los animales. Conoce su labor y súmate a su causa.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun DetailedFooter() {
    Column(modifier = Modifier.fillMaxWidth().background(WebPurpleGradient).padding(30.dp)) {
        Text("Rescatando Mascotas Forever", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Sanando su historia", color = Color.White.copy(0.8f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text("© 2025 Todos los derechos reservados.", color = Color.White.copy(0.6f), fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}
