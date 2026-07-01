package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import com.example.rescatando_mascotas_forever.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(
    navController: NavHostController,
    viewModel: EventoViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val state by viewModel.state.collectAsState()
    val filteredEventos by viewModel.filteredEventos.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val catAll = stringResource(R.string.event_cat_all)
    val catFree = "Gratis"
    val catContests = "Concursos"
    val catAdoptions = "Adopciones"

    val categorias = listOf(catAll, catFree, catContests, catAdoptions)

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { 
                MainTopBar(
                    drawerState = drawerState, 
                    scope = scope,
                    scrollBehavior = scrollBehavior
                ) 
            },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = R.mipmap.logo_foreground),
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
                                stringResource(R.string.event_title),
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
                                    Icon(Icons.Default.Event, null, tint = WebPrimary, modifier = Modifier.padding(4.dp))
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    stringResource(R.string.event_subtitle),
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        IconButton(
                            onClick = { viewModel.getEventos(1) },
                            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = WebPrimary)
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
                            value = searchText,
                            onValueChange = { viewModel.onSearchTextChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar evento...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 14.sp) },
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

                        Spacer(Modifier.height(16.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(categorias) { categoria ->
                                EventoCustomChipItem(
                                    text = categoria,
                                    selected = categoria == selectedCategory,
                                    onClick = { viewModel.onCategorySelected(categoria) }
                                )
                            }
                        }
                    }
                }

                when (val currentState = state) {
                    is EventoState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    is EventoState.Success -> {
                        if (filteredEventos.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.EventBusy,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color.Gray.copy(alpha = 0.5f)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No se encontraron eventos",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Intenta con otros filtros o términos",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else {
                            items(filteredEventos, key = { it.id }) { evento ->
                                Box(modifier = Modifier
                                    .animateItem()
                                    .padding(horizontal = 20.dp)) {
                                    EventCard(
                                        evento = evento,
                                        onClick = {
                                            navController.navigate("evento_detalle/${evento.id}")
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            if (currentState.hasMore) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (currentState.isNextPageLoading) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        } else {
                                            TextButton(onClick = { viewModel.loadNextPage() }) {
                                                Text("Cargar más eventos", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is EventoState.Error -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = currentState.message, color = Color.Red)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}

@Composable
fun EventoCustomChipItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) WebPrimary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (selected) WebPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
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
