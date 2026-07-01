package com.example.rescatando_mascotas_forever.presentation.fundacion.rescates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.ui.theme.WebPrimary
import com.example.rescatando_mascotas_forever.ui.theme.WebSecondary
import com.example.rescatando_mascotas_forever.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationRescatesScreen(
    navController: NavHostController,
    viewModel: FoundationRescatesViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(selectedTab) {
        viewModel.fetchRescates(selectedTab)
    }

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = WebPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = WebPrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Cerca de mí", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.LocationOn, null) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Mis Asignados", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.AssignmentInd, null) }
                    )
                }

                when (val currentState = state) {
                    is FoundationRescatesState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = WebPrimary)
                        }
                    }
                    is FoundationRescatesState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Error al cargar datos", fontWeight = FontWeight.Bold)
                                Text(currentState.message, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(16.dp))
                                Button(onClick = { viewModel.fetchRescates(selectedTab) }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    is FoundationRescatesState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = if (selectedTab == 0) "Reportes pendientes en tu zona" else "Rescates bajo tu supervisión",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            if (currentState.rescates.isEmpty()) {
                                item {
                                    Box(Modifier.fillParentMaxHeight(0.7f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Text("No hay rescates para mostrar", color = Color.Gray)
                                    }
                                }
                            } else {
                                items(currentState.rescates) { rescate ->
                                    RescateCardFoundation(
                                        rescate = rescate,
                                        onClick = {
                                            navController.navigate("foundation_rescate_detail/${rescate.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RescateCardFoundation(
    rescate: Rescate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Usamos Image + rememberAsyncImagePainter para máxima compatibilidad (como en PetCard)
            val painter = rememberAsyncImagePainter(Constants.getImageUrl(rescate.fotoPrincipal))

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val colorBadge = when (rescate.prioridad?.lowercase()) {
                        "alta" -> Color(0xFFD32F2F)
                        "media" -> Color(0xFFF57C00)
                        else -> WebPrimary
                    }
                    Surface(
                        color = colorBadge.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = rescate.tipoEmergencia.uppercase(),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = colorBadge
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(rescate.fechaRescate.take(10), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = rescate.descripcionRescate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, null, modifier = Modifier.size(14.dp), tint = WebSecondary)
                    Text(
                        text = rescate.lugarRescate,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VER DETALLES →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = WebPrimary,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
