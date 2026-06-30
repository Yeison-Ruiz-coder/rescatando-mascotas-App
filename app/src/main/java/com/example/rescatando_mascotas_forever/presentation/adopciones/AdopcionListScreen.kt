package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.PetCard
import com.example.rescatando_mascotas_forever.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(
    navController: NavHostController,
    viewModel: AdopcionViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val mascotas by viewModel.mascotas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val lastPage by viewModel.lastPage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedEspecie by remember { mutableStateOf("Todas") }

    val especies = listOf("Todas", "Perro", "Gato", "Ave", "Otro")

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MainTopBar(
                    drawerState = drawerState, 
                    scope = scope,
                    onTitleClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                AppBottomBar(
                    navController = navController,
                    onReselect = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
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
                                "Mascotas",
                                color = WebPrimary,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = WebPrimary.copy(alpha = 0.1f),
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Favorite, null, tint = WebPrimary, modifier = Modifier.padding(4.dp))
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Encuentra a tu compañero ideal",
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
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
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar mascota...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = WebPrimary) },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WebPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(especies) { especie ->
                                CustomChipItem(
                                    text = especie,
                                    selected = selectedEspecie == especie,
                                    onClick = { 
                                        selectedEspecie = especie
                                        viewModel.cargarMascotas(especie = especie, page = 1)
                                    }
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = WebPrimary)
                        }
                    }
                } else if (error != null) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(text = error!!, color = WebDanger, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    val filteredMascotas = mascotas.filter {
                        it.nombre.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredMascotas.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                Text("No se encontraron mascotas", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                    } else {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                val chunks = filteredMascotas.chunked(2)
                                chunks.forEach { rowMascotas ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        rowMascotas.forEach { mascota ->
                                            PetCard(
                                                mascota = mascota,
                                                onClick = { navController.navigate("mascota_detalle/${mascota.id}") },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        if (rowMascotas.size < 2) {
                                            repeat(2 - rowMascotas.size) { Spacer(modifier = Modifier.weight(1f)) }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                        
                        // Controles de Paginación
                        item {
                            if (lastPage > 1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { viewModel.prevPage(selectedEspecie) },
                                        enabled = currentPage > 1,
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WebPrimary)
                                    }

                                    Text(
                                        "Página $currentPage de $lastPage",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    IconButton(
                                        onClick = { viewModel.nextPage(selectedEspecie) },
                                        enabled = currentPage < lastPage,
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = WebPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun CustomChipItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) WebPrimary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (selected) WebPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (selected) StaticWhite else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
        )
    }
}

@Composable
fun AdoptionStepItem(number: Int, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(WebPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = StaticWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
    }
}
